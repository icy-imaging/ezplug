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

import plugins.adufour.vars.lang.Var;

/**
 * Model usable by graphical user interfaces to build custom components for {@link Var} objects.
 * This can be used, for instance, to restrict the bounds of valid values.
 * <br>
 * <br>
 * For more information and model templates, see the other classes of this package.
 *
 * @param <T> Type of the var editor model.
 * @author Alexandre Dufour
 * @see SwimmingObjectTypeModel
 * @see ValueSelectionModel
 * @see RangeModel
 * @see FileTypeModel
 * @see FileTypeListModel
 */
public interface VarEditorModel<T> {
    boolean isValid(T value);

    T getDefaultValue();
}
