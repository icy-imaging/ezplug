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

import org.bioimageanalysis.icy.model.sequence.Sequence;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarFrame;
import plugins.adufour.vars.util.VarListener;

/**
 * Graphical component letting the user select a frame number (T dimension) from a sequence
 *
 * @author Alexandre Dufour
 */
public class EzVarFrame extends EzVar<Integer> {
    private final VarListener<Sequence> listener = new VarListener<>() {
        @Override
        public void valueChanged(final Var<Sequence> source, final Sequence oldValue, final Sequence newValue) {
            if (newValue == null) {
                setVisible(false);
            }
            else {
                final int sizeT = newValue.getSizeT();
                if (sizeT == 1) {
                    setVisible(false);
                    if (variable.getReference() == null) setValue(0);
                }
                else {
                    setVisible(true);
                }
            }
        }

        @Override
        public void referenceChanged(final Var<Sequence> source, final Var<? extends Sequence> oldReference, final Var<? extends Sequence> newReference) {
            // shouldn't happen
        }
    };

    private final Var<Sequence> sequence;

    public EzVarFrame(final String name, final Var<Sequence> sequence, final boolean allowAllFrames) {
        super(new VarFrame(name, sequence, allowAllFrames), null);

        this.sequence = sequence;
        sequence.addListener(listener);

        // trigger the listener once
        listener.valueChanged(sequence, sequence.getValue(), sequence.getValue());
    }

    @Override
    protected void dispose() {
        sequence.removeListener(listener);
        super.dispose();
    }
}
