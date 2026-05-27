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

import fr.icy.extension.kernel.roi.descriptor.measure.ROIInteriorDescriptor;
import fr.icy.common.string.StringUtil;
import fr.icy.model.roi.ROIDescriptor;
import plugins.adufour.vars.gui.model.ValueSelectionModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * EzVar representing {@link ROIDescriptor} selector
 *
 * @author Stephane.D
 */
public class VarROIDescriptor extends VarString {
    final List<ROIDescriptor<?>> roiDescriptors;

    /**
     * @param name Variable name
     */
    public VarROIDescriptor(final String name) {
        super(name, "");

        roiDescriptors = new ArrayList<>(ROIDescriptor.getDescriptors().keySet());
        // build list of descriptor id
        final List<String> descriptorsId = new ArrayList<>();
        String sizeDescriptorId = null;

        for (final ROIDescriptor<?> descriptor : roiDescriptors) {
            final String id = descriptor.getId();

            // keep trace of size descriptor
            if (StringUtil.equals(ROIInteriorDescriptor.ID, id))
                sizeDescriptorId = id;

            descriptorsId.add(id);
        }

        // alpha sort
        Collections.sort(descriptorsId);

        // initialize descriptors field
        setDefaultEditorModel(new ValueSelectionModel<>(descriptorsId.toArray(new String[0]), sizeDescriptorId, false));
    }

    public VarROIDescriptor() {
        this("Descriptor");
    }

    public List<ROIDescriptor<?>> getAllDescriptors() {
        return roiDescriptors;
    }
}
