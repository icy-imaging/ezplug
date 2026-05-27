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

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

/**
 * Variable providing a trigger (i.e., a button in the graphical interface) and the number of times
 * it was triggered
 *
 * @author Alexandre Dufour
 */
public class VarTrigger extends VarInteger {
    public interface TriggerListener extends VarListener<Integer> {
        void triggered(VarTrigger source);
    }

    /**
     * Creates a new trigger with the given name
     *
     * @param name      the name of the trigger (will be the title of the button in graphical mode)
     * @param listeners the trigger listeners
     */
    public VarTrigger(final String name, final TriggerListener... listeners) {
        super(name, 0);
        if (listeners != null) {
            for (final TriggerListener listener : listeners)
                addListener(listener);
        }
    }

    @Override
    public VarEditor<Integer> createVarEditor() {
        return VarEditorFactory.getDefaultFactory().createButton(this);
    }

    /**
     * Resets the trigger count (equivalent to setting the value of this variable to 0)
     */
    public void reset() {
        setValue(0);
    }

    /**
     * Triggers the variable (equivalent to incrementing the value of this variable)
     */
    public void trigger() {
        super.setValue(getValue() + 1);

        // Fire trigger listeners
        for (final VarListener<Integer> listener : listeners)
            if (listener instanceof TriggerListener) ((TriggerListener) listener).triggered(this);
    }
}
