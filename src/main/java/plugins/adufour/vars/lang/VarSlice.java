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

import org.bioimageanalysis.icy.model.sequence.Sequence;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

/**
 * Special variable storing an integer representing a slice number in a sequence
 *
 * @author Alexandre Dufour
 */
public class VarSlice extends VarInteger {
    private final boolean allowAllSlices;

    private final Var<Sequence> sequence;

    /**
     * Creates a new slice index {@link Var} variable with the specified name and referenced sequence.
     *
     * @param name           The name of this variable
     * @param sequence       The sequence variable referenced by this variable.
     * @param allowAllSlices If true, the default slice is assigned to all (-1).
     *                       Otherwise, the first slice will be the default value (0).
     */
    public VarSlice(final String name, final Var<Sequence> sequence, final boolean allowAllSlices) {
        this(name, sequence, allowAllSlices, null);
    }

    /**
     * Creates a new slice index {@link Var} variable with the specified name and referenced sequence.
     * Optionally, a listener can be added to track events on the
     * variable.
     *
     * @param name            The name of this variable
     * @param sequence        The sequence variable referenced by this variable.
     * @param allowAllSlices  If true, the default slice is assigned to all (-1). Otherwise, the first slice will be the default value (0).
     * @param defaultListener A listener to add to this variable immediately after creation
     */
    public VarSlice(final String name, final Var<Sequence> sequence, final boolean allowAllSlices, final VarListener<Integer> defaultListener) {
        super(name, allowAllSlices ? -1 : 0, defaultListener);

        this.sequence = sequence;
        this.allowAllSlices = allowAllSlices;
    }

    @Override
    public VarEditor<Integer> createVarEditor() {
        return VarEditorFactory.getDefaultFactory().createSliceSelector(this, sequence, allowAllSlices);
    }
}
