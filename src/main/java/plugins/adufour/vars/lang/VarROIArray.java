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

package plugins.adufour.vars.lang;

import org.bioimageanalysis.icy.model.roi.ROI;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.util.VarListener;

public class VarROIArray extends VarArray<ROI> {
    /**
     * Creates a new ROI array {@link Var} variable with the specified name and default array of size 0.
     *
     * @param name The name of this variable.
     */
    public VarROIArray(final String name) {
        this(name, null);
    }

    /**
     * Creates a new ROI array {@link Var} variable with the specified name and default array of size 0.
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarROIArray(final String name, final VarListener<ROI[]> defaultListener) {
        super(name, ROI[].class, new ROI[0], defaultListener);
    }

    @Override
    public VarEditor<ROI[]> createVarEditor() {
        return super.createVarViewer();
    }

    /**
     * @return a pretty-printed text representation of the variable's local value (referenced
     * variables are <b>not</b> followed).
     * This text is used to display the value (e.g., in a graphical interface) or store the value into XML files.
     * Overriding implementations should make sure that the result of this method is compatible with the
     * {@link #parse(String)} method to ensure proper reloading from XML files.
     */
    @Override
    public String getValueAsString() {
        final ROI[] value = getValue();

        if (value == null || value.length == 0)
            return "No ROI";

        return value.length + " ROI";
    }

    @Override
    public ROI[] parse(final String input) {
        return null;
    }
}
