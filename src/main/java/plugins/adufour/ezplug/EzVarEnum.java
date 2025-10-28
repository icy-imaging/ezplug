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

package plugins.adufour.ezplug;

import plugins.adufour.vars.lang.VarEnum;

import java.util.Arrays;

/**
 * Class defining an enumeration type variable. <br>
 * The graphical component is a combo box containing some (or all) values of the enumeration
 *
 * @param <E> The enumeration type
 * @author Alexandre Dufour
 */
public class EzVarEnum<E extends Enum<E>> extends EzVar<E> {
    /**
     * Constructs a new input VarEnum variable
     *
     * @param varName the variable name
     * @param values  the values to choose from. The full list of enumeration values can be obtained with the E.values()
     *                method, where E is the enumeration type
     */
    public EzVarEnum(final String varName, final E[] values) {
        this(varName, values, 0);
    }

    /**
     * Constructs a new input VarEnum variable
     *
     * @param varName      the variable name
     * @param values       the values to choose from. The full list of enumeration values can be obtained with the E.values()
     *                     method, where E is the enumeration type
     * @param defaultValue the enumeration value to select by default
     */
    public EzVarEnum(final String varName, final E[] values, final E defaultValue) {
        this(varName, values, Arrays.asList(values).indexOf(defaultValue));
    }

    /**
     * Constructs a new input VarEnum variable
     *
     * @param varName           the variable name
     * @param values            the values to choose from. The full list of enumeration values can be obtained with the E.values()
     *                          method, where E is the enumeration type
     * @param defaultValueIndex the zero-based index of the enumeration value to select by default
     */
    public EzVarEnum(final String varName, final E[] values, final int defaultValueIndex) {
        super(new VarEnum<>(varName, values[defaultValueIndex]), values, defaultValueIndex, false);
    }
}
