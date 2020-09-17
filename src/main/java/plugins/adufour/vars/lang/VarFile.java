package plugins.adufour.vars.lang;

import java.io.File;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.FileChooser;
import plugins.adufour.vars.util.VarListener;

public class VarFile extends Var<File>
{
    /**
     * Creates a new file {@link Var}iable with the specified name and default value (may be null).
     * 
     * @param name
     *        the name of this variable
     * @param defaultValue
     *        the value assigned by default for this variable
     */
    public VarFile(String name, File defaultValue)
    {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new file {@link Var}iable with the specified name and default value (may be null). Optionally, a listener can be added to track events on the
     * variable.
     * 
     * @param name
     *        the name of this variable
     * @param defaultValue
     *        the value assigned by default for this variable
     * @param defaultListener
     *        A listener to add to this variable immediately after creation
     */
    public VarFile(String name, File defaultValue, VarListener<File> defaultListener)
    {
        super(name, File.class, defaultValue, defaultListener);
    }

    @Override
    public File parse(String s)
    {
        return new File(s);
    }

    @Override
    public String toString()
    {
        return getValue() == null ? null : getValue().getAbsolutePath();
    }

    @Override
    public VarEditor<File> createVarEditor()
    {
        return new FileChooser(this);
    }
}
