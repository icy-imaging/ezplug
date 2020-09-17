package plugins.adufour.vars.lang;

import plugins.adufour.vars.util.VarListener;

public class VarFloat extends VarNumber<Float>
{

    /**
     * Creates a new float {@link Var}iable with the specified name and default value.
     * 
     * @param name
     *        The name of this variable
     * @param defaultValue
     *        The value assigned by default for this variable. If null, the default value is set to 0f
     */
    @Deprecated
    public VarFloat(String name, Float defaultValue)
    {
        this(name, defaultValue == null ? 0f : defaultValue.floatValue());
    }

    /**
     * Creates a new float {@link Var}iable with the specified name and default value.
     * 
     * @param name
     *        The name of this variable
     * @param defaultValue
     *        The value assigned by default for this variable
     */
    public VarFloat(String name, float defaultValue)
    {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new float {@link Var}iable with the specified name and default value. Optionally, a listener can be added to track events on the variable.
     * 
     * @param name
     *        The name of this variable
     * @param defaultValue
     *        The value assigned by default for this variable
     * @param defaultListener
     *        A listener to add to this variable immediately after creation
     */
    public VarFloat(String name, float defaultValue, VarListener<Float> defaultListener)
    {
        super(name, Float.TYPE, defaultValue, defaultListener);
    }

    @Override
    public Float parse(String s)
    {
        return Float.parseFloat(s);
    }

    @Override
    public int compareTo(Float f)
    {
        return getValue().compareTo(f);
    }

    @Override
    public Float getValue()
    {
        return getValue(false);
    }

    /**
     * @return The float representing the variable value.
     *         <p>
     *         <b>Warning:</b> if the current variable references a variable of different (wider) type, truncation
     *         will occur.
     */
    @Override
    public Float getValue(boolean forbidNull)
    {
        Number number = super.getValue(forbidNull);

        return number == null ? null : number.floatValue();
    }
}
