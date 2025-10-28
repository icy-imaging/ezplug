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

import javax.annotation.Nonnull;

public class VarLong extends VarNumber<Long> {

    /**
     * Creates a new long integer {@link Var} variable with the specified name and default value.
     *
     * @param name         The name of this variable.
     * @param defaultValue The value assigned by default for this variable.
     */
    public VarLong(final String name, final long defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new long integer {@link Var} variable with the specified name and default value.
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable.
     * @param defaultValue    The value assigned by default for this variable.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarLong(final String name, final long defaultValue, final VarListener<Long> defaultListener) {
        super(name, Long.TYPE, defaultValue, defaultListener);
    }

    @Override
    public Long parse(final String s) {
        return Long.valueOf(s);
    }

    @Override
    public int compareTo(@Nonnull final Long value) {
        return getValue().compareTo(value);
    }

    @Override
    public Long getValue() {
        return getValue(false);
    }

    /**
     * @return An integer representing the variable value.
     * <b>Warning:</b> If the current variable references a variable of different (wider) type,
     * truncation will occur.
     */
    @Override
    public Long getValue(final boolean forbidNull) {
        return super.getValue(forbidNull);
    }

    @Override
    protected boolean areValuesEqual(final @NotNull Long a, final Object b) {
        if (b instanceof Number) {
            return super.areValuesEqual(a, ((Number) b).longValue());
        }
        return false;
    }
}
