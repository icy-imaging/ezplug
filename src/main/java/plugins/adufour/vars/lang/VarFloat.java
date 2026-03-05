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

public class VarFloat extends VarNumber<Float> {

    /**
     * Creates a new float {@link Var} variable with the specified name and default value.
     *
     * @param name         The name of this variable
     * @param defaultValue The value assigned by default for this variable
     */
    public VarFloat(final String name, final float defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new float {@link Var} variable with the specified name and default value.
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable
     * @param defaultValue    The value assigned by default for this variable
     * @param defaultListener A listener to add to this variable immediately after creation
     */
    public VarFloat(final String name, final float defaultValue, final VarListener<Float> defaultListener) {
        super(name, Float.TYPE, defaultValue, defaultListener);
    }

    @Override
    public Float parse(final String s) {
        return Float.parseFloat(s);
    }

    @Override
    public int compareTo(@NotNull final Float f) {
        return getValue().compareTo(f);
    }

    @Override
    public Float getValue() {
        return getValue(false);
    }

    /**
     * @return The float representing the variable value.
     * <p>
     * <b>Warning:</b> if the current variable references a variable of different (wider) type, truncation
     * will occur.
     */
    @Override
    public Float getValue(final boolean forbidNull) {
        final Number number = super.getValue(forbidNull);

        return number == null ? null : number.floatValue();
    }
}
