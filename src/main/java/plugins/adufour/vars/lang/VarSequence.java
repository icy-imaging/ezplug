package plugins.adufour.vars.lang;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import icy.main.Icy;
import icy.sequence.Sequence;
import icy.util.StringUtil;
import icy.util.XMLUtil;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

public class VarSequence extends Var<Sequence>
{
    public static final String NO_SEQUENCE = "No Sequence";
    public static final String ACTIVE_SEQUENCE = "Active Sequence";

    private boolean activeSequenceSelected;

    /**
     * Creates a new sequence {@link Var}iable with the specified name and default value (may be null).
     * 
     * @param name
     *        The name of this variable.
     * @param defaultValue
     *        The sequence assigned by default for this variable. If null, the active sequence will be assigned.
     */
    public VarSequence(String name, Sequence defaultValue)
    {
        this(name, defaultValue, null);
    }

    /**
     * Creates a new sequence {@link Var}iable with the specified name and default value (may be null). Optionally, a listener can be added to
     * track events on the variable.
     * 
     * @param name
     *        The name of this variable.
     * @param defaultValue
     *        The sequence assigned by default for this variable. If null, the active sequence will be assigned.
     * @param defaultListener
     *        A listener to add to this variable immediately after creation.
     */
    public VarSequence(String name, Sequence defaultValue, VarListener<Sequence> defaultListener)
    {
        super(name, Sequence.class, defaultValue, defaultListener);

        final Sequence seq = Icy.getMainInterface().getActiveSequence();

        // if we have an active Sequence then we set it by default
        if (seq != null && defaultValue == null)
        {
            activeSequenceSelected = true;
            setValue(seq);
        }
        else
            activeSequenceSelected = false;
    }

    /**
     * Saves the current variable to the specified node
     * 
     * @throws UnsupportedOperationException
     *         if the functionality is not supported by the current variable type
     */
    @Override
    public boolean saveToXML(Node node) throws UnsupportedOperationException
    {
        XMLUtil.setAttributeValue((Element) node, Var.XML_KEY_VALUE, getValueAsString());

        return true;
    }

    @Override
    public boolean loadFromXML(Node node)
    {
        setValueAsString(XMLUtil.getAttributeValue((Element) node, XML_KEY_VALUE, null));

        return true;
    }

    @Override
    public VarEditor<Sequence> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createSequenceChooser(this);
    }

    @Override
    public VarEditor<Sequence> createVarViewer()
    {
        return VarEditorFactory.getDefaultFactory().createSequenceViewer(this);
    }

    @Override
    public String getValueAsString()
    {
        Sequence s = getValue();

        if (isActiveSequenceSelected())
            return ACTIVE_SEQUENCE;
        if (s == null)
            return NO_SEQUENCE;
        // if ((s == Icy.getMainInterface().getActiveSequence()) && activeSequenceSelected)
        // return ACTIVE_SEQUENCE;

        if (s.getFilename() != null)
            return s.getFilename();

        return s.getName();
    }

    public boolean isActiveSequenceSelected()
    {
        final Var<? extends Sequence> ref = getReference();

        if (ref instanceof VarSequence)
            return ((VarSequence) ref).isActiveSequenceSelected();

        return activeSequenceSelected;
    }

    public void setActiveSequenceSelected(boolean value)
    {
        activeSequenceSelected = value;
    }

    /**
     * @deprecated Uses {@link #setActiveSequenceSelected(boolean)} instead
     */
    @Deprecated
    public boolean isNoSequenceSelected()
    {
        return StringUtil.equals(getValueAsString(), NO_SEQUENCE);
    }

    /**
     * @deprecated Uses {@link #isActiveSequenceSelected()} instead
     */
    @Deprecated
    public void setNoSequenceSelection()
    {
        setValue(null);
    }

    @Override
    public void setValueAsString(String newValue)
    {
        // FIX: simpler implementation (Stephane)
        if (StringUtil.equals(newValue, ACTIVE_SEQUENCE))
        {
            boolean changed = !activeSequenceSelected;
            final Sequence oldValue = getValue();
            final Sequence seq = Icy.getMainInterface().getActiveSequence();

            // "Active Sequence" selection
            setActiveSequenceSelected(true);
            // use parent setValue to keep ActiveSequence set
            super.setValue(seq);

            // force event if activeSequenceSelected changed (for correct refresh)
            if (changed)
                fireVariableChanged(oldValue, seq);
        }
        // "No Sequence" selection or not supported
        else
            setValue(null);
    }

    @Override
    public void setValue(Sequence newValue) throws IllegalAccessError
    {
        boolean changed = activeSequenceSelected;
        final Sequence oldValue = getValue();

        // FIX: set by value ? unset "Active Sequence" selection (Stephane)
        setActiveSequenceSelected(false);

        super.setValue(newValue);

        // force event if activeSequenceSelected changed (for correct refresh)
        if (changed)
            fireVariableChanged(oldValue, newValue);
    }
}
