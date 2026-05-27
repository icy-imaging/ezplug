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

import org.jetbrains.annotations.NotNull;

public class TypeUtil {
    /**
     * @param primitiveType Primitive type to box into a class.
     * @return The Boxed type corresponding to the specified primitive type (or the original type if
     * not a primitive)
     */
    public static Class<?> toBoxedType(final @NotNull Class<?> primitiveType) {
        if (!primitiveType.isPrimitive())
            return primitiveType;

        if (primitiveType == boolean.class)
            return Boolean.class;
        if (primitiveType == void.class)
            return Void.class;
        if (primitiveType == char.class)
            return Character.class;
        if (primitiveType == byte.class)
            return Byte.class;
        if (primitiveType == short.class)
            return Short.class;
        if (primitiveType == int.class)
            return Integer.class;
        if (primitiveType == long.class)
            return Long.class;
        if (primitiveType == float.class)
            return Float.class;
        if (primitiveType == double.class)
            return Double.class;

        throw new IllegalArgumentException(primitiveType.getName());
    }

    /**
     * This method is intended to help non-programming users in differentiating and understanding
     * the various Java types by giving them more explicit names
     *
     * @param type Class of the type to convert,
     * @return A human-friendly name designating the specified type.
     */
    public static String toFriendlyString(final Class<?> type) {
        if (type == null)
            return "no type";

        if (type.isPrimitive())
            return toFriendlyString(toBoxedType(type));

        if (type.isArray())
            return "List of " + toFriendlyString(type.getComponentType()).toLowerCase() + "s";

        if (type == Double.class)
            return "Decimal";

        if (type == String.class)
            return "Text";

        return type.getSimpleName();
    }
}
