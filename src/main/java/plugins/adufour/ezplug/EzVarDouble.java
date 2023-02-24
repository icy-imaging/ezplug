package plugins.adufour.ezplug;

import java.util.HashMap;

import plugins.adufour.vars.gui.model.DoubleRangeModel;
import plugins.adufour.vars.gui.model.RangeModel.RangeEditorType;
import plugins.adufour.vars.lang.VarDouble;

/**
 * Specialized implementation of {@link plugins.adufour.ezplug.EzVarNumeric} for variables of type double.
 *
 * @author Alexandre Dufour
 */
public class EzVarDouble extends EzVarNumeric<Double> {
    /**
     * Creates a new double variable with default minimum and maximum values, and a default step size of 0.01.
     *
     * @param varName The name of the variable (as it will appear on the interface).
     */
    public EzVarDouble(String varName) {
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
    public EzVarDouble(String varName, double min, double max, double step) {
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
    public EzVarDouble(String varName, double value, double min, double max, double step) {
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
    public EzVarDouble(String varName, double value, double min, double max, double step, RangeEditorType editorType, HashMap<Double, String> labels) {
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
    public EzVarDouble(String varName, Double[] defaultValues, boolean allowUserInput) throws NullPointerException {
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
    public EzVarDouble(String varName, Double[] defaultValues, int defaultValueIndex, boolean allowUserInput) throws NullPointerException {
        super(new VarDouble(varName, 0.0), defaultValues, defaultValueIndex, allowUserInput);
    }
}
