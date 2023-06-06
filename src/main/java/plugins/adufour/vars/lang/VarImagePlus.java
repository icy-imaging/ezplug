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

import ij.ImagePlus;
import plugins.adufour.vars.util.VarListener;

public class VarImagePlus extends Var<ImagePlus> {
    /**
     * Creates a new ImagePlus {@link Var} variable with the specified name and default value (can be null)
     *
     * @param name         The name of this variable.
     * @param defaultValue The value assigned by default for this variable.
     */
    public VarImagePlus(final String name, final ImagePlus defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new ImagePlus {@link Var} variable with the specified name and default value (may be null).
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable.
     * @param defaultValue    The value assigned by default for this variable.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarImagePlus(final String name, final ImagePlus defaultValue, final VarListener<ImagePlus> defaultListener) {
        super(name, ImagePlus.class, defaultValue, defaultListener);
    }
}
