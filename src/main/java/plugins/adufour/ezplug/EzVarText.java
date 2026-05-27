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

package plugins.adufour.ezplug;

import plugins.adufour.vars.lang.VarString;

/**
 * Class defining a variable of type String, embarking a textfield as a graphical component
 *
 * @author Alexandre Dufour
 */
public class EzVarText extends EzVar<String> {
    /**
     * Creates a new Text variable
     *
     * @param varName the name of the variable (as it will appear on the interface)
     */
    public EzVarText(final String varName) {
        this(varName, 1);
    }

    /**
     * Creates a new Text variable
     *
     * @param varName the name of the variable (as it will appear on the interface)
     * @param nbLines the number of lines to display on the interface
     */
    public EzVarText(final String varName, final int nbLines) {
        this(varName, "", nbLines);
    }

    /**
     * Creates a new Text variable
     *
     * @param varName     the name of the variable (as it will appear on the interface)
     * @param defaultText the default text to enter (the number of lines will be derived from this text)
     */
    public EzVarText(final String varName, final String defaultText) {
        this(varName, defaultText, defaultText.split("\n").length);
    }

    /**
     * Creates a new Text variable
     *
     * @param varName     the name of the variable (as it will appear on the interface)
     * @param defaultText the default text to enter
     * @param nbLines     the number of lines to display on the interface
     */
    public EzVarText(final String varName, final String defaultText, final int nbLines) {
        super(new VarString(varName, defaultText, nbLines), null);
    }

    /**
     * Creates a new Text input variable with a list of default values
     *
     * @param varName        the variable name
     * @param defaultValues  the list of default values the user may choose from
     * @param allowUserInput set to true if the user may input text manually instead of selecting a default
     *                       value, or false to forbid user input
     */
    public EzVarText(final String varName, final String[] defaultValues, final Boolean allowUserInput) {
        this(varName, defaultValues, 0, allowUserInput);
    }

    /**
     * Creates a new Text input variable with a list of default values and a default selection
     *
     * @param varName           the variable name
     * @param defaultValues     the list of default values the user may choose from
     * @param defaultValueIndex the index of the default selected value
     * @param allowUserInput    set to true if the user may input text manually instead of selecting a default
     *                          value, or false to forbid user input
     */
    public EzVarText(final String varName, final String[] defaultValues, final int defaultValueIndex, final Boolean allowUserInput) {
        super(new VarString(varName, null), defaultValues, defaultValueIndex, allowUserInput);
    }
}