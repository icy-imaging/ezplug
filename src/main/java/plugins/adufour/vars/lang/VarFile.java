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

package plugins.adufour.vars.lang;

import java.io.File;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.FileChooser;
import plugins.adufour.vars.util.VarListener;

public class VarFile extends Var<File> {
    /**
     * Creates a new file {@link Var} variable with the specified name and default value (can be null).
     *
     * @param name         the name of this variable
     * @param defaultValue the value assigned by default for this variable
     */
    public VarFile(final String name, final File defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new file {@link Var} variable with the specified name and default value (can be null).
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            the name of this variable
     * @param defaultValue    the value assigned by default for this variable
     * @param defaultListener A listener to add to this variable immediately after creation
     */
    public VarFile(final String name, final File defaultValue, final VarListener<File> defaultListener) {
        super(name, File.class, defaultValue, defaultListener);
    }

    @Override
    public File parse(final String s) {
        return new File(s);
    }

    @Override
    public String toString() {
        return getValue() == null ? null : getValue().getAbsolutePath();
    }

    @Override
    public VarEditor<File> createVarEditor() {
        return new FileChooser(this);
    }
}
