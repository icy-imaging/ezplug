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

package plugins.adufour.vars.gui.model;

import plugins.adufour.vars.gui.FileMode;

import java.io.File;
import java.io.FileFilter;

public class FileTypeModel implements VarEditorModel<File> {
    private final FileMode mode;

    private final FileFilter filter;

    private final String path;

    private final boolean allowHidden;

    public FileTypeModel(final String initialPath, final FileMode mode, final FileFilter filter, final boolean allowHidden) {
        this.path = initialPath;
        this.filter = filter;
        this.mode = mode;
        this.allowHidden = allowHidden;
    }

    @Override
    public boolean isValid(final File value) {
        if (value == null) return false;

        if (filter != null && !filter.accept(value)) return false;

        if (value.isFile() && mode != FileMode.FILES) return false;

        if (value.isDirectory() && mode != FileMode.FOLDERS) return false;

        if (value.isHidden() && !allowHidden) return false;

        return true;
    }

    @Override
    public File getDefaultValue() {
        return null;
    }

    public FileFilter getFilter() {
        return filter;
    }

    public FileMode getMode() {
        return mode;
    }

    public String getPath() {
        return path;
    }

    public boolean allowHidden() {
        return allowHidden;
    }
}
