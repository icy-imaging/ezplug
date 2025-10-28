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

package plugins.adufour.vars.gui.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Variable constraint that only allows to choose from a specified set of values.
 *
 * @param <T> Value type of the selection model.
 * @author Alexandre Dufour
 */
public class ValueSelectionModel<T> implements VarEditorModel<T> {
    protected final List<T> validValues;

    protected final T defaultValue;

    private final boolean freeInput;

    public ValueSelectionModel() {
        this(null);
    }

    public ValueSelectionModel(final T[] validValues) {
        this(validValues, 0, validValues == null || validValues.length == 0);
    }

    /**
     * @param validValues       A list of valid values.
     * @param defaultValueIndex The index of the default selected value.
     * @param freeInput         Set to true if any valid outside the list is valid (no check will be performed).
     */
    public ValueSelectionModel(final T[] validValues, final int defaultValueIndex, final boolean freeInput) {
        this.validValues = (validValues == null) ? new ArrayList<>() : Arrays.asList(validValues);
        this.defaultValue = (this.validValues.size() > defaultValueIndex) ? this.validValues.get(defaultValueIndex) : null;
        this.freeInput = freeInput;
    }

    /**
     * @param validValues  A list of valid values.
     * @param defaultValue The default selected value.
     * @param freeInput    Set to true if any valid outside the list is valid (no check will be performed).
     */
    public ValueSelectionModel(final T[] validValues, final T defaultValue, final boolean freeInput) {
        this.validValues = (validValues == null) ? new ArrayList<>() : Arrays.asList(validValues);
        this.defaultValue = (this.validValues.contains(defaultValue) ? defaultValue : null);
        this.freeInput = freeInput;
    }

    @Override
    public boolean isValid(final T value) {
        return freeInput || validValues.contains(value);
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    public List<T> getValidValues() {
        return validValues;
    }

    public boolean isFreeInput() {
        return freeInput;
    }
}
