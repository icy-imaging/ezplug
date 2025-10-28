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

import org.bioimageanalysis.icy.common.collection.array.Array1DUtil;
import org.bioimageanalysis.icy.common.collection.array.ArrayUtil;
import org.bioimageanalysis.icy.common.type.DataType;
import org.bioimageanalysis.icy.model.sequence.Sequence;
import org.jetbrains.annotations.NotNull;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.gui.model.TypeSelectionModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.util.MutableType;
import plugins.adufour.vars.util.TypeChangeListener;
import plugins.adufour.vars.util.VarListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Variable holding a value with a mutable type, i.e., its type may be changed at runtime.
 *
 * @author Alexandre Dufour
 */
@SuppressWarnings("rawtypes")
public class VarMutable extends Var implements MutableType {
    private final ArrayList<TypeChangeListener> listeners = new ArrayList<>();

    /**
     * Creates a new mutable-type {@link Var} variable with the specified name and initial type.
     * The default value is always set to null in order to be properly reset.
     *
     * @param name        The name of the new variable.
     * @param initialType The initial type of the new variable.
     *                    The type can be changed using the {@link #setType(Class)} method.
     *                    If null, the type is set {@link Object}.
     */
    public VarMutable(final String name, final Class<?> initialType) {
        this(name, initialType, null);
    }

    /**
     * Creates a new mutable-type {@link Var} variable with the specified name and initial type.
     * The default value is always set to null in order to be properly reset.
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of the new variable.
     * @param initialType     The initial type of the new variable.
     *                        The type can be changed using the {@link #setType(Class)} method.
     *                        If null, the type is set {@link Object}.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    @SuppressWarnings("unchecked")
    public VarMutable(final String name, final Class<?> initialType, final VarListener<?> defaultListener) {
        super(name, initialType != null ? initialType : Object.class, null, defaultListener);
    }

    @Override
    public VarEditor createVarEditor() {
        final VarEditorFactory factory = VarEditorFactory.getDefaultFactory();

        if (getDefaultEditorModel() instanceof TypeSelectionModel) {
            return factory.createTypeChooser(this);
        }

        // Revert to the default editor
        return factory.createMutableVarEditor(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public VarEditor createVarViewer() {
        if (Sequence.class.equals(type)) {
            return VarEditorFactory.getDefaultFactory().createSequenceViewer(this);
        }

        // Revert to the default viewer
        return super.createVarViewer();
    }

    @Override
    public void addTypeChangeListener(final TypeChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeTypeChangeListener(final TypeChangeListener listener) {
        listeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isAssignableFrom(final @NotNull Var source) {
        final Class<?> sourceType = source.getType();

        // cannot point to null
        if (sourceType == null)
            return false;

        // null can point to anything (the type will change after linking)
        if (type == null)
            return true;

        // special case for primitive numbers
        if (Number.class.isAssignableFrom(type) && sourceType.isPrimitive())
            return true;
        // and the other way round
        if (Number.class.isAssignableFrom(sourceType) && type.isPrimitive())
            return true;

        // last (default) case
        return type.isAssignableFrom(sourceType)
                || (type.isArray() && type.getComponentType().isAssignableFrom(sourceType));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setDefaultEditorModel(final VarEditorModel model) {
        if (model instanceof TypeSelectionModel) {
            defaultEditorModel = model;
            setType(((TypeSelectionModel) model).getDefaultValue());
        }
        else
            super.setDefaultEditorModel(model);
    }

    @Override
    public void setType(final Class<?> newType) {
        final Class<?> oldType = this.getType();

        if (oldType == newType)
            return;

        if (isReferenced())
            throw new IllegalAccessError("Cannot change the type of variable \"" + getName()
                    + "\": it is being referenced by another variable");

        setValue(null);

        this.type = newType;

        for (final TypeChangeListener listener : listeners)
            listener.typeChanged(this, oldType, newType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(final Object newValue) {
        // Since this method is loosely typed, we need to make the difference between single objects
        // and arrays of objects to prevent potential (erroneous) ClassCastException(s)

        // the obvious ones first
        if (newValue == null || getType().isAssignableFrom(newValue.getClass())) {
            super.setValue(newValue);
        }
        // the easy ones next
        else if (
                newValue instanceof Number && Number.class.isAssignableFrom(getType())
                        || (getType().isPrimitive() && getType() != Boolean.TYPE)
        ) {
            // put the common ones first to optimize a bit...
            if (getType() == Integer.class || getType() == Integer.TYPE)
                super.setValue(((Number) newValue).intValue());
            else if (getType() == Double.class || getType() == Double.TYPE)
                super.setValue(((Number) newValue).doubleValue());
            else if (getType() == Byte.class || getType() == Byte.TYPE)
                super.setValue(((Number) newValue).byteValue());
            else if (getType() == Short.class || getType() == Short.TYPE)
                super.setValue(((Number) newValue).shortValue());
            else if (getType() == Float.class || getType() == Float.TYPE)
                super.setValue(((Number) newValue).floatValue());
            else
                super.setValue(((Number) newValue).longValue());
        }
        // and now for the hard one
        else if (getType().isArray() && newValue.getClass().isArray()) {
            // the easy way (both arrays match) is already covered above
            // => we need to handle the tricky case (Javascript) where newValue is *always* Object[]
            // ==> copy its elements "manually" into a valid array

            try {
                // create a new valid array
                final Class<?> localType = getType().getComponentType();
                final int nbValues = Array.getLength(newValue);
                final Object array = Array.newInstance(localType, nbValues);

                if (localType.isPrimitive()) {
                    final DataType dataType = ArrayUtil.getDataType(array);
                    for (int i = 0; i < nbValues; i++) {
                        // assume newValue also has numbers inside...
                        final Number n = (Number) Array.get(newValue, i);
                        // let Icy do the conversion
                        Array1DUtil.setValue(array, i, dataType, n.doubleValue());
                    }
                }
                else {
                    System.arraycopy(newValue, 0, array, 0, nbValues);
                }

                super.setValue(array);
            }
            catch (final ArrayStoreException typeError) {
                final String text = "[" + ArrayUtil.arrayToString(newValue).replace(":", ", ") + "]";
                throw new ClassCastException(text + " is not of type " + getType().getSimpleName());
            }
        }
        // let's be nice and box single values into arrays
        else if (getType().isArray() && getType().getComponentType().isAssignableFrom(newValue.getClass())) {
            final Object array = Array.newInstance(getType().getComponentType(), 1);
            Array.set(array, 0, newValue);
            super.setValue(array);
        }
        // This has to fail then...
        else
            throw new ClassCastException(newValue + " is not of type " + getType().getSimpleName());

    }

    @SuppressWarnings("unchecked")
    @Override
    public void setReference(final Var variable) throws ClassCastException {
        // change the type just in time before setting the reference
        if (variable != null)
            setType(variable.getType());
        super.setReference(variable);
    }
}
