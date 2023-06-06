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

import plugins.adufour.vars.lang.Var;

import javax.swing.*;
import java.awt.*;

public class Label<T> extends SwingVarEditor<T> {
    public final int MAX_WIDTH = 150;

    public Label(final Var<T> variable) {
        super(variable);
    }

    @Override
    protected JComponent createEditorComponent() {
        return new JLabel(variable.getValueAsString(true));
    }

    @Override
    protected void activateListeners() {
    }

    @Override
    protected void deactivateListeners() {
    }

    @Override
    public JLabel getEditorComponent() {
        return (JLabel) super.getEditorComponent();
    }

    @Override
    public Dimension getPreferredSize() {
        final Dimension dim = getEditorComponent().getPreferredSize();
        if (dim.width > MAX_WIDTH) dim.width = MAX_WIDTH;
        return dim;
    }

    @Override
    protected void updateInterfaceValue() {
        getEditorComponent().setText(variable.getValueAsString(true));
    }

    @Override
    public boolean isComponentResizeable() {
        return true;
    }

    @Override
    public double getComponentVerticalResizeFactor() {
        return 0;
    }
}
