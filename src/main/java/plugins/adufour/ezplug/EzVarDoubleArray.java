package plugins.adufour.ezplug;

import plugins.adufour.vars.lang.VarDoubleArray;

/**
 * Double arrays
 * 
 * @deprecated use {@link EzVarDoubleArrayNative} instead (optimized performances)
 * @author Alexandre Dufour
 */
@Deprecated
public class EzVarDoubleArray extends EzVar<Double[]>
{
    /**
     * Creates a new integer variable with a given array of possible values.
     * 
     * @param varName
     *        The name of the variable (as it will appear on the interface).
     * @param defaultValues
     *        The list of possible values the user may choose from.
     * @param allowUserInput
     *        Set to true to allow the user to input its own value manually, false otherwise.
     * @throws NullPointerException
     *         If the defaultValues parameter is null.
     */
    public EzVarDoubleArray(String varName, Double[][] defaultValues, boolean allowUserInput)
            throws NullPointerException
    {
        this(varName, defaultValues, 0, allowUserInput);
    }

    /**
     * Creates a new integer variable with a given array of possible values.
     * 
     * @param varName
     *        The name of the variable (as it will appear on the interface).
     * @param defaultValues
     *        The list of possible values the user may choose from.
     * @param defaultValueIndex
     *        The index of the default selected value.
     * @param allowUserInput
     *        Set to true to allow the user to input its own value manually, false otherwise.
     * @throws NullPointerException
     *         If the defaultValues parameter is null.
     */
    public EzVarDoubleArray(String varName, Double[][] defaultValues, int defaultValueIndex, boolean allowUserInput)
            throws NullPointerException
    {
        super(new VarDoubleArray(varName, null), defaultValues, defaultValueIndex, allowUserInput);
    }
}