/*
 * Copyright (c) 2010-2023. Institut Pasteur.
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

import org.bioimageanalysis.icy.model.sequence.DimensionId;
import org.bioimageanalysis.icy.model.sequence.Sequence;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.util.VarListener;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.text.ParseException;

public abstract class SequenceDimensionSelector extends Spinner<Integer> {
    protected Var<Sequence> sequence;

    protected DimensionId dimension;

    protected boolean allowAll;

    private final VarListener<Sequence> sequenceListener = new VarListener<>() {
        @Override
        public void valueChanged(final Var<Sequence> source, final Sequence oldValue, final Sequence newValue) {
            updateInterfaceValue();
        }

        @Override
        public void referenceChanged(final Var<Sequence> source, final Var<? extends Sequence> oldReference, final Var<? extends Sequence> newReference) {
            updateInterfaceValue();
        }
    };

    public SequenceDimensionSelector(final Var<Integer> variable, final Var<Sequence> sequence, final DimensionId dim, final boolean allowAll) {
        super(variable);
        this.sequence = sequence;
        this.dimension = dim;
        this.allowAll = allowAll;
    }

    /**
     * Creates a graphical component able to receive user input to change the variable value.<br>
     * Note that listeners are <b>NOT</b> registered here. This operation should be done when (and
     * if) the component is to be used (to avoid memory leaks due to improper cleaning)
     */
    @Override
    public JSpinner createEditorComponent() {
        final JSpinner jSpinner = new JSpinner(new SpinnerNumberModel() {
            @Override
            public void setValue(final Object channel) {
                variable.setValue((Integer) channel);
            }

            @Override
            public Integer getValue() {
                return variable.getValue();
            }

            @Override
            public Integer getPreviousValue() {
                final int minValue = allowAll ? -1 : 0;

                return Math.max(minValue, variable.getValue() - 1);
            }

            @Override
            public Object getNextValue() {
                int maxValue = 65535;

                if (sequence != null && sequence.getValue() != null) maxValue = sequence.getValue().getSize(dimension) - 1;

                return Math.min(maxValue, variable.getValue() + 1);
            }
        });

        final JFormattedTextField ftf = ((JSpinner.DefaultEditor) jSpinner.getEditor()).getTextField();
        ftf.setFormatterFactory(getFormatterFactory());

        return jSpinner;
    }

    protected AbstractFormatterFactory getFormatterFactory() {
        final DefaultFormatter editFormatter = new DefaultFormatter() {
            @Override
            public String valueToString(final Object value) {
                if (value == null) return "";

                if (value.equals(-1)) return "ALL";

                if (sequence == null || sequence.getValue() == null) return value.toString();

                return toUserFriendlyString((Integer) value);
            }

            @Override
            public Object stringToValue(final String text) throws ParseException {
                if (text.equalsIgnoreCase("ALL")) return -1;

                if (sequence == null || sequence.getValue() == null) return Integer.parseInt(text);

                try {
                    final int value = toInteger(text);
                    if (value < -1) throw new ParseException(text, 0);
                    if (value == -1 && !allowAll) throw new ParseException(text, 0);
                    if (value >= sequence.getValue().getSize(dimension)) throw new ParseException(text, 0);

                    return value;
                }
                catch (final NumberFormatException e) {
                    throw new ParseException(text, 0);
                }
            }
        };

        return new DefaultFormatterFactory() {
            @Override
            public AbstractFormatter getDefaultFormatter() {
                return editFormatter;
            }
        };
    }

    protected String toUserFriendlyString(final Integer value) {
        return value.toString();
    }

    protected Integer toInteger(final String text) throws ParseException {
        return Integer.parseInt(text);
    }

    @Override
    protected void updateInterfaceValue() {
        final JFormattedTextField ftf = ((JSpinner.DefaultEditor) getEditorComponent().getEditor()).getTextField();

        ftf.setValue(variable.getValue());
    }

    @Override
    protected void activateListeners() {
        super.activateListeners();
        sequence.addListener(sequenceListener);
    }

    @Override
    protected void deactivateListeners() {
        sequence.removeListener(sequenceListener);
        super.deactivateListeners();
    }
}
