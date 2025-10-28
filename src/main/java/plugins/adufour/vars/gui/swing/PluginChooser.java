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

package plugins.adufour.vars.gui.swing;

import org.bioimageanalysis.icy.extension.ExtensionLoader;
import org.bioimageanalysis.icy.extension.plugin.PluginDescriptor;
import org.bioimageanalysis.icy.extension.plugin.abstract_.Plugin;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarPlugin;

import javax.swing.*;
import javax.swing.event.ListDataListener;

public class PluginChooser<P extends Plugin> extends SwingVarEditor<PluginDescriptor> {
    public PluginChooser(final Var<PluginDescriptor> variable) {
        super(variable);
    }

    @Override
    protected JComponent createEditorComponent() {
        final JComboBox<PluginDescriptor> combo = new JComboBox<>();

        combo.setModel(new ComboBoxModel<>() {
            @Override
            public void removeListDataListener(final ListDataListener l) {
            }

            @Override
            public int getSize() {
                final Class<P> pluginType = ((VarPlugin<P>) variable).pluginType;
                return ExtensionLoader.getPlugins(pluginType).size();
            }

            @Override
            public PluginDescriptor getElementAt(final int index) {
                final Class<P> pluginType = ((VarPlugin<P>) variable).pluginType;
                //return ExtensionLoader.getPlugins(pluginType).get(index);
                return ExtensionLoader.getPlugins(pluginType).toArray(new PluginDescriptor[0])[index];
            }

            @Override
            public void addListDataListener(final ListDataListener l) {
            }

            @Override
            public void setSelectedItem(final Object anItem) {
                variable.setValue((PluginDescriptor) anItem);
            }

            @Override
            public PluginDescriptor getSelectedItem() {
                return variable.getValue();
            }
        });

        if (combo.getModel().getSize() > 1) combo.setSelectedIndex(0);

        combo.setRenderer((list, descriptor, index, isSelected, cellHasFocus) -> {
            if (descriptor == null) return new JLabel("no selection");
            final JLabel header = new JLabel(descriptor.getName());
            header.setToolTipText(descriptor.getDescription());
            return header;
        });

        return combo;
    }

    @Override
    protected void activateListeners() {

    }

    @Override
    protected void deactivateListeners() {

    }

    @Override
    protected void updateInterfaceValue() {

    }
}
