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
import java.awt.event.*;

public class TextField<T> extends SwingVarEditor<T> {
    protected ActionListener actionListener;

    protected FocusListener focusListener;

    protected KeyListener keyListener;

    public TextField(final Var<T> variable) {
        super(variable);
    }

    @Override
    public JComponent createEditorComponent() {
        final JTextField jTextField = new JTextField();

        initializeComponent(jTextField);

        return jTextField;
    }

    protected void initializeComponent(final JTextField jTextField) {
        jTextField.setText(getVariable().getValueAsString());
        actionListener = e -> setVariableValue(jTextField);

        focusListener = new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent e) {
                setVariableValue(jTextField);
            }
        };

        keyListener = new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                try {
                    variable.parse(jTextField.getText());
                    jTextField.setForeground(Color.black);
                }
                catch (final Exception ex) {
                    jTextField.setForeground(Color.red);
                }
            }

            @Override
            public void keyPressed(final KeyEvent e) {
            }
        };
    }

    protected void setVariableValue(final JTextField jTextField) {
        try {
            if (variable.getReference() == null) variable.setValue(variable.parse(jTextField.getText()));
            jTextField.setForeground(Color.black);
            jTextField.setToolTipText(null);
        }
        catch (final RuntimeException e) {
            jTextField.setForeground(Color.red);
            jTextField.setToolTipText("Cannot convert input into a " + getVariable().getTypeAsString());
        }
    }

    @Override
    protected void updateInterfaceValue() {
        getEditorComponent().setText(variable.getValueAsString(true));
    }

    @Override
    public JTextField getEditorComponent() {
        return (JTextField) super.getEditorComponent();
    }

    @Override
    protected void activateListeners() {
        getEditorComponent().addActionListener(actionListener);
        getEditorComponent().addFocusListener(focusListener);
        getEditorComponent().addKeyListener(keyListener);
    }

    @Override
    protected void deactivateListeners() {
        getEditorComponent().removeActionListener(actionListener);
        getEditorComponent().removeFocusListener(focusListener);
        getEditorComponent().removeKeyListener(keyListener);
    }
}
