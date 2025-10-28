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

import plugins.adufour.vars.util.VarListener;

/**
 * Class defining a variable with a native array of type <code>float[]</code>.
 *
 * @author Alexandre Dufour
 */
public class VarFloatArrayNative extends VarGenericArray<float[]> {
    /**
     * Creates a new float array {@link Var} variable with the specified name and default value.
     *
     * @param name         The name of this variable
     * @param defaultValue The value assigned by default for this variable.
     *                     If null, a new float array of size zero is assigned as default value.
     */
    public VarFloatArrayNative(final String name, final float[] defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new float array {@link Var} variable with the specified name and default value.
     * Optionally, a listener can be added to track on the variable.
     *
     * @param name            The name of this variable
     * @param defaultValue    The value assigned by default for this variable.
     *                        If null, a new float array of size zero is assigned as default value.
     * @param defaultListener A listener to add to this variable immediately after creation
     */
    public VarFloatArrayNative(final String name, final float[] defaultValue, final VarListener<float[]> defaultListener) {
        super(name, float[].class, defaultValue, defaultListener);
    }

    @Override
    public Object parseComponent(final String s) {
        return Float.parseFloat(s);
    }

    @Override
    public float[] getValue(final boolean forbidNull) {
        // handle the case where the reference is not an array

        final Var<?> reference = getReference();

        if (reference == null)
            return super.getValue(forbidNull);

        final Object value = reference.getValue();

        if (value == null)
            return super.getValue(forbidNull);

        if (value instanceof Number)
            return new float[]{((Number) value).floatValue()};

        return (float[]) value;
    }
}
