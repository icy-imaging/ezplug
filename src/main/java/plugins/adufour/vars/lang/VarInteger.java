package plugins.adufour.vars.lang;

import plugins.adufour.vars.util.VarListener;

public class VarInteger extends VarNumber<Integer>
{
    /**
     * Creates a new integer {@link Var}iable with the specified name and default value.
     * 
     * @deprecated use {@link #VarInteger(String, int)} instead
     * @param name
     *        The name of this variable
     * @param defaultValue
     *        The value assigned by default for this variable. If null, 0 is assigned as the default value.
     */
    @Deprecated
    public VarInteger(String name, Integer defaultValue)
    {
        this(name, defaultValue == null ? 0 : defaultValue.intValue());
    }

    /**
     * Creates a new integer {@link Var}iable with the specified name and default value.
     * 
     * @param name
     *        The name of this variable
     * @param defaultValue
     *        The value assigned by default for this variable
     */
    public VarInteger(String name, int defaultValue)
    {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new integer {@link Var}iable with the specified name and default value. Optionally, a listener can be added to track events on
     * the variable.
     * 
     * @param name
     *        The name of this variable
     * @param defaultValue
     *        The value assigned by default for this variable
     * @param defaultListener
     *        A listener to add to this variable immediately after creation
     */
    public VarInteger(String name, int defaultValue, VarListener<Integer> defaultListener)
    {
        super(name, Integer.TYPE, defaultValue, defaultListener);
    }

    @Override
    public Integer parse(String s)
    {
        return Integer.parseInt(s);
    }

    @Override
    public int compareTo(Integer integer)
    {
        return getValue().compareTo(integer);
    }

    @Override
    public Integer getValue()
    {
        return getValue(false);
    }

    /**
     * Returns an integer representing the variable value.<br>
     * NOTE: if the current variable references a variable of different (wider) type, truncation
     * will occur
     */
    public Integer getValue(boolean forbidNull)
    {
        Number number = super.getValue(forbidNull);

        return number == null ? null : number.intValue();
    }
}
