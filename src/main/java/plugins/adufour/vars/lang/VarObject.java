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

import org.jetbrains.annotations.NotNull;
import plugins.adufour.vars.util.VarListener;

public class VarObject extends Var<Object> {

    /**
     * Creates a new object {@link Var} variable with the specified name and default value (can be null)
     *
     * @param name         The name of this variable.
     * @param defaultValue The value assigned by default for this variable.
     */
    public VarObject(final String name, final Object defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new object {@link Var} variable with the specified name and default value (can be null).
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable.
     * @param defaultValue    The value assigned by default for this variable.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarObject(final String name, final Object defaultValue, final VarListener<Object> defaultListener) {
        super(name, Object.class, defaultValue, defaultListener);
    }

    @Override
    public boolean isAssignableFrom(final @NotNull Var<?> source) {
        // an Object type variable may always point to any DEFINED type

        return getType() != null;
    }
}
