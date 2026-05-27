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

package plugins.adufour.vars.lang;

import fr.icy.model.sequence.Sequence;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

public class VarSequenceArray extends VarGenericArray<Sequence[]> {
    /**
     * Creates a new sequence array {@link Var} variable with the specified name.
     * By default, the array is initialized to size 0.
     *
     * @param name The name of this variable.
     */
    public VarSequenceArray(final String name) {
        this(name, (VarListener<Sequence[]>) null);
    }

    /**
     * Creates a new sequence array {@link Var} variable with the specified name.
     * By default, the array is initialized to size 0.
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarSequenceArray(final String name, final VarListener<Sequence[]> defaultListener) {
        super(name, Sequence[].class, new Sequence[0], defaultListener);
    }

    /**
     * Creates a new sequence array {@link Var} variable with the specified name and holding the given sequences.
     *
     * @param name      The name of this variable.
     * @param sequences The sequence array assigned by default for this variable. If null, a new sequence array of size zero is assigned as default value.
     */
    public VarSequenceArray(final String name, final Sequence... sequences) {
        super(name, Sequence[].class, sequences != null ? sequences : new Sequence[0]);
    }

    @Override
    public VarEditor<Sequence[]> createVarEditor() {
        return VarEditorFactory.getDefaultFactory().createSequenceList(this);
    }
}
