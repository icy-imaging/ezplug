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

import org.bioimageanalysis.icy.Icy;
import org.bioimageanalysis.icy.common.string.StringUtil;
import org.bioimageanalysis.icy.gui.listener.ActiveSequenceListener;
import org.bioimageanalysis.icy.gui.listener.GlobalSequenceListener;
import org.bioimageanalysis.icy.io.FileUtil;
import org.bioimageanalysis.icy.model.sequence.Sequence;
import org.bioimageanalysis.icy.model.sequence.SequenceEvent;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarSequence;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SequenceChooser extends SwingVarEditor<Sequence> {
    private interface SequenceListener extends GlobalSequenceListener, ActiveSequenceListener {
    }

    private SequenceListener listener;

    private JComboSequenceBoxListener jComboSequenceBoxListener;

    public SequenceChooser(final Var<Sequence> variable) {
        super(variable);
    }

    private final class JComboSequenceBoxListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            final JComboBox<Object> jComboSequences = getEditorComponent();

            final Object o = jComboSequences.getSelectedItem();

            // null or String special value ?
            if ((o == null) || o.equals(VarSequence.NO_SEQUENCE) || o.equals(VarSequence.ACTIVE_SEQUENCE))
                variable.setValueAsString((String) o);
            else {
                final Sequence newValue = (Sequence) o;

                jComboSequences.setToolTipText(newValue.getName());

                variable.setValue(newValue);
            }
        }
    }

    private static final class SequenceChooserModel extends DefaultComboBoxModel<Object> {
        public SequenceChooserModel() {
            setSelectedItem(VarSequence.ACTIVE_SEQUENCE);
        }

        @Override
        public int getSize() {
            // index 0: no sequence
            // index 1: active sequence
            return 2 + Icy.getMainInterface().getSequences().size();
        }

        @Override
        public Object getElementAt(final int index) {
            if (index <= 0)
                return VarSequence.NO_SEQUENCE;
            if (index == 1)
                return VarSequence.ACTIVE_SEQUENCE;

            return Icy.getMainInterface().getSequences().get(index - 2);
        }

        @Override
        public void addListDataListener(final ListDataListener l) {
            // don't register anything
        }
    }

    /**
     * Creates a graphical component able to receive user input to change the variable value.<br>
     * Note that listeners are <b>NOT</b> registered here. This operation should be done when (and
     * if) the component is to be used (to avoid memory leaks due to improper cleaning)
     */
    @Override
    public JComponent createEditorComponent() {
        jComboSequenceBoxListener = new JComboSequenceBoxListener();

        final JComboBox<Object> jComboSequences = new JComboBox<>(new SequenceChooserModel());

        jComboSequences.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            if ((value == null) || value.equals(VarSequence.NO_SEQUENCE))
                return new JLabel(VarSequence.NO_SEQUENCE);

            if (value.equals(VarSequence.ACTIVE_SEQUENCE))
                return new JLabel(VarSequence.ACTIVE_SEQUENCE);

            if (value instanceof Sequence) {
                final String s = FileUtil.getFileName(((Sequence) value).getName());
                return new JLabel(StringUtil.limit(s, 24));
            }

            throw new IllegalArgumentException(value.toString());
        });

        listener = new SequenceListener() {
            @Override
            public void sequenceClosed(final Sequence sequence) {
                if (variable.getReference() != null)
                    return;

                // we just closed the sequence that was selected --> set value to ACTIVE SEQUENCE
                if (variable.getValue() == sequence)
                    jComboSequences.setSelectedIndex(1);
                // // no more opened sequence ? --> set to no sequence
                // else if (jComboSequences.getModel().getSize() == 2)
                // variable.setValue(null);

                jComboSequences.repaint();
                jComboSequences.updateUI();
            }

            @Override
            public void sequenceOpened(final Sequence sequence) {
                jComboSequences.repaint();
                jComboSequences.updateUI();
            }

            @Override
            public void sequenceActivated(final Sequence sequence) {
                // variable cannot be changed (linked)
                if (variable.getReference() != null)
                    return;

                // active sequence selection ? --> set current sequence
                if (jComboSequences.getSelectedIndex() == 1)
                    variable.setValueAsString(VarSequence.ACTIVE_SEQUENCE);

                jComboSequences.repaint();
                jComboSequences.updateUI();
            }

            @Override
            public void sequenceDeactivated(final Sequence sequence) {
                //
            }

            @Override
            public void activeSequenceChanged(final SequenceEvent event) {
                //
            }
        };

        if (variable.getReference() == null)
            variable.setValue(Icy.getMainInterface().getActiveSequence());

        return jComboSequences;
    }

    @Override
    public Dimension getPreferredSize() {
        final Dimension dim = super.getPreferredSize();
        dim.height = 20;
        return dim;
    }

    @Override
    public void dispose() {
        super.dispose();

        final JComboBox<Object> jComboSequences = getEditorComponent();

        // replace custom instances by new empty ones for garbage collection
        jComboSequences.setRenderer(new DefaultListCellRenderer());
        jComboSequences.setModel(new DefaultComboBoxModel<>());

    }

    @Override
    protected void updateInterfaceValue() {
        final JComboBox<Object> editor = getEditorComponent();
        final Var<? extends Sequence> v = ((variable.getReference() != null) ? variable.getReference() : variable);

        if ((v instanceof VarSequence) && ((VarSequence) v).isActiveSequenceSelected()) {
            editor.setSelectedIndex(1);
            editor.repaint();
        }
        else if (v.getValue() == null) {
            editor.setSelectedIndex(0);
            editor.repaint();
        }
        else {
            editor.setSelectedItem(v.getValue());
            editor.repaint();
        }

        getEditorComponent()
                .setToolTipText("<html><pre><font size=3>" + variable.getValueAsString(true) + "</font></pre></html>");
    }

    @SuppressWarnings("unchecked")
    @Override
    public JComboBox<Object> getEditorComponent() {
        return (JComboBox<Object>) super.getEditorComponent();
    }

    @Override
    protected void activateListeners() {
        Icy.getMainInterface().addGlobalSequenceListener(listener);
        Icy.getMainInterface().addActiveSequenceListener(listener);
        getEditorComponent().addActionListener(jComboSequenceBoxListener);
    }

    @Override
    protected void deactivateListeners() {
        Icy.getMainInterface().removeGlobalSequenceListener(listener);
        Icy.getMainInterface().removeActiveSequenceListener(listener);
        getEditorComponent().removeActionListener(jComboSequenceBoxListener);
    }
}
