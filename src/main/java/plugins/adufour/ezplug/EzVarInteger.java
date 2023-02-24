package plugins.adufour.ezplug;

import plugins.adufour.vars.gui.model.IntegerRangeModel;
import plugins.adufour.vars.gui.model.RangeModel.RangeEditorType;
import plugins.adufour.vars.lang.VarInteger;

/**
 * Specialized implementation of {@link plugins.adufour.ezplug.EzVarNumeric} for variables of type integer.
 *
 * @author Alexandre Dufour
 */
public class EzVarInteger extends EzVarNumeric<Integer> {
    /**
     * Creates a new integer variable with default minimum and maximum values.
     *
     * @param varName The name of the variable (as it will appear on the interface).
     */
    public EzVarInteger(String varName) {
        this(varName, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
    }

    /**
     * Creates a new integer variable with specified minimum and maximum values.
     *
     * @param varName The name of the variable (as it will appear on the interface).
     * @param min     The minimum allowed value.
     * @param max     The maximum allowed value.
     * @param step    The step between consecutive values.
     */
    public EzVarInteger(String varName, int min, int max, int step) {
        this(varName, min, min, max, step);
    }

    /**
     * Creates a new integer variable with specified default, minimum and maximum values.
     *
     * @param varName The name of the variable (as it will appear on the interface).
     * @param value   The default value.
     * @param min     The minimum allowed value.
     * @param max     The maximum allowed value.
     * @param step    The step between consecutive values.
     */
    public EzVarInteger(String varName, int value, int min, int max, int step) {
        this(varName, value, min, max, step, RangeEditorType.SPINNER);
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
     */
    public EzVarInteger(String varName, int value, int min, int max, int step, RangeEditorType editorType) {
        super(new VarInteger(varName, value), new IntegerRangeModel(value, min, max, step));
    }

    /**
     * Creates a new integer variable with a given array of possible values.
     *
     * @param varName        The name of the variable (as it will appear on the interface).
     * @param defaultValues  The list of possible values the user may choose from.
     * @param allowUserInput Set to true to allow the user to input its own value manually, false otherwise.
     * @throws NullPointerException If the defaultValues parameter is null.
     */
    public EzVarInteger(String varName, Integer[] defaultValues, boolean allowUserInput) throws NullPointerException {
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
    public EzVarInteger(String varName, Integer[] defaultValues, int defaultValueIndex, boolean allowUserInput) throws NullPointerException {
        super(new VarInteger(varName, 0), defaultValues, defaultValueIndex, allowUserInput);
    }
}