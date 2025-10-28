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

import org.bioimageanalysis.icy.system.thread.ThreadUtil;
import org.jetbrains.annotations.NotNull;
import plugins.adufour.vars.gui.model.ValueSelectionModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.lang.Var;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.List;

public class ComboBox<T> extends SwingVarEditor<T> {
    private ActionListener actionListener;

    /**
     * Creates a new combo box component for the specified variable. Note that the specified
     * variable must have a constraint of type {@link ValueSelectionModel}
     *
     * @param variable the variable to attach to this component
     * @throws IllegalArgumentException if the variable has no constraint, or if the constraint is not a
     *                                  {@link ValueSelectionModel}
     */
    public ComboBox(final Var<T> variable) throws IllegalArgumentException {
        super(variable);
    }

    private static @NotNull String arrayToString(final Object array) {
        final StringBuilder s = new StringBuilder();
        final int length = Array.getLength(array);
        s.append((length == 0) ? "" : Array.get(array, 0).toString());
        for (int i = 1; i < length; i++)
            s.append(" ").append(Array.get(array, i).toString());
        return s.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public JComponent createEditorComponent() throws IllegalArgumentException {
        final VarEditorModel<T> cons = variable.getDefaultEditorModel();

        List<T> defaultValues = null;
        boolean freeInput = true;
        T defaultValue = variable.getDefaultValue();

        if (cons != null) {
            if (cons instanceof final ValueSelectionModel<T> constraint) {
                defaultValues = constraint.getValidValues();
                freeInput = constraint.isFreeInput();
                defaultValue = constraint.getDefaultValue();
            }
            else
                throw new IllegalArgumentException("Variable " + variable.getName() + " must have a value-type constraint");
        }
        final JComboBox<T> jComboBox;
        if (defaultValues == null) {
            jComboBox = new JComboBox<>();
        }
        else {
            jComboBox = new JComboBox<>((T[]) defaultValues.toArray());
            // jComboBox = new JComboBox<T>(defaultValues.toArray());
        }
        jComboBox.setEditable(freeInput);
        jComboBox.setSelectedItem(defaultValue);

        actionListener = e -> {
            if (variable.getReference() == null)
                updateVariableValue();
        };

        // Override the default renderer to support array-type items
        jComboBox.setRenderer(createRenderer());

        // if the combo box allows user input, override the editor to support array-type items
        if (jComboBox.isEditable())
            jComboBox.setEditor(createEditor());

        return jComboBox;
    }

    protected ListCellRenderer<T> createRenderer() {
        return (list, value, index, isSelected, cellHasFocus) -> {
            String s = "";
            if (value != null) {
                if (value.getClass().isArray())
                    s = arrayToString(value);
                else
                    s = value.toString();
            }
            return new JLabel(s);
        };
    }

    protected ComboBoxEditor createEditor() {
        return new ComboBoxEditor() {
            final JTextField jTextField = new JTextField();
            final Color defaultColor = jTextField.getForeground();
            final Color errorColor = Color.red;

            @Override
            public void addActionListener(final ActionListener l) {

            }

            @Override
            public Component getEditorComponent() {
                jTextField.setFocusable(true);
                final Dimension dim = jTextField.getPreferredSize();
                dim.height = 20;
                jTextField.setPreferredSize(dim);
                return jTextField;
            }

            @Override
            public T getItem() {
                T item;

                try {
                    item = variable.parse(jTextField.getText());
                    jTextField.setForeground(defaultColor);
                    jTextField.setToolTipText(null);
                }
                catch (final NumberFormatException nfE) {
                    item = null;
                    jTextField.setForeground(errorColor);
                    jTextField.setToolTipText("Cannot parse input into a " + getVariable().getClass().getSimpleName());
                }

                return item;
            }

            @Override
            public void removeActionListener(final ActionListener l) {

            }

            @Override
            public void selectAll() {
                jTextField.selectAll();
            }

            @Override
            public void setItem(final Object item) {
                if (item == null) {
                    jTextField.setText("");
                }
                else {
                    jTextField.setText(item.getClass().isArray() ? arrayToString(item) : item.toString());
                }
            }
        };
    }

    /**
     * Replaces the list of values available in the combo box of this variable<br>
     * NOTE: this method will replace the current constraint on the variable
     *
     * @param values            Values to be set as default.
     * @param defaultValueIndex Index of the value to be set as default.
     * @param allowUserInput    If true, the user can change the value.
     */
    public void setDefaultValues(final T[] values, final int defaultValueIndex, final boolean allowUserInput) {
        ThreadUtil.invokeLater(() -> {
            final JComboBox<T> jComboBox = getEditorComponent();

            jComboBox.removeAllItems();
            for (final T value : values)
                jComboBox.addItem(value);
            variable.setDefaultEditorModel(new ValueSelectionModel<>(values, defaultValueIndex, allowUserInput));
            // jComboBox.setSelectedIndex(defaultValueIndex);
            jComboBox.setEditable(allowUserInput);
        });
    }

    @SuppressWarnings("unchecked")
    protected void updateVariableValue() {
        variable.setValue((T) getEditorComponent().getSelectedItem());
    }

    @Override
    protected void updateInterfaceValue() {
        getEditorComponent().getModel().setSelectedItem(variable.getValue());
    }

    @Override
    public void dispose() {
        super.dispose();

        // replace custom instances by new empty ones for garbage collection
        final JComboBox<T> jComboBox = getEditorComponent();
        jComboBox.setRenderer(new DefaultListCellRenderer());
        jComboBox.setModel(new DefaultComboBoxModel<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    public JComboBox<T> getEditorComponent() {
        return (JComboBox<T>) super.getEditorComponent();
    }

    @Override
    protected void activateListeners() {
        getEditorComponent().addActionListener(actionListener);
    }

    @Override
    protected void deactivateListeners() {
        getEditorComponent().removeActionListener(actionListener);
    }
}
