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

import javax.swing.*;
import java.awt.*;

/**
 * Abstract class defining a component of an EzPlug and its behavior in the graphical user
 * interface.
 * <p>
 * An EzComponent can either be an input variable (through which users can select or input values),
 * or a graphical object allowing to produce high-quality graphical interfaces to the EzPlug.
 * <p>
 * Methods in this class provide the ability to show and hide components directly from the interface
 * using triggers from other EzComponents
 *
 * @author Alexandre Dufour
 */
public abstract class EzComponent {
    /**
     * The name of this variable (as it will appear in the interface). This field is read-only (it
     * is set once upon component creation).
     */
    public final String name;

    protected boolean visible = true;

    private EzDialog gui;

    /**
     * The group to which this component belongs, or null if this component is top-level.
     */
    private EzPanel parent = null;

    protected EzComponent(final String name) {
        this.name = name;
    }

    /**
     * @return True if this component is visible on the user interface.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the component's visibility
     *
     * @param state The new visibility state
     */
    public void setVisible(final boolean state) {
        visible = state;
    }

    void addToContainer(final Container container) {
        if (!visible)
            return;

        ThreadUtil.invoke(() -> addTo(container), !SwingUtilities.isEventDispatchThread());
    }

    protected abstract void addTo(Container container);

    /**
     * Gets a reference to the graphical interface containing this variable
     *
     * @return the GUI.
     */
    final EzDialog getUI() {
        return this.gui;
    }

    /**
     * Assign the interface which owns this component
     *
     * @param ezDialog The parent frame of this component
     */
    final void setUI(final EzDialog ezDialog) {
        this.gui = ezDialog;
    }

    /**
     * @return the panel to which this component belongs (or null if this component is top-level)
     */
    protected EzPanel getParentPanel() {
        return parent;
    }

    /**
     * Sets the parent panel of this component
     *
     * @param parent the parent of this component
     * @throws IllegalArgumentException if this component already belongs to another parent
     */
    protected void setParentPanel(final EzPanel parent) {
        if (this.parent != null && parent != null)
            throw new IllegalArgumentException("component " + name + " already belongs to group " + parent.name);
        this.parent = parent;
    }

    /**
     * Assigns a tool-tip text to the variable, which pops up when the user hovers the mouse on it.
     *
     * @param text the text to display (usually no more than 20 words)
     */
    public abstract void setToolTipText(String text);

    protected void dispose() {
        this.gui = null;
        this.parent = null;
    }
}
