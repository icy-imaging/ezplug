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

import org.bioimageanalysis.icy.model.sequence.Sequence;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

/**
 * Special variable storing an integer representing a frame number in a sequence
 *
 * @author Alexandre Dufour
 */
public class VarFrame extends VarInteger {
    private final boolean allowAllFrames;

    private final Var<Sequence> sequence;

    /**
     * Creates a new frame {@link Var} variable of a given sequence and with the specified name.
     *
     * @param name           The name of this variable.
     * @param sequence       The sequence from which frames are selected.
     * @param allowAllFrames If true, all frames can be selected (default value).
     *                       The first frame is the default value otherwise.
     */
    public VarFrame(final String name, final Var<Sequence> sequence, final boolean allowAllFrames) {
        this(name, sequence, allowAllFrames, null);
    }

    /**
     * Creates a new frame {@link Var} variable of a given sequence and with the specified name.
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable.
     * @param sequence        The sequence from which frames are selected.
     * @param allowAllFrames  If true, all frames can be selected (default value).
     *                        The first frame is the default value otherwise.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarFrame(final String name, final Var<Sequence> sequence, final boolean allowAllFrames, final VarListener<Integer> defaultListener) {
        super(name, allowAllFrames ? -1 : 0, defaultListener);

        this.sequence = sequence;
        this.allowAllFrames = allowAllFrames;
    }

    @Override
    public VarEditor<Integer> createVarEditor() {
        return VarEditorFactory.getDefaultFactory().createFrameSelector(this, sequence, allowAllFrames);
    }
}
