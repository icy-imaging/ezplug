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

package plugins.adufour.ezplug;

import org.bioimageanalysis.icy.common.string.StringUtil;
import plugins.adufour.vars.gui.FileMode;
import plugins.adufour.vars.gui.model.FileTypeModel;
import plugins.adufour.vars.gui.swing.FileChooser;
import plugins.adufour.vars.lang.VarFile;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 * Class defining a variable of type String, embarking a button triggering a file dialog as
 * a graphical component
 *
 * @author Alexandre Dufour
 */
public class EzVarFile extends EzVar<File> {
    /**
     * Constructs a new input variable with a given name and default file dialog path
     *
     * @param varName the name of the variable (as it will appear in the interface)
     * @param path    the default path to show in the file dialog
     */
    public EzVarFile(final String varName, final String path) {
        this(varName, path, (FileFilter) null);
    }

    /**
     * Constructs a new input variable with given name, file path and filter.
     *
     * @param varName  The name of the variable (as it will appear in the interface).
     * @param path     The default path to show in the file dialog.
     * @param wildcard A wild-card filter (e.g. "*.tif"). Warning: this parameter is *not* a regular expression. To use regular expressions, see
     *                 {@link #EzVarFile(String, String, Pattern)}.
     */
    public EzVarFile(final String varName, final String path, final String wildcard) {
        this(varName, null, Pattern.compile(StringUtil.wildcardToRegex(wildcard)));
    }

    /**
     * Constructs a new input variable with given name, file path and filter.
     *
     * @param varName The name of the variable (as it will appear in the interface).
     * @param path    The default path to show in the file dialog.
     * @param pattern A regular expression pattern to filter the file list.
     */
    public EzVarFile(final String varName, final String path, final Pattern pattern) {
        this(varName, null, (pathname) -> pathname.isDirectory() || Pattern.matches(pattern.pattern(), pathname.getPath()));
    }

    /**
     * Constructs a new input variable with given name, file path and filter.
     *
     * @param varName The name of the variable (as it will appear in the interface).
     * @param path    The default path to show in the file dialog.
     * @param filter  The file filter.
     */
    public EzVarFile(final String varName, final String path, final FileFilter filter) {
        super(new VarFile(varName, null), new FileTypeModel(path, FileMode.FILES, filter, false));
    }

    /**
     * Replaces the button text by the given string.
     *
     * @param text The button text.
     */
    public void setButtonText(final String text) {
        ((FileChooser) getVarEditor()).setButtonText(text);
    }
}
