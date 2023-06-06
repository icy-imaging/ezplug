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

import plugins.adufour.vars.util.VarListener;

/**
 * Class defining a variable with a native array of type <code>int[]</code>.
 *
 * @author Alexandre Dufour
 */
public class VarIntegerArrayNative extends VarGenericArray<int[]> {
    /**
     * Creates a new integer array {@link Var} variable with the specified name and default value.
     *
     * @param name         The name of this variable.
     * @param defaultValue The value assigned by default for this variable.
     *                     If null, a new integer array of size zero of the given type is assigned as default value.
     */
    public VarIntegerArrayNative(final String name, final int[] defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new integer array {@link Var} variable with the specified name and default value.
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable.
     * @param defaultValue    The value assigned by default for this variable.
     *                        If null, a new integer array of size zero of the given type is assigned as default value.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarIntegerArrayNative(final String name, final int[] defaultValue, final VarListener<int[]> defaultListener) {
        super(name, int[].class, defaultValue, defaultListener);
    }

    @Override
    public Object parseComponent(final String s) {
        return Integer.parseInt(s);
    }

    @Override
    public int[] getValue(final boolean forbidNull) {
        // handle the case where the reference is not an array

        final Var<?> reference = getReference();

        if (reference == null)
            return super.getValue(forbidNull);

        final Object value = reference.getValue();

        if (value == null)
            return super.getValue(forbidNull);

        if (value instanceof Number)
            return new int[]{((Number) value).intValue()};

        return (int[]) value;
    }
}
