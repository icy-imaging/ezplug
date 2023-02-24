package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.gui.model.RangeModel;
import plugins.adufour.vars.gui.model.RangeModel.RangeEditorType;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.util.VarListener;

/**
 * Class bringing support for variables handling comparable types
 *
 * @param <N> The numeric type handled by this instance.
 * @author Alexandre Dufour
 */
public abstract class VarNumber<N extends Number> extends Var<N> implements Comparable<N> {
    /**
     * Creates a new numeric {@link Var}iable with the specified name, and inner numeric value type and default value (may be null).
     *
     * @param name         The name of this variable.
     * @param type         The numeric type of this variable.
     * @param defaultValue The value assigned by default for this variable.
     */
    public VarNumber(String name, Class<N> type, N defaultValue) {
        this(name, type, defaultValue, null);
    }

    /**
     * Creates a new numeric {@link Var}iable with the specified name, and inner numeric value type and default value (may be null). Optionally, a listener can
     * be added to track events on the variable.
     *
     * @param name            The name of this variable.
     * @param type            The numeric type of this variable.
     * @param defaultValue    The value assigned by default for this variable.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarNumber(String name, Class<N> type, N defaultValue, VarListener<N> defaultListener) {
        super(name, type, defaultValue, defaultListener);
    }

    /**
     * Creates a new numeric {@link Var}iable with the specified name and editor model.
     *
     * @param name        The name of this variable.
     * @param editorModel The default editor model of this numeric variable (can be changed later via the {@link #setDefaultEditorModel(VarEditorModel)} method.
     */
    public VarNumber(String name, VarEditorModel<N> editorModel) {
        super(name, editorModel);
    }

    @Override
    public boolean isAssignableFrom(Var<?> source) {
        if (source.getType() == null)
            return false;

        Class<?> sourceType = source.getType();

        return sourceType == Double.TYPE
                || sourceType == Float.TYPE
                || sourceType == Integer.TYPE
                || Number.class.isAssignableFrom(source.getType());
    }

    @Override
    public VarEditor<N> createVarEditor() {
        VarEditorFactory factory = VarEditorFactory.getDefaultFactory();

        if (getDefaultEditorModel() instanceof RangeModel) {
            RangeEditorType editorType = ((RangeModel<N>) getDefaultEditorModel()).getEditorType();
            switch (editorType) {
                case SLIDER:
                    return factory.createSlider(this);
                case SPINNER:
                    return factory.createSpinner(this);
                default:
                    throw new UnsupportedOperationException("Editor not yet implemented: " + editorType);
            }
        }

        if (getDefaultEditorModel() == null)
            return factory.createTextField(this);

        return super.createVarEditor();
    }

    @Override
    protected boolean areValuesEqual(Number a, Object b) {
        if (b instanceof Number) {
            return Double.compare(a.doubleValue(), ((Number) b).doubleValue()) == 0;
        }
        return false;
    }
}
