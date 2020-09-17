package plugins.adufour.vars.lang;

import icy.sequence.Sequence;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

/**
 * Special variable storing an integer representing a slice number in a sequence
 * 
 * @author Alexandre Dufour
 */
public class VarSlice extends VarInteger
{
    private boolean allowAllSlices;

    private Var<Sequence> sequence;

    /**
     * Creates a new slice index {@link Var}iable with the specified name and referenced sequence.
     * 
     * @param name
     *        The name of this variable
     * @param sequence
     *        The sequence variable referenced by this variable.
     * @param allowAllSlices
     *        If true, the default slice is assigned to all (-1). Otherwise, the first slice will be the default value (0).
     */
    public VarSlice(String name, Var<Sequence> sequence, boolean allowAllSlices)
    {
        this(name, sequence, allowAllSlices, null);
    }

    /**
     * Creates a new slice index {@link Var}iable with the specified name and referenced sequence. Optionally, a listener can be added to track events on the
     * variable.
     * 
     * @param name
     *        The name of this variable
     * @param sequence
     *        The sequence variable referenced by this variable.
     * @param allowAllSlices
     *        If true, the default slice is assigned to all (-1). Otherwise, the first slice will be the default value (0).
     * @param defaultListener
     *        A listener to add to this variable immediately after creation
     */
    public VarSlice(String name, Var<Sequence> sequence, boolean allowAllSlices, VarListener<Integer> defaultListener)
    {
        super(name, allowAllSlices ? -1 : 0, defaultListener);

        this.sequence = sequence;
        this.allowAllSlices = allowAllSlices;
    }

    @Override
    public VarEditor<Integer> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createSliceSelector(this, sequence, allowAllSlices);
    }
}
