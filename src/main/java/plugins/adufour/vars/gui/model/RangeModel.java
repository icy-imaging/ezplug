/*
 * Copyright (c) 2010-2026. Institut Pasteur.
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

package plugins.adufour.vars.gui.model;

import java.util.HashMap;

/**
 * Generic model used to design graphical editors for numeric variables.
 *
 * @param <N> Numeric type of the range model.
 * @author Alexandre Dufour
 * @see IntegerRangeModel
 * @see DoubleRangeModel
 * @see FloatRangeModel
 */
public abstract class RangeModel<N extends Number> implements VarEditorModel<N> {
    /**
     * Default editor is a spinner, but can be changed (see {@link RangeEditorType})
     */
    private RangeEditorType editorType = RangeEditorType.SPINNER;

    public enum RangeEditorType {
        /**
         * A spinner that allows manual value editing, and browsing through the values with small
         * arrows on the side or with the mouse wheel. This is the default component
         */
        SPINNER,
        /**
         * A slider with tick marks and a label on each side
         * <p>
         * <b>WARNING: this is experimental and labels might not display accurately!!</b>
         */
        SLIDER
    }

    protected N defaultValue, step;
    protected Comparable<N> min, max;

    protected HashMap<N, String> labels;

    public RangeModel(final N defaultValue, final Comparable<N> min, final Comparable<N> max, final N step) {
        this(defaultValue, min, max, step, RangeEditorType.SPINNER, null);
    }

    public RangeModel(final N defaultValue, final Comparable<N> min, final Comparable<N> max, final N step, final RangeEditorType editorType, final HashMap<N, String> labels) {
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.step = step;
        setEditorType(editorType);
        this.labels = labels;
    }

    @Override
    public N getDefaultValue() {
        return defaultValue;
    }

    public RangeEditorType getEditorType() {
        return editorType;
    }

    public HashMap<N, String> getLabels() {
        return labels;
    }

    /**
     * @return the number of values covered by this range, i.e., <code>(max - min) / step</code>
     */
    public abstract int getRangeSize();

    public Comparable<N> getMaximum() {
        return max;
    }

    public Comparable<N> getMinimum() {
        return min;
    }

    public N getStepSize() {
        return step;
    }

    /**
     * @param index zero-based index.
     * @return the value corresponding to the specified zero-based index, i.e.,
     * <code>min + index * step</code>
     */
    public abstract N getValueForIndex(int index);

    /**
     * Calculates the index of the specified value within the range, according to the current minimum value and step.
     *
     * @param value Value to search.
     * @return the number of steps from the minimum to the specified value
     */
    public abstract int indexOf(N value);

    public void setMaximum(final Comparable<N> maximum) {
        this.max = maximum;
    }

    public void setEditorType(final RangeEditorType editorType) {
        this.editorType = editorType;
    }

    public void setMinimum(final Comparable<N> minimum) {
        this.min = minimum;
    }

    public void setStepSize(final N step) {
        this.step = step;
    }
}