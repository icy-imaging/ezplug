/*
 * Copyright (c) 2010-2025. Institut Pasteur.
 *
 * This file is part of Icy.
 * Icy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Icy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Icy. If not, see <https://www.gnu.org/licenses/>.
 */

package plugins.adufour.vars.lang;

import org.jetbrains.annotations.NotNull;
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
     * Creates a new numeric {@link Var} variable with the specified name,
     * and inner numeric value type and default value (can be null).
     *
     * @param name         The name of this variable.
     * @param type         The numeric type of this variable.
     * @param defaultValue The value assigned by default for this variable.
     */
    public VarNumber(final String name, final Class<N> type, final N defaultValue) {
        this(name, type, defaultValue, null);
    }

    /**
     * Creates a new numeric {@link Var} variable with the specified name,
     * and inner numeric value type and default value (can be null).
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable.
     * @param type            The numeric type of this variable.
     * @param defaultValue    The value assigned by default for this variable.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarNumber(final String name, final Class<N> type, final N defaultValue, final VarListener<N> defaultListener) {
        super(name, type, defaultValue, defaultListener);
    }

    /**
     * Creates a new numeric {@link Var} variable with the specified name and editor model.
     *
     * @param name        The name of this variable.
     * @param editorModel The default editor model of this numeric variable
     *                    (can be changed later via the {@link #setDefaultEditorModel(VarEditorModel)} method).
     */
    public VarNumber(final String name, final VarEditorModel<N> editorModel) {
        super(name, editorModel);
    }

    @Override
    public boolean isAssignableFrom(final @NotNull Var<?> source) {
        if (source.getType() == null)
            return false;

        final Class<?> sourceType = source.getType();

        return sourceType == Double.TYPE
                || sourceType == Float.TYPE
                || sourceType == Integer.TYPE
                || Number.class.isAssignableFrom(source.getType());
    }

    @Override
    public VarEditor<N> createVarEditor() {
        final VarEditorFactory factory = VarEditorFactory.getDefaultFactory();

        if (getDefaultEditorModel() instanceof RangeModel) {
            final RangeEditorType editorType = ((RangeModel<N>) getDefaultEditorModel()).getEditorType();
            return switch (editorType) {
                case SLIDER -> factory.createSlider(this);
                case SPINNER -> factory.createSpinner(this);
                default -> throw new UnsupportedOperationException("Editor not yet implemented: " + editorType);
            };
        }

        if (getDefaultEditorModel() == null)
            return factory.createTextField(this);

        return super.createVarEditor();
    }

    @Override
    protected boolean areValuesEqual(final @NotNull Number a, final Object b) {
        if (b instanceof Number) {
            return Double.compare(a.doubleValue(), ((Number) b).doubleValue()) == 0;
        }
        return false;
    }
}
