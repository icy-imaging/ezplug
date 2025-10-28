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

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

/**
 * Boolean variable
 *
 * @author Alexandre Dufour
 */
public class VarBoolean extends Var<Boolean> {
    /**
     * @param name         the variable name
     * @param defaultValue the default value
     */
    public VarBoolean(final String name, final boolean defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * @param name         the variable name
     * @param defaultValue the default value
     */
    public VarBoolean(final String name, final Boolean defaultValue) {
        this(name, defaultValue, null);
    }

    /**
     * @param name            the variable name
     * @param defaultValue    the default value
     * @param defaultListener A listener to add to this variable immediately after creation
     */
    public VarBoolean(final String name, final Boolean defaultValue, final VarListener<Boolean> defaultListener) {
        super(name, Boolean.TYPE, defaultValue, defaultListener);
    }

    @Override
    public Boolean parse(final String s) {
        return Boolean.parseBoolean(s);
    }

    /**
     * Inverts the value of this variable.<br>
     * Warning: this method does nothing if the current variable references another
     *
     * @return the variable value after the change
     */
    public Boolean toggleValue() {
        final Boolean value = getValue();

        if (getReference() != null) return value;

        final Boolean newValue = !value;

        setValue(newValue);

        return newValue;
    }

    @Override
    public VarEditor<Boolean> createVarEditor() {
        return VarEditorFactory.getDefaultFactory().createCheckBox(this);
    }
}
