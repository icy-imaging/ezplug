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

package plugins.adufour.ezplug;

import org.bioimageanalysis.icy.system.thread.ThreadUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Class defining a generic panel containing multiple {@link EzComponent} objects. For two concrete
 * examples of this class, see {@link EzGroup} and {@link EzTabs}
 *
 * @author Alexandre Dufour
 */
public class EzPanel extends EzComponent implements Iterable<EzComponent> {
    private class EzComponentList extends ArrayList<EzComponent> {
        public EzComponentList(final int initialSize) {
            super(initialSize);
        }

        @Override
        public boolean add(final @NotNull EzComponent e) {
            e.setParentPanel(EzPanel.this);
            return super.add(e);
        }

        @Override
        public void add(final int index, final @NotNull EzComponent element) {
            element.setParentPanel(EzPanel.this);
            super.add(index, element);
        }

        @Override
        public boolean addAll(final @NotNull Collection<? extends EzComponent> c) {
            for (final EzComponent comp : c)
                comp.setParentPanel(EzPanel.this);
            return super.addAll(c);
        }

        @Override
        public boolean addAll(final int index, final @NotNull Collection<? extends EzComponent> c) {
            for (final EzComponent comp : c)
                comp.setParentPanel(EzPanel.this);
            return super.addAll(index, c);
        }

        @Override
        public void clear() {
            for (final EzComponent comp : this)
                comp.setParentPanel(null);
            super.clear();
        }

        @Override
        public EzComponent remove(final int index) {
            final EzComponent oldElement = super.remove(index);
            if (oldElement != null) oldElement.setParentPanel(null);
            return oldElement;
        }

        @Override
        protected void removeRange(final int fromIndex, final int toIndex) {
            for (int i = fromIndex; i < toIndex; i++) {
                if (get(i) != null) get(i).setParentPanel(null);
            }
            super.removeRange(fromIndex, toIndex);
        }

        @Override
        public EzComponent set(final int index, final @NotNull EzComponent element) {
            element.setParentPanel(EzPanel.this);
            final EzComponent oldElement = super.set(index, element);
            if (oldElement != null) oldElement.setParentPanel(null);
            return oldElement;
        }
    }

    private final List<EzComponent> components;

    protected JComponent container;

    protected String toolTipText = "";

    public EzPanel(final String name, final EzComponent @NotNull ... ezComponents) {
        super(name);

        ThreadUtil.invoke(() -> container = createContainer(), !SwingUtilities.isEventDispatchThread());

        this.components = new EzComponentList(ezComponents.length);

        add(ezComponents);
    }

    protected JComponent createContainer() {
        return new JPanel(new GridBagLayout());
    }

    /**
     * Adds the specified EzComponents to this panel
     *
     * @param ezComponents the components to add
     */
    public void add(final EzComponent... ezComponents) {
        if (ezComponents == null || ezComponents.length == 0) return;

        components.addAll(Arrays.asList(ezComponents));
    }

    @Override
    public @NotNull Iterator<EzComponent> iterator() {
        return components.iterator();
    }

    protected void buildPanel() {
        container.removeAll();
        for (final EzComponent ezComponent : components)
            if (ezComponent.isVisible()) ezComponent.addTo(container);
    }

    @Override
    protected void addTo(final @NotNull Container parentContainer) {
        buildPanel();

        final GridBagLayout gridbag = (GridBagLayout) parentContainer.getLayout();

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gridbag.setConstraints(this.container, gbc);

        parentContainer.add(this.container);
    }

    public String getToolTipText() {
        return toolTipText;
    }

    @Override
    public void setToolTipText(final String text) {
        this.toolTipText = text;
    }

    @Override
    protected void dispose() {
        for (final EzComponent ezComponent : components)
            ezComponent.dispose();
        components.clear();

        super.dispose();
    }
}
