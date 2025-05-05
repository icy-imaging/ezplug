/*
 * Copyright (c) 2010-2024. Institut Pasteur.
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

import plugins.adufour.vars.lang.VarTrigger;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Button extends SwingVarEditor<Integer> {
    private ActionListener listener;

    public Button(final VarTrigger variable) {
        super(variable);

        setNameVisible(false);
    }

    @Override
    protected JButton createEditorComponent() {
        if (getEditorComponent() != null) deactivateListeners();

        listener = e -> ((VarTrigger) variable).trigger();

        return new JButton(variable.getName());
    }

    @Override
    public JButton getEditorComponent() {
        return (JButton) super.getEditorComponent();
    }

    @Override
    public double getComponentVerticalResizeFactor() {
        return 0.0;
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
        // nothing to do (it's just a button with a name)
    }
}
