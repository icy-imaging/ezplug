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

import javax.annotation.Nonnull;

public class VarDouble extends VarNumber<Double> {

    /**
     * @param name         The name of this variable
     * @param defaultValue The value assigned by default for this variable
     */
    public VarDouble(final String name, final double defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * @param name            The name of this variable
     * @param defaultValue    The value assigned by default for this variable
     * @param defaultListener A listener to add to this variable immediately after creation
     */
    public VarDouble(final String name, final double defaultValue, final VarListener<Double> defaultListener) {
        super(name, Double.TYPE, defaultValue, defaultListener);
    }

    @Override
    public Double parse(final String s) {
        return Double.parseDouble(s);
    }

    @Override
    public int compareTo(@Nonnull final Double d) {
        return getValue().compareTo(d);
    }

    @Override
    public Double getValue() {
        return getValue(false);
    }

    @Override
    public Double getValue(final boolean forbidNull) throws VarException {
        final Number number = super.getValue(forbidNull);

        return number == null ? null : number.doubleValue();
    }
}
