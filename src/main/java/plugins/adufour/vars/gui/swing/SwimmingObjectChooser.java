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

import fr.icy.Icy;
import fr.icy.model.swimmingPool.SwimmingObject;
import fr.icy.model.swimmingPool.SwimmingPoolEvent;
import fr.icy.model.swimmingPool.SwimmingPoolListener;
import fr.icy.system.thread.ThreadUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import plugins.adufour.vars.lang.Var;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SwimmingObjectChooser extends SwingVarEditor<SwimmingObject> implements SwimmingPoolListener {
    private JComboBoxListener jComboBoxListener;

    private ArrayList<SwimmingObject> validObjects;

    /**
     * The combo box model is slightly different from that of SequenceSelector with the following
     * modifications: <br>
     * - the list of pointers to this variable is added to the combo box<br>
     * - no local reference to the currently selected sequence
     */
    private final class JComboBoxModel extends DefaultComboBoxModel<SwimmingObject> {
        @Override
        public void addListDataListener(final ListDataListener l) {
        }

        @Contract(pure = true)
        @Override
        public @Nullable SwimmingObject getElementAt(final int index) {
            // first is the "no" selection
            if (index == 0) return null;

            return validObjects.get(index - 1);
        }

        @Override
        public int getSize() {
            // slot 0 is dedicated to "no selection", everything else is shifted up by one increment
            return 1 + validObjects.size();
        }

    }

    private static final class JComboBoxRenderer implements ListCellRenderer<SwimmingObject> {
        @Contract("_, _, _, _, _ -> new")
        @Override
        public @NotNull Component getListCellRendererComponent(final JList<? extends SwimmingObject> list, final SwimmingObject value, final int index, final boolean isSelected, final boolean cellHasFocus) {
            if (value == null) return new JLabel("No selection");

            final String name = value.getName();
            return new JLabel(name);
        }
    }

    private final class JComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            final SwimmingObject newValue = (SwimmingObject) getEditorComponent().getSelectedItem();

            if (newValue != null) getEditorComponent().setToolTipText(newValue.getName());

            variable.setValue(newValue);
        }
    }

    public SwimmingObjectChooser(final Var<SwimmingObject> variable) {
        super(variable);
    }

    @Override
    public void dispose() {
        super.dispose();

        validObjects.clear();

        final JComboBox<SwimmingObject> jComboBox = getEditorComponent();
        // replace custom instances by new empty ones for garbage collection
        jComboBox.setRenderer(new DefaultListCellRenderer());
        jComboBox.setModel(new DefaultComboBoxModel<>());
    }

    @Override
    public JComponent createEditorComponent() {
        validObjects = new ArrayList<>();
        jComboBoxListener = new JComboBoxListener();
        final JComboBoxModel jComboBoxModel = new JComboBoxModel();
        final JComboBoxRenderer jComboBoxRenderer = new JComboBoxRenderer();

        final JComboBox<SwimmingObject> jComboBox = new JComboBox<>(jComboBoxModel);

        for (final SwimmingObject swObj : Icy.getMainInterface().getSwimmingPool().getObjects())
            if (variable.getDefaultEditorModel().isValid(swObj)) {
                validObjects.add(swObj);
            }

        if (!validObjects.isEmpty()) jComboBox.setSelectedIndex(1);

        jComboBox.setRenderer(jComboBoxRenderer);

        return jComboBox;
    }

    @Override
    protected void updateInterfaceValue() {
        if (variable.getReference() != null) {
            getEditorComponent().setSelectedItem(variable.getValue());
            getEditorComponent().repaint();
        }
        else if (variable.getValue() == null) {
            getEditorComponent().setSelectedIndex(0);
            getEditorComponent().repaint();
        }

        final String valueAsString = variable.getValueAsString(true);

        getEditorComponent().setToolTipText(valueAsString.isEmpty() ? null : "<html><pre><font size=3>" + valueAsString + "</font></pre></html>");
    }

    @Override
    public void swimmingPoolChangeEvent(final @NotNull SwimmingPoolEvent event) {
        final SwimmingObject object = event.getResult();

        if (!variable.getDefaultEditorModel().isValid(object)) return;

        switch (event.getType()) {
            case ELEMENT_ADDED:
                validObjects.add(object);
                break;

            case ELEMENT_REMOVED:
                validObjects.remove(object);
                break;
        }

        ThreadUtil.invokeLater(() -> {
            getEditorComponent().repaint();
            getEditorComponent().updateUI();
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public JComboBox<SwimmingObject> getEditorComponent() {
        return (JComboBox<SwimmingObject>) super.getEditorComponent();
    }

    @Override
    protected void activateListeners() {
        getEditorComponent().addActionListener(jComboBoxListener);
        Icy.getMainInterface().getSwimmingPool().addListener(SwimmingObjectChooser.this);
    }

    @Override
    protected void deactivateListeners() {
        getEditorComponent().removeActionListener(jComboBoxListener);
        Icy.getMainInterface().getSwimmingPool().removeListener(SwimmingObjectChooser.this);
    }

}
