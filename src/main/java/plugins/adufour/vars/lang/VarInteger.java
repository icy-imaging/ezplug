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

import org.jetbrains.annotations.NotNull;
import plugins.adufour.vars.util.VarListener;

public class VarInteger extends VarNumber<Integer> {

    /**
     * Creates a new integer {@link Var} variable with the specified name and default value.
     *
     * @param name         The name of this variable
     * @param defaultValue The value assigned by default for this variable
     */
    public VarInteger(final String name, final int defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new integer {@link Var} variable with the specified name and default value.
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable
     * @param defaultValue    The value assigned by default for this variable
     * @param defaultListener A listener to add to this variable immediately after creation
     */
    public VarInteger(final String name, final int defaultValue, final VarListener<Integer> defaultListener) {
        super(name, Integer.TYPE, defaultValue, defaultListener);
    }

    @Override
    public Integer parse(final String s) {
        return Integer.parseInt(s);
    }

    @Override
    public int compareTo(@NotNull final Integer integer) {
        return getValue().compareTo(integer);
    }

    @Override
    public Integer getValue() {
        return getValue(false);
    }

    /**
     * Returns an integer representing the variable value.<br>
     * NOTE: if the current variable references a variable of different (wider) type, truncation
     * will occur
     */
    @Override
    public Integer getValue(final boolean forbidNull) {
        final Number number = super.getValue(forbidNull);

        return number == null ? null : number.intValue();
    }
}
