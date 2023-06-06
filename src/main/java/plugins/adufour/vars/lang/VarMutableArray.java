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

import plugins.adufour.vars.util.VarException;
import plugins.adufour.vars.util.VarListener;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Iterator;

/**
 * Variable holding an array of mutable type
 *
 * @author Alexandre Dufour
 */
public class VarMutableArray extends VarMutable implements Iterable<Object> {

    /**
     * Creates a new mutable-type array {@link Var} variable with the specified name and initial type.
     * The default value is always set to null in order to be properly reset
     *
     * @param name        The name of the new variable.
     * @param initialType The initial array type of the new variable. The type can be changed using the {@link #setType(Class)} method. If null, the type is set
     *                    {@link Object}.
     * @throws IllegalArgumentException If the initial type is not an array type.
     */
    public VarMutableArray(final String name, final Class<?> initialType) throws IllegalArgumentException {
        this(name, initialType, null);
    }

    /**
     * Creates a new mutable-type array {@link Var} variable with the specified name and initial type.
     * The default value is always set to null in order to be properly reset.
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of the new variable.
     * @param initialType     The initial array type of the new variable.
     *                        The type can be changed using the {@link #setType(Class)} method.
     *                        If null, the type is set {@link Object}.
     * @param defaultListener A listener to add to this variable immediately after creation.
     * @throws IllegalArgumentException If the initial type is not an array type.
     */
    public VarMutableArray(final String name, final Class<?> initialType, final VarListener<?> defaultListener)
            throws IllegalArgumentException {
        super(name, initialType, defaultListener);
        if ((initialType != null) && !initialType.isArray())
            throw new IllegalArgumentException("The initial type " + initialType + " is not an array type");
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean isAssignableFrom(final Var source) {
        return super.isAssignableFrom(source);// && source.getType().isArray();
    }

    /**
     * @param <T>   The expected type of the returned element.
     * @param index The index to retrieve
     * @return The array element at the specified index.
     * @throws NullPointerException If the variable value is null.
     * @throws ClassCastException   If the inferred type is incompatible with the array element.
     */
    @SuppressWarnings("unchecked")
    public <T> T getElementAt(final int index) throws NullPointerException, ClassCastException {
        return (T) Array.get(getValue(), index);
    }

    /**
     * @return The size of this array, or -1 if the array is <code>null</code>.
     */
    public int size() {
        return getValue() == null ? -1 : Array.getLength(getValue());
    }

    /**
     * @throws IllegalArgumentException If newType is not an array type.
     */
    @Override
    public void setType(final Class<?> newType) throws IllegalArgumentException {
        if ((newType != null) && !newType.isArray())
            throw new IllegalArgumentException("The new type " + newType + " is not an array type");
        super.setType(newType);
    }

    @Override
    public Object getValue(final boolean forbidNull) throws VarException {
        final Var<?> reference = getReference();

        if (reference == null)
            return super.getValue(forbidNull);

        final Object value = reference.getValue();

        if (value == null)
            return super.getValue(forbidNull);

        if (value.getClass().isArray())
            return value;

        final Object array = Array.newInstance(value.getClass(), 1);
        Array.set(array, 0, value);
        return array;
    }

    @Override
    public Iterator<Object> iterator() {
        if (getValue() == null)
            return Collections.emptyIterator();

        return Collections.singletonList(getValue()).iterator();
    }
}
