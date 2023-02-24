package plugins.adufour.ezplug;

import icy.plugin.PluginDescriptor;
import icy.plugin.abstract_.Plugin;
import plugins.adufour.vars.lang.VarPlugin;

/**
 * New variable displaying a list of plug-ins of a given type.
 *
 * @param <P> Type of plugin supported by the variable.
 * @author Alexandre Dufour
 */
public class EzVarPlugin<P extends Plugin> extends EzVar<PluginDescriptor> {
    public EzVarPlugin(String name, Class<P> pluginType) {
        super(new VarPlugin<>(name, pluginType), null);
    }

    public P newInstance() {
        return ((VarPlugin<P>) variable).newInstance();
    }
}
