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

package plugins.adufour.ezplug;

import plugins.adufour.vars.lang.VarBoolean;

/**
 * Boolean variable for the ezPlug framework. Creates a JCheckBox component to select between true
 * and false
 *
 * @author Alexandre Dufour
 */
public class EzVarBoolean extends EzVar<Boolean> {
    /**
     * Constructs a new input variable holding a boolean value with a JCheckBox for graphical user
     * interface
     *
     * @param varName      the name of the variable
     * @param defaultValue the default state of the JCheckBox
     */
    public EzVarBoolean(final String varName, final boolean defaultValue) {
        super(new VarBoolean(varName, defaultValue), null);
    }
}
