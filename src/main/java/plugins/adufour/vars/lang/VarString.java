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

import org.w3c.dom.Node;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.gui.model.PasswordModel;
import plugins.adufour.vars.util.VarListener;

public class VarString extends Var<String> {
    private final int nbLines;

    /**
     * Creates a new text {@link Var} variable with the specified name and default value (can be null),
     * with a default single-line text field editor.
     *
     * @param name         The name of this variable (<b>not</b> the actual text to store and/or display).
     * @param defaultValue the initial text to store and/or display.
     */
    public VarString(final String name, final String defaultValue) {
        this(name, defaultValue, 1, null);
    }

    /**
     * Creates a new text {@link Var} variable with the specified name and default value (can be null),
     * with a default single-line text field editor.
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable (*not* the actual text to store and/or display).
     * @param defaultValue    the initial text to store and/or display.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarString(final String name, final String defaultValue, final VarListener<String> defaultListener) {
        this(name, defaultValue, 1, null);
    }

    /**
     * Creates a new text {@link Var} variable with the specified name and default value (can be null),
     * with a default text field editor.
     *
     * @param name         The name of this variable (*not* the actual text to store and/or display).
     * @param defaultValue the initial text to store and/or display.
     * @param nbLines      The number of lines allowed for this text variable (used only to create the graphical interface).
     */
    public VarString(final String name, final String defaultValue, final int nbLines) {
        this(name, defaultValue, nbLines, null);
    }

    /**
     * Creates a new text {@link Var} variable with the specified name and default value (can be null),
     * with a default text field editor.
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable (*not* the actual text to store and/or display).
     * @param defaultValue    the initial text to store and/or display.
     * @param nbLines         The number of lines allowed for this text variable (used only to create the graphical interface).
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarString(final String name, final String defaultValue, final int nbLines, final VarListener<String> defaultListener) {
        super(name, String.class, defaultValue, defaultListener);
        this.nbLines = nbLines;
    }

    @Override
    public String parse(final String s) {
        return s;
    }

    @Override
    public VarEditor<String> createVarEditor() {
        final VarEditorFactory factory = VarEditorFactory.getDefaultFactory();

        if (getDefaultEditorModel() == null)
            return nbLines > 1 ? factory.createTextArea(this, nbLines) : factory.createTextField(this);

        if (getDefaultEditorModel() instanceof PasswordModel)
            return factory.createPasswordField(this);

        return super.createVarEditor();
    }

    @Override
    public String getValueAsString() {
        // don't show passwords
        if (getDefaultEditorModel() instanceof PasswordModel)
            return "";

        return super.getValueAsString();
    }

    @Override
    public boolean saveToXML(final Node node) throws UnsupportedOperationException {
        // don't save passwords
        if (getDefaultEditorModel() instanceof PasswordModel)
            return true;

        return super.saveToXML(node);
    }
}
