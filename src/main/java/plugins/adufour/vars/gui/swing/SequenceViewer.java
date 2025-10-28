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

package plugins.adufour.vars.gui.swing;

import org.bioimageanalysis.icy.Icy;
import org.bioimageanalysis.icy.gui.GraphicsUtil;
import org.bioimageanalysis.icy.gui.sequence.SequencePreviewPanel;
import org.bioimageanalysis.icy.model.sequence.Sequence;
import org.bioimageanalysis.icy.model.sequence.SequenceEvent;
import org.bioimageanalysis.icy.model.sequence.SequenceListener;
import org.jetbrains.annotations.NotNull;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarMutable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SequenceViewer extends SwingVarEditor<Sequence> implements SequenceListener {
    public SequenceViewer(final Var<Sequence> variable) {
        super(variable);
    }

    /**
     * Constructs a new SequenceViewer using a mutable variable of the type sequence.
     *
     * @param variable the mutable variable containing the {@link Sequence} to display
     * @throws ClassCastException if the given variable is not of type {@link Sequence}
     * @see Var#getType()
     */
    @SuppressWarnings("unchecked")
    public SequenceViewer(final VarMutable variable) {
        super(variable);
        if (!variable.getType().isAssignableFrom(Sequence.class)) throw new ClassCastException("Variable " + variable.getName() + " is not a Sequence");
    }

    @Override
    protected JComponent createEditorComponent() {
        final SequencePreviewPanel editor = new SequencePreviewPanel(true) {
            @Override
            protected void paintChildren(final Graphics g) {
                super.paintChildren(g);
                if (mouseOver && getVariable().getValue() != null) {
                    g.setColor(Color.white);
                    GraphicsUtil.drawCenteredString(g, "(double-click to open)", getWidth() / 2, getHeight() - 20, true);
                }
            }
        };

        // make every component opaque
        for (final JPanel panel : new JPanel[]{editor.getMainPanel(), editor.getTPanel(), editor.getZPanel()}) {
            editor.setOpaque(false);
            panel.setOpaque(false);
            for (final Component c : panel.getComponents())
                if (c instanceof JComponent) ((JComponent) c).setOpaque(false);
        }

        return editor;
    }

    private boolean mouseOver = false;

    private final MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(final @NotNull MouseEvent e) {
            if (e.getClickCount() == 2) {
                final Sequence s = getVariable().getValue();
                if (s != null) Icy.getMainInterface().addSequence(s);
            }
        }

        @Override
        public void mouseEntered(final MouseEvent e) {
            mouseOver = true;
            getEditorComponent().repaint();
        }

        @Override
        public void mouseExited(final MouseEvent e) {
            mouseOver = false;
            getEditorComponent().repaint();
        }
    };

    @Override
    protected void activateListeners() {
        getEditorComponent().addMouseListener(mouseAdapter);
    }

    @Override
    protected void deactivateListeners() {
        getEditorComponent().removeMouseListener(mouseAdapter);
    }

    @Override
    public SequencePreviewPanel getEditorComponent() {
        return (SequencePreviewPanel) super.getEditorComponent();
    }

    @Override
    protected void updateInterfaceValue() {
        final Sequence s = variable.getValue();
        getEditorComponent().setModel(s);
    }

    @Override
    public boolean isComponentResizeable() {
        return true;
    }

    @Override
    public double getComponentVerticalResizeFactor() {
        return 1.0;
    }

    @Override
    public void valueChanged(final Var<Sequence> source, final Sequence oldValue, final Sequence newValue) {
        if (oldValue != null) oldValue.removeListener(this);
        if (newValue != null) newValue.addListener(this);
        super.valueChanged(source, oldValue, newValue);
    }

    @Override
    public void referenceChanged(final Var<Sequence> source, final Var<? extends Sequence> oldReference, final Var<? extends Sequence> newReference) {
        if (oldReference != null && oldReference.getValue() != null) oldReference.getValue().removeListener(this);
        super.referenceChanged(source, oldReference, newReference);
    }

    @Override
    public void sequenceClosed(final Sequence sequence) {
    }

    @Override
    public void sequenceChanged(final SequenceEvent sequenceEvent) {
        getEditorComponent().dimensionChanged();
        getEditorComponent().imageChanged();
    }
}
