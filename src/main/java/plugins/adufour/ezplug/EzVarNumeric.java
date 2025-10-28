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

package plugins.adufour.ezplug;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.model.RangeModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.gui.swing.Spinner;
import plugins.adufour.vars.lang.Var;

/**
 * Superclass of all variables holding a number-type variable. This class provides generic methods
 * and graphical interface elements common to all number-type variables
 *
 * @param <N> the type of number to use in this variable.
 * @author Alexandre Dufour
 */
public abstract class EzVarNumeric<N extends Number> extends EzVar<N> {
    protected EzVarNumeric(final Var<N> variable, final VarEditorModel<N> model) {
        super(variable, model);
    }

    protected EzVarNumeric(final Var<N> variable, final N[] defaultValues, final int defaultValueIndex, final boolean allowUserInput) {
        super(variable, defaultValues, defaultValueIndex, allowUserInput);
    }

    /**
     * Gets the minimum value of this variable.
     *
     * @return The minimum value of this variable.
     * @throws UnsupportedOperationException If the graphical component of this variable is not a JSpinner object.
     */
    @SuppressWarnings("unchecked")
    public N getMinValue() {
        final VarEditorModel<N> cons = variable.getDefaultEditorModel();

        if (cons instanceof RangeModel) {
            return (N) ((RangeModel<N>) cons).getMinimum();
        }

        throw new UnsupportedOperationException("This variable is not constrained by range");
    }

    /**
     * Sets the minimum value for the JSpinner component.
     *
     * @param minValue The minimum value to set.
     * @throws UnsupportedOperationException If the graphical component of this variable is not a JSpinner object.
     */
    public void setMinValue(final Comparable<N> minValue) throws UnsupportedOperationException {
        final VarEditor<N> editor = getVarEditor();
        if (editor instanceof Spinner)
            ((Spinner<N>) editor).setMinimum(minValue);

        else
            throw new UnsupportedOperationException("Input component is not a spinner");
    }

    /**
     * Gets the step value of this variable.
     *
     * @return The step between consecutive values.
     * @throws UnsupportedOperationException If the graphical component of this variable is not a JSpinner object.
     */
    public N getStep() throws UnsupportedOperationException {
        final VarEditorModel<N> cons = variable.getDefaultEditorModel();

        if (cons instanceof RangeModel)
            return ((RangeModel<N>) cons).getStepSize();

        throw new UnsupportedOperationException("This variable is not constrained by range");
    }

    /**
     * Sets the step between consecutive values.
     *
     * @param step The step between consecutive values.
     * @throws UnsupportedOperationException If the graphical component of this variable is not a JSpinner object.
     */
    public void setStep(final N step) throws UnsupportedOperationException {
        final VarEditor<N> editor = getVarEditor();
        if (editor instanceof Spinner) {
            ((Spinner<N>) editor).setStepSize(step);
        }
        else
            throw new UnsupportedOperationException("Input component is not a spinner");
    }

    /**
     * Gets the maximum value of this variable.
     *
     * @return The maximum value of this variable.
     * @throws UnsupportedOperationException If the graphical component of this variable is not a JSpinner object.
     */
    @SuppressWarnings("unchecked")
    public N getMaxValue() {
        final VarEditorModel<N> cons = variable.getDefaultEditorModel();

        if (cons instanceof RangeModel)
            return (N) ((RangeModel<N>) cons).getMaximum();

        throw new UnsupportedOperationException("This variable is not constrained by range");
    }

    /**
     * Sets the maximum value for the JSpinner component.
     *
     * @param maxValue The maximum value to set.
     * @throws UnsupportedOperationException If the graphical component of this variable is not a JSpinner object.
     */
    public void setMaxValue(final Comparable<N> maxValue) {
        final VarEditor<N> editor = getVarEditor();
        if (editor instanceof Spinner) {
            ((Spinner<N>) editor).setMaximum(maxValue);
        }
        else
            throw new UnsupportedOperationException("Input component is not a spinner");
    }

    /**
     * Sets the news value of the variable. This method only works if the component is a JSpinner
     * object.
     *
     * @param value The new value.
     * @param max   The new max bound.
     * @param min   The new min bound.
     * @param step  The new step.
     * @throws UnsupportedOperationException If the graphical component of this variable is not a JSpinner object.
     */
    public void setValues(final N value, final Comparable<N> min, final Comparable<N> max, final N step) {
        final VarEditor<N> editor = getVarEditor();
        if (editor instanceof Spinner) {
            ((Spinner<N>) editor).setMinimum(min);
            ((Spinner<N>) editor).setMaximum(max);
            ((Spinner<N>) editor).setStepSize(step);
            setValue(value);
        }
        else
            throw new UnsupportedOperationException("Input component is not a spinner object");
    }
}