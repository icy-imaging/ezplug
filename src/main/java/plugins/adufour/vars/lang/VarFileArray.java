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

package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.FilesChooser;
import plugins.adufour.vars.util.VarListener;

import java.io.File;

/**
 * Specialized implementation of Var to manipulate file arrays
 *
 * @author Alexandre Dufour
 */
public class VarFileArray extends VarGenericArray<File[]> {
    /**
     * Creates a new file array {@link Var} variable with the specified name and default value
     *
     * @param name         the name of this variable
     * @param defaultValue the value assigned by default for this variable.
     *                     If null, a new array of size zero of the given type is assigned as default value.
     */
    public VarFileArray(final String name, final File[] defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new file array {@link Var} variable with the specified name and default value.
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            the name of this variable
     * @param defaultValue    the value assigned by default for this variable.
     *                        If null, a new array of size zero of the given type is assigned as default value.
     * @param defaultListener A listener to add to this variable immediately after creation
     */
    public VarFileArray(final String name, final File[] defaultValue, final VarListener<File[]> defaultListener) {
        super(name, File[].class, defaultValue, defaultListener);
    }

    @Override
    public String getSeparator(final int dimension) {
        return File.pathSeparator;
    }

    @Override
    public VarEditor<File[]> createVarEditor() {
        return new FilesChooser(this);
    }

    @Override
    public Object parseComponent(final String s) {
        return new java.io.File(s);
    }
}
