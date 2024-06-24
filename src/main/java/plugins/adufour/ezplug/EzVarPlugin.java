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

package plugins.adufour.ezplug;

import org.bioimageanalysis.icy.extension.plugin.PluginDescriptor;
import org.bioimageanalysis.icy.extension.plugin.abstract_.Plugin;
import plugins.adufour.vars.lang.VarPlugin;

/**
 * New variable displaying a plugin list of a given type.
 *
 * @param <P> Type of plugin supported by the variable.
 * @author Alexandre Dufour
 */
public class EzVarPlugin<P extends Plugin> extends EzVar<PluginDescriptor> {
    public EzVarPlugin(final String name, final Class<P> pluginType) {
        super(new VarPlugin<>(name, pluginType), null);
    }

    public P newInstance() {
        return ((VarPlugin<P>) variable).newInstance();
    }
}
