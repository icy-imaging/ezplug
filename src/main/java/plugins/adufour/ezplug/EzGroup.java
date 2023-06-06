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

import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Class defining a group of EzComponents, which will appear in the interface within a titled box.
 *
 * @author Alexandre Dufour
 */
public class EzGroup extends EzPanel {
    public interface FoldListener {
        void foldStateChanged(boolean state);
    }

    private final FoldHandler foldHandler = new FoldHandler();

    private final ArrayList<FoldListener> listeners = new ArrayList<>();

    /**
     * Creates a new EzGroup with the given box title and set of ezComponents. Each component will
     * be drawn on the interface in their order of declaration.
     *
     * @param groupTitle   The group title (will appear on the enclosing box in the user interface)
     * @param ezComponents the components to add (must be non-empty)
     * @throws IllegalArgumentException if ezComponents is empty
     */
    public EzGroup(final String groupTitle, final EzComponent... ezComponents) {
        super(groupTitle, ezComponents);

        container.addMouseListener(foldHandler);
    }

    public void addFoldListener(final FoldListener listener) {
        listeners.add(listener);
    }

    @Override
    protected void buildPanel() {
        if (foldHandler.isFolded) {
            container.removeAll();
            container.setBorder(foldHandler.foldedBorder);
        }
        else {
            super.buildPanel();
            container.setBorder(foldHandler.unfoldedBorder);
        }
    }

    @Override
    public void dispose() {
        listeners.clear();

        container.removeMouseListener(foldHandler);

        super.dispose();
    }

    private void fireFoldStateChanged(final boolean state) {
        final ArrayList<FoldListener> listenersCopy;

        synchronized (listeners) {
            listenersCopy = new ArrayList<>(listeners);
        }

        for (final FoldListener listener : listenersCopy)
            listener.foldStateChanged(state);
    }

    /**
     * @return <code>true</code> if this group is currently folded (no visible component),
     * <code>false</code> otherwise
     */
    public boolean getFoldedState() {
        return foldHandler.isFolded;
    }

    /**
     * Sets the folded state of this group
     *
     * @param folded If true, the group will be folded and its components will be hidden.
     */
    public void setFoldedState(final boolean folded) {
        if (folded == foldHandler.isFolded)
            return;

        foldHandler.isFolded = folded;
        fireFoldStateChanged(folded);
        foldHandler.adjustToolTip();
    }

    private final class FoldHandler implements MouseListener {
        private boolean isFolded = false;

        private final static char FOLDED_SYMBOL = '▶'; //u25B6

        private final static char UNFOLDED_SYMBOL = '▼'; //u25BC

        private final TitledBorder foldedBorder = new TitledBorder(FoldHandler.FOLDED_SYMBOL + " " + name);

        private final TitledBorder unfoldedBorder = new TitledBorder(FoldHandler.UNFOLDED_SYMBOL + " " + name);

        @Override
        public void mouseClicked(final MouseEvent e) {
            final FontMetrics fm = container.getGraphics().getFontMetrics();
            final int w = fm.charWidth(FOLDED_SYMBOL);
            final int h = fm.getHeight();
            final int xBorder = 10;
            final int yBorder = 4;
            if (e.getX() < xBorder || e.getY() < yBorder || e.getX() > (xBorder + w) || e.getY() > (yBorder + h)) {
                e.consume();
                return;
            }

            isFolded = !isFolded;
            fireFoldStateChanged(isFolded);
            adjustToolTip();
        }

        @Override
        public void mousePressed(final MouseEvent e) {
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
        }

        @Override
        public void mouseEntered(final MouseEvent e) {
            adjustToolTip();
        }

        @Override
        public void mouseExited(final MouseEvent e) {
        }

        private void adjustToolTip() {
            if (isFolded && !getToolTipText().isEmpty()) {
                container.setToolTipText(getToolTipText());
            }
            else {
                container.setToolTipText(null);
            }
        }
    }
}
