package plugins.adufour.vars.gui.swing;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

import icy.system.FileDrop;
import icy.system.FileDrop.FileDropListener;
import icy.system.thread.ThreadUtil;
import plugins.adufour.vars.gui.FileMode;
import plugins.adufour.vars.gui.model.FileTypeListModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.lang.Var;

/**
 * File chooser component for the Vars library. This component is tailored to select a list of files
 * via a {@link JFileChooser} component
 *
 * @author Alexandre Dufour
 */
public class FilesChooser extends SwingVarEditor<File[]> {
    private ActionListener actionListener;

    public FilesChooser(Var<File[]> variable) {
        super(variable);
    }

    @Override
    public JComponent createEditorComponent() {
        JButton jButton = new JButton();

        String path = null;
        FileMode fileMode = FileMode.ALL;
        boolean allowHidden = false;

        VarEditorModel<File[]> model = variable.getDefaultEditorModel();

        if (model instanceof FileTypeListModel) {
            path = ((FileTypeListModel) model).getPath();
            fileMode = ((FileTypeListModel) model).getMode();
            allowHidden = ((FileTypeListModel) model).allowHidden();
        }

        final JFileChooser jFileChooser = new JFileChooser(path);
        switch (fileMode) {
            case FILES:
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jButton.setText("Select files...");
                break;

            case FOLDERS:
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jButton.setText("Select folders...");
                break;

            default:
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jButton.setText("Select files or folders...");
        }

        jFileChooser.setMultiSelectionEnabled(true);
        jFileChooser.setFileHidingEnabled(allowHidden);

        actionListener = e -> {
            if (variable.getValue() != null) {
                jFileChooser.setSelectedFiles(variable.getValue());
            }

            if (jFileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
                return;

            variable.setValue(jFileChooser.getSelectedFiles());
            File[] files = jFileChooser.getSelectedFiles();
            setButtonText(files.length + " file" + (files.length > 1 ? "s" : "") + " (click to change)");
        };

        FileDropListener fileDropListener = getFileDropListener(fileMode);
        new FileDrop(jButton, BorderFactory.createLineBorder(Color.blue.brighter(), 2), fileDropListener);

        return jButton;
    }

    private FileDropListener getFileDropListener(final FileMode fileMode) {
        return new FileDropListener() {

            @Override
            public void filesDropped(File[] files) {
                if (isActionListenerActive()) {
                    File[] filteredFiles;
                    switch (fileMode) {
                        case FILES:
                            filteredFiles = getFiles(files);

                            if (filteredFiles.length > 0) {
                                variable.setValue(filteredFiles);
                                setButtonText(filteredFiles.length + " file" + (files.length > 1 ? "s" : "")
                                        + " (click to change)");
                            }
                            break;

                        case FOLDERS:
                            filteredFiles = getFolders(files);
                            if (filteredFiles.length > 0) {
                                variable.setValue(filteredFiles);
                                setButtonText(filteredFiles.length + " folder" + (files.length > 1 ? "s" : "")
                                        + " (click to change)");
                            }
                            break;

                        default:
                            variable.setValue(files);
                            setButtonText(
                                    files.length + " file" + (files.length > 1 ? "s" : "") + " (click to change)");
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

            private File[] getFiles(File[] files) {
                List<File> fileList = new ArrayList<>();
                for (File f : files) {
                    if (f.isFile())
                        fileList.add(f);
                }

                return fileList.toArray(new File[fileList.size()]);
            }

            private File[] getFolders(File[] files) {
                List<File> folderList = new ArrayList<>();
                for (File f : files) {
                    if (f.isDirectory())
                        folderList.add(f);
                }

                return folderList.toArray(new File[folderList.size()]);
            }
        };
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
        File[] newValue = variable.getValue();

        StringBuilder list = new StringBuilder("<html><pre><font size=3>");

        if (newValue != null) {
            for (File f : newValue) {
                list.append(f.getAbsolutePath());
                if (f.isDirectory())
                    list.append("/");
                list.append("<br>");
            }
        }

        list.append("</font></pre></html>");

        getEditorComponent().setToolTipText(list.toString());
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
