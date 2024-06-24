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

import org.bioimageanalysis.icy.gui.frame.IcyInternalFrame;

public class EzInternalFrame extends IcyInternalFrame {
    EzInternalFrame(final String title, final boolean resizable, final boolean closable, final boolean maximizable, final boolean iconifiable) {
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
        // => the UI doesn't know about the frame anymore
        if (isVisible()) {
            getUI().uninstallUI(this);
        }
        super.dispose();
    }
}
