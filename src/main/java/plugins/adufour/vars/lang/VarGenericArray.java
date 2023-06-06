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

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.ArrayType;
import plugins.adufour.vars.util.VarListener;

import java.lang.reflect.Array;

/**
 * Class defining a variable holding an array of arbitrary type (including Java primitives).
 * This class should be preferred to {@link VarArray} when dealing
 * with large arrays of primitives in order to optimize performances.
 *
 * @param <A> the underlying array type (*not* the inner component type, e.g., <code>int[]</code>
 *            and not <code>int</code>)
 * @author Alexandre Dufour
 */
public class VarGenericArray<A> extends Var<A> implements ArrayType {
    /**
     * Creates a new array {@link Var} variable with the specified name, and inner value type
     * and default value.
     *
     * @param name         The name of this variable.
     * @param type         The type of this variable array.
     * @param defaultValue The value assigned by default for this variable.
     *                     If null, a new array of size zero of the given type is assigned as default value.
     */
    public VarGenericArray(final String name, final Class<A> type, final A defaultValue) {
        this(name, type, defaultValue, null);
    }

    /**
     * Creates a new array {@link Var} variable with the specified name, and inner value type and default value.
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable.
     * @param type            The type of this variable array.
     * @param defaultValue    The value assigned by default for this variable.
     *                        If null, a new array of size zero of the given type is assigned as default value.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    @SuppressWarnings("unchecked")
    public VarGenericArray(final String name, final Class<A> type, final A defaultValue, final VarListener<A> defaultListener) {
        super(name, type, (defaultValue != null ? defaultValue : (A) Array.newInstance(type.getComponentType(), 0)), defaultListener);
    }

    @Override
    public VarEditor<A> createVarEditor() {
        return VarEditorFactory.getDefaultFactory().createTextField(this);
    }

    @Override
    public int getDimensions() {
        return 1;
    }

    @Override
    public Class<?> getInnerType() {
        return getType() == null ? null : getType().getComponentType();
    }

    @Override
    public String getSeparator(final int dimension) {
        if (dimension == 0)
            return " ";

        throw new ArrayIndexOutOfBoundsException(dimension);
    }

    @SuppressWarnings("unchecked")
    @Override
    public A getValue() {
        final A a = super.getValue();
        if (a == null)
            return (A) Array.newInstance(getInnerType(), 0);

        if (type == null)
            return null;

        // if the inner type is not array while value is an array --> do not encapsulate
        if (!getInnerType().isArray() && a.getClass().isArray())
            return a;

        // convert single elements to singled-value arrays
        if (getInnerType().isAssignableFrom(a.getClass())) {
            final Object array = Array.newInstance(a.getClass(), 1);
            Array.set(array, 0, a);
            return (A) array;
        }

        return a;
    }

    @Override
    public boolean isAssignableFrom(final Var<?> source) {
        final Class<?> componentType = getInnerType();

        if (componentType == null)
            return false;

        if (source instanceof VarGenericArray) {
            // check if the inner type is compatible
            final VarGenericArray<?> varNativeArray = (VarGenericArray<?>) source;
            final Class<?> sourceComponentType = varNativeArray.getInnerType();
            if (sourceComponentType == null)
                return false;
            return componentType.isAssignableFrom(sourceComponentType);
        }
        else if (componentType.isAssignableFrom(source.getType())) {
            return true;
        }
        else
            return super.isAssignableFrom(source);
    }

    @SuppressWarnings("unchecked")
    @Override
    public A parse(String input) {
        final Class<?> componentType = getInnerType();

        input = input.trim();

        if (input.isEmpty())
            return (A) Array.newInstance(componentType, 0);

        final String[] inputs = input.trim().split(getSeparator(0));
        final A array = (A) Array.newInstance(componentType, inputs.length);

        for (int i = 0; i < inputs.length; i++)
            Array.set(array, i, parseComponent(inputs[i]));

        return array;
    }

    /**
     * Parse a string representing an element of the underlying array.
     * Note that this method throws an <code>UnsupportedOperationException</code> by default,
     * therefore the implementation must be provided by overriding classes
     *
     * @param s the string to parse
     * @return a primitive type representing the given string
     */
    @Override
    public Object parseComponent(final String s) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Parsing not implemented for type " + getClass().getSimpleName());
    }

    /**
     * @return the size of this array, or -1 if the array is <code>null</code>
     */
    public int size() {
        return size(0);
    }

    @Override
    public int size(final int dimension) throws ArrayIndexOutOfBoundsException {
        if (dimension > 0)
            throw new ArrayIndexOutOfBoundsException(dimension);

        final Object array = getValue();

        return array == null ? -1 : Array.getLength(array);
    }
}
