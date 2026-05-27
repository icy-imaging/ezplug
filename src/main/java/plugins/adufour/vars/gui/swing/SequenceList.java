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
import fr.icy.gui.listener.GlobalSequenceListener;
import fr.icy.model.sequence.Sequence;
import fr.icy.system.thread.ThreadUtil;
import plugins.adufour.vars.lang.Var;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.List;

/**
 * Graphical component representing the list of opened sequence, allowing multiple selected items
 *
 * @author Alexandre Dufour
 */
public class SequenceList extends SwingVarEditor<Sequence[]> {
    private ListSelectionListener listener;

    private GlobalSequenceListener mainListener;

    public SequenceList(final Var<Sequence[]> variable) {
        super(variable);
    }

    private static final class SequenceListModel extends DefaultListModel<Sequence> {
        @Override
        public int getSize() {
            return Icy.getMainInterface().getSequences().size();
        }

        @Override
        public Sequence getElementAt(final int index) {
            return Icy.getMainInterface().getSequences().get(index);
        }
    }

    @Override
    protected JComponent createEditorComponent() {
        final JList<Sequence> list = new JList<>();

        list.setModel(new SequenceListModel());

        listener = e -> {
            if (e.getValueIsAdjusting()) return;

            final List<Sequence> selection = list.getSelectedValuesList();

            final Sequence[] sequences = new Sequence[selection.size()];

            for (int i = 0; i < sequences.length; i++) {
                sequences[i] = selection.get(i);
            }

            variable.setValue(sequences);
        };

        mainListener = new GlobalSequenceListener() {
            @Override
            public void sequenceClosed(final Sequence sequence) {
                list.updateUI();
                list.repaint();
            }

            @Override
            public void sequenceOpened(final Sequence sequence) {
                list.updateUI();
                list.repaint();
            }
        };

        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JList<Sequence> getEditorComponent() {
        return (JList<Sequence>) super.getEditorComponent();
    }

    @Override
    public double getComponentVerticalResizeFactor() {
        return 0.5;
    }

    @Override
    public boolean isComponentOpaque() {
        return true;
    }

    @Override
    protected void activateListeners() {
        Icy.getMainInterface().addGlobalSequenceListener(mainListener);
        getEditorComponent().addListSelectionListener(listener);
    }

    @Override
    protected void deactivateListeners() {
        Icy.getMainInterface().removeGlobalSequenceListener(mainListener);
        getEditorComponent().removeListSelectionListener(listener);
    }

    @Override
    protected void updateInterfaceValue() {
        ThreadUtil.invokeLater(() -> {
            // if (Arrays.equals(variable.getValue(), getEditorComponent().getSelectedValues()))
            // return;

            final Sequence[] seqs = variable.getValue();
            final int[] indices = new int[seqs.length];

            for (int i = 0; i < seqs.length; i++)
                indices[i] = Icy.getMainInterface().getSequences().indexOf(seqs[i]);

            getEditorComponent().setSelectedIndices(indices);
        });
    }

}
