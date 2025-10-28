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

import plugins.adufour.vars.gui.model.ValueSelectionModel;
import plugins.adufour.vars.util.VarListener;

public class VarEnum<T extends Enum<T>> extends Var<T> {
    /**
     * Creates a new {@link Var} variable with the specified name and default value (CANNOT be null).
     *
     * @param name         The name of this variable
     * @param defaultValue The <b>non-null</b> value assigned by default for this variable.
     * @throws NullPointerException If defaultValue is null
     */
    public VarEnum(final String name, final T defaultValue) throws NullPointerException {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new {@link Var} variable with the specified name and default value (CANNOT be null).
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable
     * @param defaultValue    The <b>non-null</b> value assigned by default for this variable.
     * @param defaultListener A listener to add to this variable immediately after creation
     * @throws NullPointerException If defaultValue is null
     */
    public VarEnum(final String name, final T defaultValue, final VarListener<T> defaultListener) throws NullPointerException {
        super(name, defaultValue.getDeclaringClass(), defaultValue, defaultListener);
        setDefaultEditorModel(new ValueSelectionModel<>(defaultValue.getDeclaringClass().getEnumConstants(), defaultValue, false));
    }

    @Override
    public String getValueAsString() {
        return getValue().name();
    }

    /**
     * Parses the given string into an Enumeration value
     */
    @Override
    public T parse(final String s) {
        return Enum.valueOf(getType(), s);
    }
}
