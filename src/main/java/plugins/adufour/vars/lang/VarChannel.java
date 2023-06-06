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

import icy.sequence.Sequence;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

public class VarChannel extends VarInteger {
    private final boolean allowAllChannels;

    private final Var<Sequence> sequence;

    /**
     * @param name             The name of this variable
     * @param sequence         The sequence referenced by this variable
     * @param allowAllChannels If true, the default value will be all channels. Zero otherwise
     */
    public VarChannel(final String name, final Var<Sequence> sequence, final boolean allowAllChannels) {
        this(name, sequence, allowAllChannels, null);
    }

    /**
     * @param name             The name of this variable
     * @param sequence         The sequence referenced by this variable
     * @param allowAllChannels If true, the default value will be all channels. Zero otherwise
     * @param defaultListener  A listener to add to this variable immediately after creation
     */
    public VarChannel(final String name, final Var<Sequence> sequence, final boolean allowAllChannels, final VarListener<Integer> defaultListener) {
        super(name, allowAllChannels ? -1 : 0, defaultListener);

        this.sequence = sequence;
        this.allowAllChannels = allowAllChannels;
    }

    @Override
    public VarEditor<Integer> createVarEditor() {
        return VarEditorFactory.getDefaultFactory().createChannelSelector(this, sequence, allowAllChannels);
    }
}
