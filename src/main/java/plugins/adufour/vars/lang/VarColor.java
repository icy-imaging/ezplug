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

import org.w3c.dom.Node;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

import java.awt.*;

public class VarColor extends Var<Color> {
    /**
     * Creates a new variable holding a OME-compatible {@link Color} object
     *
     * @param name         the name of this variable
     * @param defaultColor the non-null default color value of this variable
     */
    public VarColor(final String name, final Color defaultColor) {
        this(name, defaultColor, null);
    }

    /**
     * Creates a new variable holding {@link Color} object
     *
     * @param name            the name of this variable
     * @param defaultColor    the non-null default color value of this variable
     * @param defaultListener the default listener to add to this variable immediately after creation
     */
    public VarColor(final String name, final Color defaultColor, final VarListener<Color> defaultListener) {
        super(name, defaultColor, defaultListener);
    }

    @Override
    public VarEditor<Color> createVarEditor() {
        return VarEditorFactory.getDefaultFactory().createColorChooser(this);
    }

    @Override
    public boolean saveToXML(final Node node) throws UnsupportedOperationException {
        return super.saveToXML(node);
    }

    @Override
    public Color parse(String text) {
        if (text == null)
            return Color.black;

        // text should look like:
        // java.awt.Color[r=0,g=255,b=255]
        text = text.substring(text.indexOf('[') + 1, text.indexOf(']'));
        final String[] rgb = text.split(",");
        final int r = Integer.parseInt(rgb[0].substring(rgb[0].indexOf('=') + 1));
        final int g = Integer.parseInt(rgb[1].substring(rgb[1].indexOf('=') + 1));
        final int b = Integer.parseInt(rgb[2].substring(rgb[2].indexOf('=') + 1));
        return new Color(r, g, b);
    }
}
