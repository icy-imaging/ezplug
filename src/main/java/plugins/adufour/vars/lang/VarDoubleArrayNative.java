/*
 * Copyright (c) 2010-2026. Institut Pasteur.
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
 * Class defining a variable with a native array of type <code>double[]</code>.
 *
 * @author Alexandre Dufour
 */
public class VarDoubleArrayNative extends VarGenericArray<double[]> {
    /**
     * @param name         the name of this variable
     * @param defaultValue the value assigned by default for this variable. If null, a new double array of size zero is assigned as default value.
     */
    public VarDoubleArrayNative(final String name, final double[] defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * @param name            the name of this variable
     * @param defaultValue    the value assigned by default for this variable. If null, a new double array of size zero is assigned as default value.
     * @param defaultListener A listener to add to this variable immediately after creation
     */
    public VarDoubleArrayNative(final String name, final double[] defaultValue, final VarListener<double[]> defaultListener) {
        super(name, double[].class, defaultValue, defaultListener);
    }

    @Override
    public Object parseComponent(final String s) {
        return Double.parseDouble(s);
    }

    @Override
    public double[] getValue(final boolean forbidNull) {
        // handle the case where the reference is not an array

        final Var<?> reference = getReference();

        if (reference == null)
            return super.getValue(forbidNull);

        final Object value = reference.getValue();

        if (value == null)
            return super.getValue(forbidNull);

        if (value instanceof Number)
            return new double[]{((Number) value).doubleValue()};

        return (double[]) value;
    }
}
