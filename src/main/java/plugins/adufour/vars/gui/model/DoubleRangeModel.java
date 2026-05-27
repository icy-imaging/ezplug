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

public class DoubleRangeModel extends RangeModel<Double> {
    public DoubleRangeModel(final Double defaultValue, final Double min, final Double max, final Double step) {
        this(defaultValue, min, max, step, RangeEditorType.SPINNER, null);
    }

    public DoubleRangeModel(final Double defaultValue, final Double min, final Double max, final Double step, final RangeEditorType editorType, final HashMap<Double, String> labels) {
        super(defaultValue, min, max, step, editorType, labels);
    }

    @Override
    public boolean isValid(final Double value) {
        return min.compareTo(value) <= 0 && max.compareTo(value) >= 0 && value % step == 0;
    }

    @Override
    public int getRangeSize() {
        return (int) Math.round((((Double) max) - ((Double) min)) / step);
    }

    @Override
    public Double getValueForIndex(final int index) {
        return ((Double) min) + index * step;
    }

    @Override
    public int indexOf(final Double value) {
        return (int) Math.round((value - (Double) min) / step);
    }
}