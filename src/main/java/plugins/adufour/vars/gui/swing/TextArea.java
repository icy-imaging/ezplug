/*
 * Copyright (c) 2010-2025. Institut Pasteur.
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

import org.jdesktop.swingx.JXTextArea;
import plugins.adufour.vars.lang.Var;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TextArea<T> extends SwingVarEditor<T> {
    private FocusListener focusListener;

    private KeyListener keyListener;

    public TextArea(final Var<T> variable, final int rows) {
        super(variable);
        getTextArea().setRows(rows);
    }

    @Override
    public JComponent createEditorComponent() {
        final JXTextArea jTextArea = new JXTextArea();
        jTextArea.setText(getVariable().getValueAsString());

        focusListener = new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent e) {
                setVariableValue(jTextArea);
            }
        };

        keyListener = new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                try {
                    variable.parse(jTextArea.getText());
                    jTextArea.setForeground(Color.black);
                }
                catch (final Exception ex) {
                    jTextArea.setForeground(Color.red);
                }
            }

            @Override
            public void keyPressed(final KeyEvent e) {
            }
        };

        final JScrollPane scroll = new JScrollPane(jTextArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setAutoscrolls(true);

        // return jTextArea;
        return scroll;
    }

    private void setVariableValue(final JXTextArea jTextArea) {
        try {
            if (variable.getReference() == null) variable.setValue(variable.parse(jTextArea.getText()));
            jTextArea.setForeground(Color.black);
            jTextArea.setToolTipText(null);
        }
        catch (final RuntimeException e) {
            jTextArea.setForeground(Color.red);
            jTextArea.setToolTipText("Cannot convert input into a " + getVariable().getTypeAsString());
        }
    }

    @Override
    protected void updateInterfaceValue() {
        getTextArea().setText(variable.getValueAsString(true));
    }

    @Override
    public JScrollPane getEditorComponent() {
        return (JScrollPane) super.getEditorComponent();
    }

    public JXTextArea getTextArea() {
        return (JXTextArea) getEditorComponent().getViewport().getView();
    }

    @Override
    protected void activateListeners() {
        getTextArea().addFocusListener(focusListener);
        getTextArea().addKeyListener(keyListener);
    }

    @Override
    protected void deactivateListeners() {
        getTextArea().removeFocusListener(focusListener);
        getTextArea().removeKeyListener(keyListener);
    }
}
