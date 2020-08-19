package plugins.adufour.vars.lang;

import plugins.adufour.vars.util.VarListener;

public class VarLong extends VarNumber<Long>
{
    /**
     * @param name
     * @param defaultValue
     */
    public VarLong(String name, long defaultValue)
    {
        this(name, defaultValue, null);
    }

    /**
     * @param name
     * @param defaultValue
     * @param defaultListener
     *        A listener to add to this variable immediately after creation
     */
    public VarLong(String name, long defaultValue, VarListener<Long> defaultListener)
    {
        super(name, Long.TYPE, Long.valueOf(defaultValue), defaultListener);
    }

    @Override
    public Long parse(String s)
    {
        return Long.valueOf(s);
    }

    @Override
    public int compareTo(Long value)
    {
        return getValue().compareTo(value);
    }

    @Override
    public Long getValue()
    {
        return getValue(false);
    }

    /**
     * Returns an integer representing the variable value.<br>
     * NOTE: if the current variable references a variable of different (wider) type, truncation
     * will occur
     */
    @Override
    public Long getValue(boolean forbidNull)
    {
        final Long result = super.getValue(forbidNull);

        return result == null ? null : result;
    }

    @Override
    protected boolean areValuesEqual(Long a, Object b)
    {
        if (b instanceof Number)
        {
            return super.areValuesEqual(a, ((Number) b).longValue());
        }
        return false;
    }
}
