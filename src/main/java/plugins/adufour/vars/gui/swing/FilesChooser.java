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

import org.bioimageanalysis.icy.system.FileDrop;
import org.bioimageanalysis.icy.system.thread.ThreadUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import plugins.adufour.vars.gui.FileMode;
import plugins.adufour.vars.gui.model.FileTypeListModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.lang.Var;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * File chooser component for the Vars library. This component is tailored to select a list of files
 * via a {@link JFileChooser} component
 *
 * @author Alexandre Dufour
 */
public class FilesChooser extends SwingVarEditor<File[]> {
    private ActionListener actionListener;

    public FilesChooser(final Var<File[]> variable) {
        super(variable);
    }

    @Override
    public JComponent createEditorComponent() {
        final JButton jButton = new JButton();

        String path = null;
        FileMode fileMode = FileMode.ALL;
        boolean allowHidden = false;

        final VarEditorModel<File[]> model = variable.getDefaultEditorModel();

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
            final File[] files = jFileChooser.getSelectedFiles();
            setButtonText(files.length + " file" + (files.length > 1 ? "s" : "") + " (click to change)");
        };

        final FileDrop.FileDropListener fileDropListener = getFileDropListener(fileMode);
        new FileDrop(jButton, BorderFactory.createLineBorder(Color.blue.brighter(), 2), fileDropListener);

        return jButton;
    }

    @Contract(value = "_ -> new", pure = true)
    private FileDrop.@NotNull FileDropListener getFileDropListener(final FileMode fileMode) {
        return new FileDrop.FileDropListener() {

            @Override
            public void filesDropped(final File[] files) {
                if (isActionListenerActive()) {
                    final File[] filteredFiles;
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
                for (final ActionListener l : getEditorComponent().getActionListeners()) {
                    if (l == actionListener)
                        return true;
                }
                return false;
            }

            private File @NotNull [] getFiles(final File @NotNull [] files) {
                final List<File> fileList = new ArrayList<>();
                for (final File f : files) {
                    if (f.isFile())
                        fileList.add(f);
                }

                return fileList.toArray(new File[0]);
            }

            private File @NotNull [] getFolders(final File @NotNull [] files) {
                final List<File> folderList = new ArrayList<>();
                for (final File f : files) {
                    if (f.isDirectory())
                        folderList.add(f);
                }

                return folderList.toArray(new File[0]);
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
        final File[] newValue = variable.getValue();

        final StringBuilder list = new StringBuilder("<html><pre><font size=3>");

        if (newValue != null) {
            for (final File f : newValue) {
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
