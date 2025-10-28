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

import plugins.adufour.vars.lang.VarIntegerArrayNative;

/**
 * Integer arrays
 *
 * @author Alexandre Dufour
 */
public class EzVarIntegerArrayNative extends EzVar<int[]> {
    /**
     * Creates a new integer variable with a given array of possible values
     *
     * @param varName        the name of the variable (as it will appear on the interface)
     * @param defaultValues  the list of possible values the user may choose from
     * @param allowUserInput set to true to allow the user to input its own value manually, false otherwise
     * @throws NullPointerException if the defaultValues parameter is null
     */
    public EzVarIntegerArrayNative(final String varName, final int[][] defaultValues, final boolean allowUserInput) throws NullPointerException {
        this(varName, defaultValues, 0, allowUserInput);
    }

    /**
     * Creates a new integer variable with a given array of possible values
     *
     * @param varName           the name of the variable (as it will appear on the interface)
     * @param defaultValues     the list of possible values the user may choose from
     * @param defaultValueIndex the index of the default selected value
     * @param allowUserInput    set to true to allow the user to input its own value manually, false otherwise
     * @throws NullPointerException if the defaultValues parameter is null
     */
    public EzVarIntegerArrayNative(final String varName, final int[][] defaultValues, final int defaultValueIndex, final boolean allowUserInput) throws NullPointerException {
        super(new VarIntegerArrayNative(varName, defaultValues == null ? null : defaultValues[defaultValueIndex]), defaultValues, defaultValueIndex, allowUserInput);
    }
}