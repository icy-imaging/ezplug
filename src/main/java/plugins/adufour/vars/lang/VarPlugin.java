package plugins.adufour.vars.lang;

import icy.plugin.PluginDescriptor;
import icy.plugin.PluginLoader;
import icy.plugin.abstract_.Plugin;
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
     * Creates a new {@link Var}iable with the specified name and plugin type.
     *
     * @param name       The name of this variable.
     * @param pluginType The type of plugins accepted by this variable.
     */
    public VarPlugin(String name, Class<P> pluginType) {
        this(name, pluginType, null);
    }

    /**
     * Creates a new plugin {@link Var}iable with the specified name and plugin type. Optionally, a listener can be added to track events on the variable.
     *
     * @param name            The name of this variable.
     * @param pluginType      The type of plugins accepted by this variable.
     * @param defaultListener A listener to add to this variable immediately after creation.
     */
    public VarPlugin(String name, Class<P> pluginType, VarListener<PluginDescriptor> defaultListener) {
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
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getValueAsString() {
        return getValue().getClassName();
    }

    @Override
    public PluginDescriptor parse(String text) {
        return PluginLoader.getPlugin(text);
    }
}
