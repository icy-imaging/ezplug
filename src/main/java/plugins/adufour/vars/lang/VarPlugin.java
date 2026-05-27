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

import fr.icy.extension.ExtensionLoader;
import fr.icy.extension.plugin.PluginDescriptor;
import fr.icy.extension.plugin.abstract_.Plugin;
import fr.icy.system.logging.IcyLogger;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

import java.lang.reflect.InvocationTargetException;

/**
 * Variable that manipulates a plug-in definition
 *
 * @author Alexandre Dufour
 */
public class VarPlugin<P extends Plugin> extends Var<PluginDescriptor> {
    public final Class<P> pluginType;

    /**
     * Creates a new {@link Var} variable with the specified name and plugin type.
     *
     * @param name       The name of this variable.
     * @param pluginType The type of plugins accepted by this variable.
     */
    public VarPlugin(final String name, final Class<P> pluginType) {
        this(name, pluginType, null);
    }

    /**
     * Creates a new plugin {@link Var} variable with the specified name and plugin type.
     * Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable.
     * @param pluginType      The type of plugins accepted by this variable.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarPlugin(final String name, final Class<P> pluginType, final VarListener<PluginDescriptor> defaultListener) {
        super(name, PluginDescriptor.class, null, defaultListener);
        this.pluginType = pluginType;
    }

    @Override
    public VarEditor<PluginDescriptor> createVarEditor() {
        return VarEditorFactory.getDefaultFactory().createPluginChooser(this);
    }

    @SuppressWarnings("unchecked")
    public P newInstance() {
        try {
            return ((Class<P>) getValue(true).getPluginClass()).getDeclaredConstructor().newInstance();
        }
        catch (final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            IcyLogger.error(VarPlugin.class, e, e.getLocalizedMessage());
        }

        return null;
    }

    @Override
    public String getValueAsString() {
        return getValue().getClassName();
    }

    @Override
    public PluginDescriptor parse(final String text) {
        return ExtensionLoader.getPlugin(text);
    }
}
