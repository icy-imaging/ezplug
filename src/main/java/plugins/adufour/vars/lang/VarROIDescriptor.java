package plugins.adufour.vars.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import icy.roi.ROIDescriptor;
import icy.util.StringUtil;
import plugins.adufour.vars.gui.model.ValueSelectionModel;
import plugins.kernel.roi.descriptor.measure.ROIInteriorDescriptor;

/**
 * EzVar representing {@link ROIDescriptor} selector
 *
 * @author Stephane.D
 */
public class VarROIDescriptor extends VarString {
    final List<ROIDescriptor> roiDescriptors;

    /**
     * @param name Variable name
     */
    public VarROIDescriptor(String name) {
        super(name, "");

        roiDescriptors = new ArrayList<>(ROIDescriptor.getDescriptors().keySet());
        // build list of descriptor id
        final List<String> descriptorsId = new ArrayList<>();
        String sizeDescriptorId = null;

        for (ROIDescriptor descriptor : roiDescriptors) {
            final String id = descriptor.getId();

            // keep trace of size descriptor
            if (StringUtil.equals(ROIInteriorDescriptor.ID, id))
                sizeDescriptorId = id;

            descriptorsId.add(id);
        }

        // alpha sort
        Collections.sort(descriptorsId);

        // initialize descriptors field
        setDefaultEditorModel(new ValueSelectionModel<>(descriptorsId.toArray(new String[descriptorsId.size()]),
                sizeDescriptorId, false));
    }

    public VarROIDescriptor() {
        this("Descriptor");
    }

    public List<ROIDescriptor> getAllDescriptors() {
        return roiDescriptors;
    }
}
