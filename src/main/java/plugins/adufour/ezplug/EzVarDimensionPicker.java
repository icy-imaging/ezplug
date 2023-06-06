/*
 * Copyright (c) 2010-2023. Institut Pasteur.
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

package plugins.adufour.ezplug;

import icy.sequence.DimensionId;
import icy.sequence.Sequence;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.util.VarListener;

public class EzVarDimensionPicker extends EzVarInteger {
    private final boolean active;

    private final class SequenceChangedListener implements VarListener<Sequence> {
        @Override
        public void valueChanged(final Var<Sequence> source, final Sequence oldValue, final Sequence newValue) {

            if (newValue == null) {
                setVisible(false);
                setValue(0);
            }
            else {
                final int currentValue = getValue();
                // set an invalid value
                setValue(-1);
                final int size = getSize(newValue, dim);
                setVisible(size > 1);
                setMaxValue(size - 1);
                // (re)set the current value to force an interface refresh
                setValue(currentValue >= size ? 0 : currentValue);
            }
        }

        @Override
        public void referenceChanged(final Var<Sequence> source, final Var<? extends Sequence> oldReference, final Var<? extends Sequence> newReference) {
        }
    }

    final Var<Sequence> s;
    final DimensionId dim;
    final SequenceChangedListener listener;

    /**
     * Constructs a new selector for the specified dimension. By default, <code>-1</code> is not a
     * valid value.
     *
     * @param varName  the variable name
     * @param dim      the dimension to create a selector for
     * @param sequence the sequence variable to listen to
     */
    public EzVarDimensionPicker(final String varName, final DimensionId dim, final EzVarSequence sequence) {
        this(varName, dim, sequence.variable);
    }

    /**
     * Constructs a new selector for the specified dimension
     *
     * @param varName  the variable name
     * @param dim      the dimension to create a selector for
     * @param sequence the sequence variable to listen to
     */
    public EzVarDimensionPicker(final String varName, final DimensionId dim, final Var<Sequence> sequence) {
        this(varName, dim, sequence, false);
    }

    /**
     * Constructs a new selector for the specified dimension
     *
     * @param varName  the variable name
     * @param dim      the dimension to create a selector for
     * @param sequence the sequence variable to listen to
     * @param allowAll true if <code>-1</code> is a valid value, i.e. all values are selected (e.g. -1
     *                 for dimension C indicates that all channels should be processed). If false, the
     *                 minimum selector value will be <code>0</code>
     */
    public EzVarDimensionPicker(final String varName, final DimensionId dim, final Var<Sequence> sequence, final boolean allowAll) {
        super(varName, allowAll ? -1 : 0, allowAll ? -1 : 0, 0, 1);
        if (allowAll)
            setToolTipText("Choose -1 to select all values");
        s = sequence;
        this.dim = dim;
        s.addListener(listener = new SequenceChangedListener());
        listener.valueChanged(sequence, null, sequence.getValue());
        this.active = true;
    }

    private static int getSize(final Sequence s, final DimensionId dim) {
        switch (dim) {
            case X:
                return s.getSizeX();
            case Y:
                return s.getSizeY();
            case Z:
                return s.getSizeZ();
            case T:
                return s.getSizeT();
            case C:
                return s.getSizeC();
            default:
                throw new IllegalArgumentException("Unknown sequence dimension: " + dim);
        }
    }

    @Override
    protected void dispose() {
        s.removeListener(listener);
        super.dispose();
    }

    /**
     * Sets the active state of the picker (true by default). If the picker is not active, the
     * bounds will no longer depend on the attached sequence and may be modified, e.g., via the
     * {@link EzVarNumeric#setValues(Number, Comparable, Comparable, Number)} method.
     *
     * @param active The active state of the picker.
     */
    public void setActive(final boolean active) {
        if (this.active == active)
            return;

        if (active) {
            s.addListener(listener);
        }
        else {
            s.removeListener(listener);
        }
    }
}
