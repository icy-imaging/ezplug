package plugins.adufour.vars.gui.swing;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;

import icy.gui.main.GlobalSequenceListener;
import icy.main.Icy;
import icy.sequence.Sequence;
import icy.system.thread.ThreadUtil;
import plugins.adufour.vars.lang.Var;

/**
 * Graphical component representing the list of opened sequence, allowing multiple selected items
 *
 * @author Alexandre Dufour
 */
public class SequenceList extends SwingVarEditor<Sequence[]> {
    private ListSelectionListener listener;

    private GlobalSequenceListener mainListener;

    public SequenceList(Var<Sequence[]> variable) {
        super(variable);
    }

    private static final class SequenceListModel extends DefaultListModel<Sequence> {
        @Override
        public int getSize() {
            return Icy.getMainInterface().getSequences().size();
        }

        @Override
        public Sequence getElementAt(int index) {
            return Icy.getMainInterface().getSequences().get(index);
        }
    }

    @Override
    protected JComponent createEditorComponent() {
        final JList<Sequence> list = new JList<>();

        list.setModel(new SequenceListModel());

        listener = e -> {
            if (e.getValueIsAdjusting()) return;

            List<Sequence> selection = list.getSelectedValuesList();

            Sequence[] sequences = new Sequence[selection.size()];

            for (int i = 0; i < sequences.length; i++) {
                sequences[i] = selection.get(i);
            }

            variable.setValue(sequences);
        };

        mainListener = new GlobalSequenceListener() {
            @Override
            public void sequenceClosed(Sequence sequence) {
                list.updateUI();
                list.repaint();
            }

            @Override
            public void sequenceOpened(Sequence sequence) {
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

            Sequence[] seqs = variable.getValue();
            int[] indices = new int[seqs.length];

            for (int i = 0; i < seqs.length; i++)
                indices[i] = Icy.getMainInterface().getSequences().indexOf(seqs[i]);

            getEditorComponent().setSelectedIndices(indices);
        });
    }

}
