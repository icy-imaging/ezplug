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

package plugins.adufour.vars.gui.swing;

import fr.icy.model.sequence.DimensionId;
import fr.icy.model.sequence.Sequence;
import org.jetbrains.annotations.NotNull;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarChannel;

import java.text.ParseException;

public class ChannelSelector extends SequenceDimensionSelector {
    public ChannelSelector(final VarChannel variable, final Var<Sequence> sequence, final boolean allowAllChannels) {
        super(variable, sequence, DimensionId.C, allowAllChannels);
    }

    @Override
    protected String toUserFriendlyString(final @NotNull Integer value) {
        return value.toString() + " (" + sequence.getValue().getChannelName(value) + ")";
    }

    @Override
    protected Integer toInteger(String text) throws ParseException {
        final Sequence s = sequence.getValue();

        text = text.substring(text.indexOf("(") + 1);
        text = text.substring(0, text.length() - 1);
        for (int c = 0; c < s.getSizeC(); c++)
            if (s.getChannelName(c).equalsIgnoreCase(text)) return c;

        throw new ParseException("Channel " + text + " does not exist", 0);
    }

}
