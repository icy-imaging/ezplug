package plugins.adufour.vars.lang;

import plugins.adufour.vars.util.VarListener;

public class VarLong extends VarNumber<Long> {

    /**
     * Creates a new long integer {@link Var}iable with the specified name and default value.
     *
     * @param name         The name of this variable.
     * @param defaultValue The value assigned by default for this variable.
     */
    public VarLong(String name, long defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new long integer {@link Var}iable with the specified name and default value. Optionally, a listener can be added to track events
     * on the variable.
     *
     * @param name            The name of this variable.
     * @param defaultValue    The value assigned by default for this variable.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarLong(String name, long defaultValue, VarListener<Long> defaultListener) {
        super(name, Long.TYPE, Long.valueOf(defaultValue), defaultListener);
    }

    @Override
    public Long parse(String s) {
        return Long.valueOf(s);
    }

    @Override
    public int compareTo(Long value) {
        return getValue().compareTo(value);
    }

    @Override
    public Long getValue() {
        return getValue(false);
    }

    /**
     * @return An integer representing the variable value. <b>Warning:</b> If the current variable references a variable of different (wider) type, truncation
     * will occur.
     */
    @Override
    public Long getValue(boolean forbidNull) {
        return super.getValue(forbidNull);
    }

    @Override
    protected boolean areValuesEqual(Long a, Object b) {
        if (b instanceof Number) {
            return super.areValuesEqual(a, ((Number) b).longValue());
        }
        return false;
    }
}
