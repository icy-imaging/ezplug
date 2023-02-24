package plugins.adufour.vars.lang;

import plugins.adufour.vars.util.VarListener;

/**
 * Class defining a variable with a native array of type <code>int[]</code>.
 *
 * @author Alexandre Dufour
 */
public class VarIntegerArrayNative extends VarGenericArray<int[]> {
    /**
     * Creates a new integer array {@link Var}iable with the specified name and default value.
     *
     * @param name         The name of this variable.
     * @param defaultValue The value assigned by default for this variable. If null, a new integer array of size zero of the given type is assigned as default value.
     */
    public VarIntegerArrayNative(String name, int[] defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new integer array {@link Var}iable with the specified name and default value. Optionally, a listener can be added to track events on the
     * variable.
     *
     * @param name            The name of this variable.
     * @param defaultValue    The value assigned by default for this variable. If null, a new integer array of size zero of the given type is assigned as default value.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarIntegerArrayNative(String name, int[] defaultValue, VarListener<int[]> defaultListener) {
        super(name, int[].class, defaultValue, defaultListener);
    }

    @Override
    public Object parseComponent(String s) {
        return Integer.parseInt(s);
    }

    @Override
    public int[] getValue(boolean forbidNull) {
        // handle the case where the reference is not an array

        @SuppressWarnings("rawtypes")
        Var reference = getReference();

        if (reference == null)
            return super.getValue(forbidNull);

        Object value = reference.getValue();

        if (value == null)
            return super.getValue(forbidNull);

        if (value instanceof Number)
            return new int[]{((Number) value).intValue()};

        return (int[]) value;
    }
}
