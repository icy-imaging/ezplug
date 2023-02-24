package plugins.adufour.ezplug;

import plugins.adufour.vars.gui.model.FloatRangeModel;
import plugins.adufour.vars.gui.model.RangeModel.RangeEditorType;
import plugins.adufour.vars.lang.VarFloat;

/**
 * Specialized implementation of {@link plugins.adufour.ezplug.EzVarNumeric} for variables of type float.
 *
 * @author Alexandre Dufour
 */
public class EzVarFloat extends EzVarNumeric<Float> {
    /**
     * Creates a new integer variable with default minimum and maximum values, and a default step size of 0.01.
     *
     * @param varName The name of the variable (as it will appear on the interface).
     */
    public EzVarFloat(String varName) {
        this(varName, 0, 0, Float.MAX_VALUE, 0.01f);
    }

    /**
     * Creates a new integer variable with specified minimum and maximum values.
     *
     * @param varName The name of the variable (as it will appear on the interface)
     * @param min     The minimum allowed value
     * @param max     The maximum allowed value
     * @param step    The step between consecutive values
     */
    public EzVarFloat(String varName, float min, float max, float step) {
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
    public EzVarFloat(String varName, float value, float min, float max, float step) {
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
    public EzVarFloat(String varName, float value, float min, float max, float step, RangeEditorType editorType) {
        super(new VarFloat(varName, 0.0f), new FloatRangeModel(value, min, max, step));
    }

    /**
     * Creates a new integer variable with a given array of possible values.
     *
     * @param varName        The name of the variable (as it will appear on the interface).
     * @param defaultValues  The list of possible values the user may choose from.
     * @param allowUserInput Set to true to allow the user to input its own value manually, false otherwise.
     * @throws NullPointerException If the defaultValues parameter is null.
     */
    public EzVarFloat(String varName, Float[] defaultValues, boolean allowUserInput) throws NullPointerException {
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
    public EzVarFloat(String varName, Float[] defaultValues, int defaultValueIndex, boolean allowUserInput) throws NullPointerException {
        super(new VarFloat(varName, 0.0f), defaultValues, defaultValueIndex, allowUserInput);
    }
}
