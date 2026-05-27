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

package plugins.adufour.vars.gui.model;

import fr.icy.model.roi.ROI;
import fr.icy.model.sequence.Sequence;

import java.io.File;

public class TypeSelectionModel extends ValueSelectionModel<Class<?>> {
    /**
     * Create a new type selection model with the most popular types.
     */
    public TypeSelectionModel() {
        super(new Class<?>[]{
                Object.class,
                Sequence.class,
                Integer.class,
                Double.class,
                Boolean.class,
                int[].class,
                double[].class,
                String.class,
                File.class,
                File[].class,
                ROI[].class
        });
    }

    /**
     * Creates a new custom type selection model with the specified types.
     *
     * @param classes Types to be included in the selection model.
     */
    public TypeSelectionModel(final Class<?>... classes) {
        super(classes);
    }
}
