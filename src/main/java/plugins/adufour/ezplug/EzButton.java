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

import icy.system.thread.ThreadUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class defining a button for use with EzPlugs. An EzButton adopts the same layout as other EzComponents, and fires an action listener whenever it is clicked
 * from the interface.
 *
 * @author Alexandre Dufour
 */
public class EzButton extends EzComponent {
    private JButton jButton;

    /**
     * Creates a new EzButton with given title and action listener (i.e., the method which will be called when the button is clicked)
     *
     * @param title    The button title.
     * @param listener The listener which will be called when the button is clicked.
     */
    public EzButton(final String title, final ActionListener listener) {
        super(title);

        ThreadUtil.invoke(() -> {
            jButton = new JButton(title);
            jButton.addActionListener(listener);
        }, !SwingUtilities.isEventDispatchThread());
    }

    @Override
    public void addTo(final Container container) {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        container.add(jButton, gbc);
    }

    /**
     * Adds an action listener that will be executed when the button is clicked.
     *
     * @param listener Listener to add.
     */
    public void addAction(final ActionListener listener) {
        jButton.addActionListener(listener);
    }

    /**
     * Removes an action that is executed when the button is clicked. No error is thrown if this
     * action was not registered
     *
     * @param listener Listener to add.
     */
    public void removeAction(final ActionListener listener) {
        jButton.removeActionListener(listener);
    }

    @Override
    public void setToolTipText(final String text) {
        ThreadUtil.invoke(() -> jButton.setToolTipText(text), !SwingUtilities.isEventDispatchThread());
    }

    /**
     * Sets the text on the button UI.
     *
     * @param text Text to set.
     */
    public void setText(final String text) {
        ThreadUtil.invoke(() -> jButton.setText(text), !SwingUtilities.isEventDispatchThread());
    }

    @Override
    public void dispose() {
        jButton = null;
        super.dispose();
    }

    /**
     * Enables (or disables) the button.
     *
     * @param enabled true to enable the button, otherwise false
     */
    public void setEnabled(final boolean enabled) {
        ThreadUtil.invokeLater(() -> jButton.setEnabled(enabled));
    }
}
