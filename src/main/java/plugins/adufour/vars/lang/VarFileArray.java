package plugins.adufour.vars.lang;

import java.io.File;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.FilesChooser;
import plugins.adufour.vars.util.VarListener;

/**
 * Specialized implementation of Var to manipulate file arrays
 *
 * @author Alexandre Dufour
 */
public class VarFileArray extends VarGenericArray<File[]> {
    /**
     * Creates a new file array {@link Var}iable with the specified name and default value
     *
     * @param name         the name of this variable
     * @param defaultValue the value assigned by default for this variable. If null, a new array of size zero of the given type is assigned as default value.
     */
    public VarFileArray(String name, File[] defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new file array {@link Var}iable with the specified name and default value. Optionally, a listener can be added to track events on the variable.
     *
     * @param name            the name of this variable
     * @param defaultValue    the value assigned by default for this variable. If null, a new array of size zero of the given type is assigned as default value.
     * @param defaultListener A listener to add to this variable immediately after creation
     */
    public VarFileArray(String name, File[] defaultValue, VarListener<File[]> defaultListener) {
        super(name, File[].class, defaultValue, defaultListener);
    }

    @Override
    public String getSeparator(int dimension) {
        return File.pathSeparator;
    }

    @Override
    public VarEditor<File[]> createVarEditor() {
        return new FilesChooser(this);
    }

    @Override
    public Object parseComponent(String s) {
        return new java.io.File(s);
    }
}
