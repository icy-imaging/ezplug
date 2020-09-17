package plugins.adufour.vars.lang;

import icy.sequence.Sequence;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

public class VarSequenceArray extends VarGenericArray<Sequence[]>
{
    /**
     * Creates a new sequence array {@link Var}iable with the specified name. By default, the array is initialized to size 0.
     * 
     * @param name
     *        The name of this variable.
     */
    public VarSequenceArray(String name)
    {
        this(name, (VarListener<Sequence[]>) null);
    }

    /**
     * Creates a new sequence array {@link Var}iable with the specified name. By default, the array is initialized to size 0. Optionally, a listener can be
     * added to track events on the variable.
     * 
     * @param name
     *        The name of this variable.
     * @param defaultListener
     *        A listener to add to this variable immediately after creation.
     */
    public VarSequenceArray(String name, VarListener<Sequence[]> defaultListener)
    {
        super(name, Sequence[].class, new Sequence[0], defaultListener);
    }

    /**
     * Creates a new sequence array {@link Var}iable with the specified name and holding the given sequences.
     * 
     * @param name
     *        The name of this variable.
     * @param sequences
     *        The sequence array assigned by default for this variable. If null, a new sequence array of size zero is assigned as default value.
     */
    public VarSequenceArray(String name, Sequence... sequences)
    {
        super(name, Sequence[].class, sequences != null ? sequences : new Sequence[0]);
    }

    @Override
    public VarEditor<Sequence[]> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createSequenceList(this);
    }
}
