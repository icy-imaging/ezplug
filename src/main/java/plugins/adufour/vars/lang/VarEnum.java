package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.model.ValueSelectionModel;
import plugins.adufour.vars.util.VarListener;

public class VarEnum<T extends Enum<T>> extends Var<T> {
    /**
     * Creates a new {@link Var}iable with the specified name and default value (CANNOT be null).
     *
     * @param name         The name of this variable
     * @param defaultValue The <b>non-null</b> value assigned by default for this variable.
     * @throws NullPointerException If defaultValue is null
     */
    public VarEnum(String name, T defaultValue) throws NullPointerException {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new {@link Var}iable with the specified name and default value (CANNOT be null). Optionally, a listener can be added to track events on the
     * variable.
     *
     * @param name            The name of this variable
     * @param defaultValue    The <b>non-null</b> value assigned by default for this variable.
     * @param defaultListener A listener to add to this variable immediately after creation
     * @throws NullPointerException If defaultValue is null
     */
    public VarEnum(String name, T defaultValue, VarListener<T> defaultListener) throws NullPointerException {
        super(name, defaultValue.getDeclaringClass(), defaultValue, defaultListener);
        setDefaultEditorModel(new ValueSelectionModel<>(defaultValue.getDeclaringClass().getEnumConstants(), defaultValue, false));
    }

    @Override
    public String getValueAsString() {
        return getValue().name();
    }

    /**
     * Parses the given string into an Enumeration value
     */
    @Override
    public T parse(String s) {
        return Enum.valueOf(getType(), s);
    }
}
