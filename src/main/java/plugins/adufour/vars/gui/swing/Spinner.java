/*
 * Copyright (c) 2010-2026. Institut Pasteur.
 *
 * This file is part of Icy.
 * Icy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Icy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Icy. If not, see <https://www.gnu.org/licenses/>.
 */

package plugins.adufour.vars.gui.swing;

import plugins.adufour.vars.gui.model.RangeModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.lang.Var;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.ParseException;

public class Spinner<N extends Number> extends SwingVarEditor<N> {
    private static final int MAX_SPINNER_WIDTH = 100;

    private NumberFormatter formatter;

    public Spinner(final Var<N> variable) {
        super(variable);
    }

    @Override
    public JComponent createEditorComponent() {
        final VarEditorModel<N> model = variable.getDefaultEditorModel();

        if (!(model instanceof final RangeModel<N> constraint)) {
            throw new UnsupportedOperationException(
                    "Incorrect model for variable " + variable.getName() + ": " + model);
        }

        final JSpinner jSpinner = new JSpinner(new SpinnerNumberModel(constraint.getDefaultValue(), constraint.getMinimum(), constraint.getMaximum(), constraint.getStepSize()) {
            @SuppressWarnings("unchecked")
            @Override
            public void setValue(final Object value) {
                super.setValue(value);
                variable.setValue((N) value);
            }
        });

        final JSpinner.NumberEditor editor = (JSpinner.NumberEditor) jSpinner.getEditor();
        final JFormattedTextField field = (JFormattedTextField) editor.getComponent(0);
        formatter = (NumberFormatter) field.getFormatter();

        // set the preferred size the maximum possible text length
        try {
            final String min = formatter.valueToString(constraint.getMinimum());
            final String max = formatter.valueToString(constraint.getMaximum());
            final String step = formatter.valueToString(constraint.getStepSize());

            field.setColumns(Math.max(min.length(), Math.max(max.length(), step.length())));
        }
        catch (final ParseException e) {
            throw new RuntimeException(e);
        }

        return jSpinner;
    }

    public void setMaximum(final Comparable<N> maxValue) {
        ((SpinnerNumberModel) getEditorComponent().getModel()).setMaximum(maxValue);
        ((RangeModel<N>) variable.getDefaultEditorModel()).setMaximum(maxValue);
    }

    public void setMinimum(final Comparable<N> minValue) {
        ((SpinnerNumberModel) getEditorComponent().getModel()).setMinimum(minValue);
        ((RangeModel<N>) variable.getDefaultEditorModel()).setMinimum(minValue);
    }

    public void setStepSize(final N stepSize) {
        ((SpinnerNumberModel) getEditorComponent().getModel()).setStepSize(stepSize);
        ((RangeModel<N>) variable.getDefaultEditorModel()).setStepSize(stepSize);
    }

    @Override
    protected void updateInterfaceValue() {
        final N value = variable.getValue();

        if (value == null)
            return;

        // adjust the text format (if not in scientific notation)
        if (!value.toString().contains("E")) {
            if (formatter != null && formatter.getFormat() != null) {
                ((DecimalFormat) formatter.getFormat())
                        .applyPattern(value.toString().replace("-", "").replaceAll("[0-9]", "#"));
            }
        }

        getEditorComponent().setValue(value);
    }

    @Override
    public JSpinner getEditorComponent() {
        return (JSpinner) super.getEditorComponent();
    }

    @Override
    public Dimension getPreferredSize() {
        final Dimension dim = super.getPreferredSize();
        dim.width = Math.min(MAX_SPINNER_WIDTH, dim.width);
        return dim;
    }

    @Override
    protected void activateListeners() {
    }

    @Override
    protected void deactivateListeners() {
    }
}
