package plugins.adufour.vars.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

import icy.system.FileDrop;
import icy.system.FileDrop.FileDropListener;
import icy.system.thread.ThreadUtil;
import plugins.adufour.vars.gui.FileMode;
import plugins.adufour.vars.gui.model.FileTypeModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.lang.Var;

/**
 * File chooser component
 *
 * @author Alexandre Dufour
 */
public class FileChooser extends SwingVarEditor<File> {
    private ActionListener actionListener;

    /**
     * Creates a new file chooser component
     *
     * @param variable the variable to attach to this file chooser
     */
    public FileChooser(Var<File> variable) {
        super(variable);
    }

    @Override
    public JComponent createEditorComponent() {
        JButton jButton = new JButton();

        String path = null;
        FileMode fileMode = FileMode.ALL;
        boolean allowHidden = false;
        final FileFilter filter;

        VarEditorModel<File> model = variable.getDefaultEditorModel();

        if (model instanceof FileTypeModel) {
            path = ((FileTypeModel) model).getPath();
            fileMode = ((FileTypeModel) model).getMode();
            allowHidden = ((FileTypeModel) model).allowHidden();
            filter = ((FileTypeModel) model).getFilter();
        }
        else {
            filter = null;
        }

        final JFileChooser jFileChooser = new JFileChooser(path);
        switch (fileMode) {
            case FILES:
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jButton.setText("Choose file name...");
                break;

            case FOLDERS:
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jButton.setText("Choose folder name...");
                break;

            default:
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jButton.setText("Choose file or folder name...");
        }

        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.setFileHidingEnabled(!allowHidden);
        if (filter != null)
            jFileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public String getDescription() {
                    return "Compatible files";
                }

                @Override
                public boolean accept(File f) {
                    return filter.accept(f);
                }
            });

        actionListener = e -> {
            if (variable.getValue() != null) {
                jFileChooser.setSelectedFile(variable.getValue());
            }

            if (jFileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
                return;

            variable.setValue(jFileChooser.getSelectedFile());
        };

        FileDropListener fileDropListener = getFileDropListener(fileMode);
        new FileDrop(jButton, BorderFactory.createLineBorder(Color.blue.brighter(), 2), fileDropListener);

        // jButton.addActionListener(actionListener);

        return jButton;
    }

    private FileDropListener getFileDropListener(final FileMode fileMode) {
        return new FileDropListener() {

            @Override
            public void filesDropped(File[] files) {
                if (isActionListenerActive()) {
                    File singleFile = (files != null && files.length >= 1) ? files[0] : null;

                    if (singleFile != null) {
                        switch (fileMode) {
                            case FILES:
                                if (singleFile.isFile()) {
                                    variable.setValue(singleFile);

                                }
                                break;

                            case FOLDERS:
                                if (singleFile.isDirectory())
                                    variable.setValue(singleFile);
                                break;

                            default:
                                variable.setValue(singleFile);
                        }
                    }
                }

            }

            private boolean isActionListenerActive() {
                for (ActionListener l : getEditorComponent().getActionListeners()) {
                    if (l == actionListener)
                        return true;
                }
                return false;
            }
        };
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 20);
    }

    /**
     * Replaces the button text by the given string.
     *
     * @param text The button text.
     */
    public void setButtonText(final String text) {

        ThreadUtil.invokeLater(() -> getEditorComponent().setText(text));
    }

    @Override
    protected void updateInterfaceValue() {
        File newValue = variable.getValue();

        if (newValue != null) {
            setButtonText(newValue.getAbsolutePath());

            String tooltip = "<html><pre><font size=3>";
            tooltip += newValue.getAbsolutePath();
            if (newValue.isDirectory())
                tooltip += "/";
            tooltip += "</font></pre></html>";
            getEditorComponent().setToolTipText(tooltip);
        }
    }

    @Override
    public JButton getEditorComponent() {
        return (JButton) super.getEditorComponent();
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
