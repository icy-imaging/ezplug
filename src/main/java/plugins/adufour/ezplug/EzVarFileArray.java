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

package plugins.adufour.ezplug;

import plugins.adufour.vars.gui.FileMode;
import plugins.adufour.vars.gui.model.FileTypeListModel;
import plugins.adufour.vars.lang.VarFileArray;

import java.io.File;

/**
 * Class defining a variable of type String, embarking a button triggering a file dialog as a graphical component
 *
 * @author Alexandre Dufour
 */
public class EzVarFileArray extends EzVar<File[]> {
    /**
     * Constructs a new input variable with a given name and default file dialog path
     *
     * @param varName the name of the variable (as it will appear in the interface)
     * @param path    the default path to show in the file dialog
     */
    public EzVarFileArray(final String varName, final String path) {
        super(new VarFileArray(varName, null), new FileTypeListModel(path, FileMode.FILES, null, false));
    }
}
