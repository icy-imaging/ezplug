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

package plugins.adufour.ezplug;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * GUI component for EzPlug that displays a number of tab pages.
 * <p>
 * Developer note: this component is backed by a {@link JTabbedPane}
 *
 * @author Alexandre Dufour
 */
public class EzTabs extends EzPanel {
    public enum TabPlacement {
        TOP(JTabbedPane.TOP), BOTTOM(JTabbedPane.BOTTOM), LEFT(JTabbedPane.LEFT), RIGHT(JTabbedPane.RIGHT);

        final int tabPlacement;

        TabPlacement(final int tabPlacement) {
            this.tabPlacement = tabPlacement;
        }
    }

    /**
     * Create a new component to store tab pages.
     *
     * @param uniqueID     A unique identifier. This identifier will not be shown on screen, but is necessary
     *                     to store and load user parameters.
     * @param tabPlacement The placement for the tabs relative to the content.
     */
    public EzTabs(final String uniqueID, final @NotNull TabPlacement tabPlacement) {
        super(uniqueID);

        ((JTabbedPane) container).setTabPlacement(tabPlacement.tabPlacement);
    }

    @Override
    protected JComponent createContainer() {
        return new JTabbedPane();
    }

    @Override
    protected void buildPanel() {
        final JTabbedPane tabs = (JTabbedPane) container;

        final int currentTab = tabs.getSelectedIndex();

        container.removeAll();

        for (final EzComponent tab : this)
            if (tab.isVisible()) {
                final JPanel tabPanel = new JPanel(new GridBagLayout());
                tab.addTo(tabPanel);
                tabs.addTab(tab.name, tabPanel);
            }

        if (currentTab >= 0 && currentTab < tabs.getTabCount()) {
            tabs.setSelectedIndex(currentTab);
        }
    }
}
