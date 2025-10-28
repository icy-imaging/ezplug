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
import plugins.adufour.vars.gui.model.FileTypeModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.lang.Var;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

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
    public FileChooser(final Var<File> variable) {
        super(variable);
    }

    @Override
    public JComponent createEditorComponent() {
        final JButton jButton = new JButton();

        String path = null;
        FileMode fileMode = FileMode.ALL;
        boolean allowHidden = false;
        final FileFilter filter;

        final VarEditorModel<File> model = variable.getDefaultEditorModel();

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
                public boolean accept(final File f) {
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

        final FileDrop.FileDropListener fileDropListener = getFileDropListener(fileMode);
        new FileDrop(jButton, BorderFactory.createLineBorder(Color.blue.brighter(), 2), fileDropListener);

        // jButton.addActionListener(actionListener);

        return jButton;
    }

    @Contract(value = "_ -> new", pure = true)
    private FileDrop.@NotNull FileDropListener getFileDropListener(final FileMode fileMode) {
        return new FileDrop.FileDropListener() {

            @Override
            public void filesDropped(final File[] files) {
                if (isActionListenerActive()) {
                    final File singleFile = (files != null && files.length >= 1) ? files[0] : null;

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
                for (final ActionListener l : getEditorComponent().getActionListeners()) {
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
        final File newValue = variable.getValue();

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
