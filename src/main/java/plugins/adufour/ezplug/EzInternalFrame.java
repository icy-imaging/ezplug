package plugins.adufour.ezplug;

import icy.gui.frame.IcyInternalFrame;

public class EzInternalFrame extends IcyInternalFrame {
    EzInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

    @Override
    public void doLayout() {
        if (isVisible()) super.doLayout();
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public void dispose() {
        // FIXME Memory leak: "this" is not destroyed properly.
        // the if test is needed to avoid the following bug:
        // 1) externalize the frame
        // 2) close the external frame => this dispose is called once
        // 3) close ICY => this dispose is called again => not normal !
        // => the UI doens't know about the frame anymore
        if (isVisible()) {
            getUI().uninstallUI(this);
        }
        super.dispose();
    }
}
