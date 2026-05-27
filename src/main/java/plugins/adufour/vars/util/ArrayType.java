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

package plugins.adufour.vars.util;

public interface ArrayType {
    /**
     * @return the number of dimensions in this array
     */
    int getDimensions();

    /**
     * @return the type of each element inside this array
     */
    Class<?> getInnerType();

    /**
     * @param dimension the dimension for which the separator is wanted
     * @return The character used to separate array values when displaying the value as a String
     */
    String getSeparator(int dimension) throws ArrayIndexOutOfBoundsException;

    /**
     * Parse a string representing an element of the underlying array. Note that this method throws
     * an <code>UnsupportedOperationException</code> by default, therefore the implementation must
     * be provided by overriding classes
     *
     * @param s the string to parse
     * @return a primitive type representing the given string
     */
    Object parseComponent(String s) throws UnsupportedOperationException;

    /**
     * @param dimension the dimension along which the size is wanted
     * @return the size of the array in the specified dimension, or -1 if the array is
     * <code>null</code>
     * @throws ArrayIndexOutOfBoundsException if the dimension does not exist
     */
    int size(int dimension) throws ArrayIndexOutOfBoundsException;
}
