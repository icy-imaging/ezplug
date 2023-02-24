package plugins.adufour.vars.lang;

import plugins.adufour.vars.util.VarListener;

public class VarObject extends Var<Object> {

    /**
     * Creates a new object {@link Var}iable with the specified name and default value (may be null)
     *
     * @param name         The name of this variable.
     * @param defaultValue The value assigned by default for this variable.
     */
    public VarObject(String name, Object defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new object {@link Var}iable with the specified name and default value (may be null). Optionally, a listener can be added to track events on the
     * variable.
     *
     * @param name            The name of this variable.
     * @param defaultValue    The value assigned by default for this variable.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarObject(String name, Object defaultValue, VarListener<Object> defaultListener) {
        super(name, Object.class, defaultValue, defaultListener);
    }

    @Override
    public boolean isAssignableFrom(Var<?> source) {
        // an Object type variable may always point to any DEFINED type

        return getType() != null;
    }
}
