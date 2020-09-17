package plugins.adufour.vars.lang;

import icy.sequence.Sequence;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

/**
 * Special variable storing an integer representing a frame number in a sequence
 * 
 * @author Alexandre Dufour
 */
public class VarFrame extends VarInteger
{
    private boolean allowAllFrames;

    private Var<Sequence> sequence;

    /**
     * Creates a new frame {@link Var}iable of a given sequence and with the specified name.
     * 
     * @param name
     *        The name of this variable.
     * @param sequence
     *        The sequence from which frames are selected.
     * @param allowAllFrames
     *        If true, all frames can be selected (default value). The first frame is the default value otherwise.
     */
    public VarFrame(String name, Var<Sequence> sequence, boolean allowAllFrames)
    {
        this(name, sequence, allowAllFrames, null);
    }

    /**
     * Creates a new frame {@link Var}iable of a given sequence and with the specified name. Optionally, a listener can be added to track events on the
     * variable.
     * 
     * @param name
     *        The name of this variable.
     * @param sequence
     *        The sequence from which frames are selected.
     * @param allowAllFrames
     *        If true, all frames can be selected (default value). The first frame is the default value otherwise.
     * @param defaultListener
     *        A listener to add to this variable immediately after creation.
     */
    public VarFrame(String name, Var<Sequence> sequence, boolean allowAllFrames, VarListener<Integer> defaultListener)
    {
        super(name, allowAllFrames ? -1 : 0, defaultListener);

        this.sequence = sequence;
        this.allowAllFrames = allowAllFrames;
    }

    @Override
    public VarEditor<Integer> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createFrameSelector(this, sequence, allowAllFrames);
    }
}
