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
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Class defining a slider to select a ranged-value. Not usable yet (package protected until it is
 * ready)
 *
 * @param <N>
 * @author Alexandre Dufour
 */
class Slider<N extends Number> extends SwingVarEditor<N> {
    private ChangeListener changeListener;

    public Slider(final Var<N> variable) {
        super(variable);
    }

    @Override
    public JComponent createEditorComponent() {
        final VarEditorModel<N> model = variable.getDefaultEditorModel();

        if (!(model instanceof final RangeModel<N> constraint)) {
            throw new UnsupportedOperationException("Incorrect model for variable " + variable.getName() + ": " + model);
        }

        // the model range must be converted into integer ticks for the slider component

        final int nbTicks = constraint.getRangeSize();
        final int currentTick = constraint.indexOf(constraint.getDefaultValue());

        final JSlider jSlider = new JSlider(JSlider.HORIZONTAL, 0, nbTicks, currentTick);
        jSlider.setUI(new BasicSliderUI(jSlider) {
            @Override
            protected void calculateTickRect() {
                if (this.slider.getOrientation() == JSlider.HORIZONTAL) {
                    this.tickRect.x = this.trackRect.x - 5;
                    this.tickRect.y = this.trackRect.y + this.trackRect.height;
                    this.tickRect.width = this.trackRect.width;
                    this.tickRect.height = (this.slider.getPaintTicks()) ? this.getTickLength() : 0;
                }
                else super.calculateTickRect();
            }
        });
        jSlider.setMajorTickSpacing(nbTicks);
        jSlider.setMinorTickSpacing(1);
        jSlider.setPaintTicks(true);
        jSlider.setSnapToTicks(true);

        final HashMap<N, String> labels = constraint.getLabels();

        final Hashtable<Integer, JLabel> table = new Hashtable<>();
        final Font font = new Font("Serif", Font.PLAIN, 11);

        // compute the sum of label widths
        int maxWidth = 0;

        for (final N n : labels.keySet()) {
            final JLabel label = new JLabel(labels.get(n));
            label.setOpaque(true);
            label.setFont(font);
            table.put(constraint.indexOf(n), label);
            maxWidth += label.getPreferredSize().width;
        }

        jSlider.setLabelTable(table);
        jSlider.setPaintLabels(true);

        changeListener = e -> {
            if (variable.getReference() == null) variable.setValue(constraint.getValueForIndex(jSlider.getValue()));
        };

        // Assign a maximum size to the spinner to avoid huge interfaces
        final Dimension dim = jSlider.getPreferredSize();
        // dim.setSize(Math.min(dim.width, MAX_SPINNER_WIDTH), dim.height);
        dim.width = Math.min(dim.width, maxWidth);
        jSlider.setPreferredSize(dim);

        return jSlider;
    }

    @Override
    protected void updateInterfaceValue() {
        N value = variable.getValue();
        if (value == null) value = variable.getDefaultValue();

        final int tick = ((RangeModel<N>) variable.getDefaultEditorModel()).indexOf(value);
        getEditorComponent().setValue(tick);
    }

    @Override
    public JSlider getEditorComponent() {
        return (JSlider) super.getEditorComponent();
    }

    @Override
    protected void activateListeners() {
        final JSlider jSlider = getEditorComponent();
        jSlider.addChangeListener(changeListener);
    }

    @Override
    protected void deactivateListeners() {
        final JSlider jSlider = getEditorComponent();
        jSlider.removeChangeListener(changeListener);
    }
}