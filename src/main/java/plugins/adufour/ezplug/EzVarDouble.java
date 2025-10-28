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

import plugins.adufour.vars.gui.model.DoubleRangeModel;
import plugins.adufour.vars.gui.model.RangeModel.RangeEditorType;
import plugins.adufour.vars.lang.VarDouble;

import java.util.HashMap;

/**
 * Specialized implementation of {@link plugins.adufour.ezplug.EzVarNumeric} for variables of type double.
 *
 * @author Alexandre Dufour
 */
public class EzVarDouble extends EzVarNumeric<Double> {
    /**
     * Creates a new double variable with a default minimum and maximum values, and a default step size of 0.01.
     *
     * @param varName The name of the variable (as it will appear on the interface).
     */
    public EzVarDouble(final String varName) {
        this(varName, 0, 0, Double.MAX_VALUE, 0.01);
    }

    /**
     * Creates a new double variable with specified minimum and maximum values.
     *
     * @param varName The name of the variable (as it will appear on the interface).
     * @param min     The minimum allowed value.
     * @param max     The maximum allowed value.
     * @param step    The step between consecutive values.
     */
    public EzVarDouble(final String varName, final double min, final double max, final double step) {
        this(varName, min, min, max, step);
    }

    /**
     * Creates a new double variable with specified default, minimum and maximum values.
     *
     * @param varName The name of the variable (as it will appear on the interface).
     * @param value   The default value.
     * @param min     The minimum allowed value.
     * @param max     The maximum allowed value.
     * @param step    The step between consecutive values.
     */
    public EzVarDouble(final String varName, final double value, final double min, final double max, final double step) {
        this(varName, value, min, max, step, RangeEditorType.SPINNER, null);
    }

    /**
     * Creates a new integer variable with specified default, minimum and maximum values.
     *
     * @param varName    The name of the variable (as it will appear on the interface).
     * @param value      The default value.
     * @param min        The minimum allowed value.
     * @param max        The maximum allowed value.
     * @param step       The step between consecutive values.
     * @param editorType The type of editor to use.
     * @param labels     Labels assigned for specific double values.
     */
    public EzVarDouble(final String varName, final double value, final double min, final double max, final double step, final RangeEditorType editorType, final HashMap<Double, String> labels) {
        super(new VarDouble(varName, value), new DoubleRangeModel(value, min, max, step, editorType, labels));
    }

    /**
     * Creates a new integer variable with a given array of possible values.
     *
     * @param varName        The name of the variable (as it will appear on the interface).
     * @param defaultValues  The list of possible values the user may choose from.
     * @param allowUserInput Set to true to allow the user to input its own value manually, false otherwise.
     * @throws NullPointerException If the defaultValues parameter is null.
     */
    public EzVarDouble(final String varName, final Double[] defaultValues, final boolean allowUserInput) throws NullPointerException {
        this(varName, defaultValues, 0, allowUserInput);
    }

    /**
     * Creates a new integer variable with a given array of possible values.
     *
     * @param varName           The name of the variable (as it will appear on the interface).
     * @param defaultValues     The list of possible values the user may choose from.
     * @param defaultValueIndex The index of the default selected value.
     * @param allowUserInput    Set to true to allow the user to input its own value manually, false otherwise.
     * @throws NullPointerException If the defaultValues parameter is null.
     */
    public EzVarDouble(final String varName, final Double[] defaultValues, final int defaultValueIndex, final boolean allowUserInput) throws NullPointerException {
        super(new VarDouble(varName, 0.0), defaultValues, defaultValueIndex, allowUserInput);
    }
}
