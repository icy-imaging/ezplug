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

import org.bioimageanalysis.icy.model.swimmingPool.SwimmingObject;

public class SwimmingObjectTypeModel<T> implements VarEditorModel<SwimmingObject> {
    @Override
    public boolean isValid(final SwimmingObject value) {
        try {
            // if the value casts to T, the swimming object is valid
            @SuppressWarnings({"unchecked", "unused"}) final T cast = (T) value.getObject();
            return true;
        }
        catch (final Exception ccE) {
            return false;
        }
    }

    @Override
    public SwimmingObject getDefaultValue() {
        return null;
    }
}