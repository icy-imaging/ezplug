package plugins.adufour.vars.lang;

import plugins.adufour.vars.util.VarListener;
import ij.ImagePlus;

public class VarImagePlus extends Var<ImagePlus> {
    /**
     * Creates a new ImagePlus {@link Var}iable with the specified name and default value (may be null)
     *
     * @param name         The name of this variable.
     * @param defaultValue The value assigned by default for this variable.
     */
    public VarImagePlus(String name, ImagePlus defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new ImagePlus {@link Var}iable with the specified name and default value (may be null). Optionally, a listener can be added to
     * track events on the variable.
     *
     * @param name            The name of this variable.
     * @param defaultValue    The value assigned by default for this variable.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarImagePlus(String name, ImagePlus defaultValue, VarListener<ImagePlus> defaultListener) {
        super(name, ImagePlus.class, defaultValue, defaultListener);
    }
}
