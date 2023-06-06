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

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarMutable;

import javax.swing.*;
import java.awt.*;

/**
 * Special editor that changes type according to the underlying variable
 *
 * @author Alexandre Dufour
 */
@SuppressWarnings("unchecked")
public class MutableVarEditor extends SwingVarEditor<Object> {
    public MutableVarEditor(final VarMutable variable) {
        super(variable);
    }

    /**
     * Abstract pointer to the active editor
     */
    private VarEditor<?> varEditor;

    @Override
    protected JComponent createEditorComponent() {
        // bypass accesses via getEditorComponent()
        return null;
    }

    @Override
    public void dispose() {
        varEditor = null;

        super.dispose();
    }

    @Override
    public JComponent getEditorComponent() {
        // deactivate the current editor (if any)
        if (varEditor != null) varEditor.setEnabled(false);

        final Var<?> ref = variable.getReference();

        if (ref != null) {
            varEditor = ref.createVarViewer();
        }
        else {
            varEditor = variable.createVarViewer();
            if (varEditor instanceof Label) ((Label<?>) varEditor).getEditorComponent().setHorizontalAlignment(JLabel.CENTER);
        }

        // activate the new listener
        varEditor.setEnabled(true);

        return (JComponent) varEditor.getEditorComponent();
    }

    @Override
    public Dimension getPreferredSize() {
        return varEditor.getPreferredSize();
    }

    @Override
    public boolean isComponentOpaque() {
        return false;
    }

    @Override
    public boolean isComponentEnabled() {
        return true;
    }

    @Override
    public void setComponentToolTipText(final String s) {
        varEditor.setComponentToolTipText(s);
    }

    @Override
    protected void activateListeners() {

    }

    @Override
    protected void deactivateListeners() {

    }

    @Override
    public boolean isComponentResizeable() {
        return true;
    }

    @Override
    public double getComponentVerticalResizeFactor() {
        return varEditor.getComponentVerticalResizeFactor();
    }

    @Override
    public void setEnabled(final boolean enabled) {

    }

    @Override
    protected void setEditorEnabled(final boolean enabled) {

    }

    @Override
    protected void updateInterfaceValue() {
        // this is done internally by each custom editor
    }
}
