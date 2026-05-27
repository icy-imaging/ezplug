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

import org.jetbrains.annotations.NotNull;

public class IntegerRangeModel extends RangeModel<Integer> {
    public IntegerRangeModel(final Integer defaultValue, final Integer min, final Integer max, final Integer step) {
        this(defaultValue, min, max, step, RangeEditorType.SPINNER);
    }

    public IntegerRangeModel(final Integer defaultValue, final Integer min, final Integer max, final Integer step, final RangeEditorType editorType) {
        super(defaultValue, min, max, step, editorType, null);
    }

    @Override
    public boolean isValid(final Integer value) {
        return min.compareTo(value) <= 0 && max.compareTo(value) >= 0 && (value % step == 0);
    }

    @Override
    public int getRangeSize() {
        return (((Integer) max) - ((Integer) min)) / step;
    }

    @Override
    public Integer getValueForIndex(final int index) {
        return ((Integer) min) + index * step;
    }

    @Override
    public int indexOf(final @NotNull Integer value) {
        return Math.round((value.floatValue() - (Integer) min) / step);
    }
}