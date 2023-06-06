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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Convenience class defining a generic array of elements based on the specified inner type.
 * <p>
 * The type of the underlying array cannot be changed.
 * To create arrays of changeable types, use {@link VarMutableArray} instead.
 * <p>
 * NOTE: this class provides convenience methods to add elements
 * (similarly to the {@link ArrayList} class),
 * however, performance on large arrays is not optimal.
 * For performance-critical applications,
 * use the {@link VarArray} instead
 *
 * @param <T> the inner type of the array
 * @author Alexandre Dufour
 */
public class VarArray<T> extends VarGenericArray<T[]> implements Iterable<T> {
    /**
     * Creates a new array {@link Var} variable with the specified name, and inner value type and default value.
     *
     * @param name         The name of this variable.
     * @param type         The type of this variable array (including the <code>[]</code>).
     * @param defaultValue The value assigned by default for this variable.
     *                     If null, a new array of size zero of the given type is assigned as default value.
     */
    public VarArray(final String name, final Class<T[]> type, final T[] defaultValue) {
        super(name, type, defaultValue);
    }

    /**
     * Creates a new array {@link Var} variable with the specified name, and inner value type and default value.
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable.
     * @param type            The type of this variable array (including the <code>[]</code>).
     * @param defaultValue    The value assigned by default for this variable.
     *                        If null, a new array of size zero of the given type is assigned as default value.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarArray(final String name, final Class<T[]> type, final T[] defaultValue, final VarListener<T[]> defaultListener) {
        super(name, type, defaultValue, defaultListener);
    }

    /**
     * Inserts the specified elements at the end of this array.
     * This method acts similarly to {@link ArrayList#add(Object)}:
     * an old array is replaced by a new array where the content of
     * the old array is copied and the specified element is added last
     *
     * @param elements the elements to add
     */
    @SafeVarargs
    public final void add(final T... elements) {
        final T[] oldArray = getValue();
        final ArrayList<T> newArray = new ArrayList<>(size() + elements.length);

        newArray.addAll(Arrays.asList(oldArray));
        newArray.addAll(Arrays.asList(elements));

        setValue(newArray.toArray(oldArray));
    }

    @Override
    public Iterator<T> iterator() {
        return Arrays.asList(getValue()).iterator();
    }
}
