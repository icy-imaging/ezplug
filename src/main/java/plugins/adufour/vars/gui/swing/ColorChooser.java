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

import icy.gui.component.button.ColorChooserButton;
import plugins.adufour.vars.lang.Var;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * A color chooser component that appear as a button (filled with the current color value), and
 * which pops up a {@link JColorChooser} component when clicked
 *
 * @author Alexandre Dufour
 */
public class ColorChooser extends SwingVarEditor<Color> {
    private ActionListener listener;

    public ColorChooser(final Var<Color> variable) {
        super(variable);
    }

    @Override
    protected JComponent createEditorComponent() {
        if (getEditorComponent() != null) deactivateListeners();

        listener = arg0 -> {
            final Color defaultColor = variable.getDefaultValue();
            final Color newColor = JColorChooser.showDialog(getEditorComponent(), variable.getName(), defaultColor);
            if (newColor == null) return;
            variable.setValue(newColor);
        };

        final ColorChooserButton button = new ColorChooserButton(variable.getValue());

        // TODO: 05/06/2023 Remove this old implementation of color chooser button
        /*IcyButton button = new IcyButton(new EmptyIcon(16, 16)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(variable.getValue());
                g.fill3DRect(0, 0, getWidth(), getHeight(), true);
            }
        };*/
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        return button;
    }

    @Override
    public JButton getEditorComponent() {
        return (JButton) super.getEditorComponent();
    }

    @Override
    protected void activateListeners() {
        getEditorComponent().addActionListener(listener);
    }

    @Override
    protected void deactivateListeners() {
        getEditorComponent().removeActionListener(listener);
    }

    @Override
    protected void updateInterfaceValue() {
        // nothing to do (the button will repaint itself automatically)
    }

}
