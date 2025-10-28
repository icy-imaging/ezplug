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

import org.bioimageanalysis.icy.Icy;
import org.bioimageanalysis.icy.gui.frame.IcyExternalFrame;
import org.bioimageanalysis.icy.gui.frame.IcyFrame;
import org.bioimageanalysis.icy.gui.frame.IcyInternalFrame;
import org.bioimageanalysis.icy.system.IcyHandledException;
import org.bioimageanalysis.icy.system.thread.ThreadUtil;
import plugins.adufour.ezplug.EzGroup.FoldListener;

import javax.swing.*;
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.io.File;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EzDialog extends IcyFrame implements FoldListener {
    protected static File currentParametersPath = null;

    protected Color logoTitleColor;

    private JPanel jPanelParameters;

    private final List<Object> components = new ArrayList<>();

    private JDialog dialog;

    private final boolean isDialog;

    /**
     * Task that will pack the interface. This task must be invoked on the AWT Event dispatch thread
     */
    public final Runnable fullPackingTask = () -> repack(true);

    public EzDialog(final String title) {
        this(title, true);
    }

    protected EzDialog(final String title, final boolean isDialog) {
        super(title, true, true, false, true);

        this.isDialog = isDialog;

        ThreadUtil.invokeNow(() -> {
            jPanelParameters = new JPanel();
            jPanelParameters.setLayout(new BoxLayout(jPanelParameters, BoxLayout.X_AXIS));
            jPanelParameters.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));

            if (isDialog) {
                dialog = new JDialog();
                dialog.setTitle(getTitle());
                dialog.setLayout(new BorderLayout());
                dialog.add(jPanelParameters, BorderLayout.CENTER);
                dialog.setModalityType(ModalityType.APPLICATION_MODAL);
                dialog.setLocationRelativeTo(Icy.getMainInterface().getMainFrame().getDesktopPane());
                dialog.pack();
            }
            else {
                getContentPane().add(jPanelParameters, BorderLayout.CENTER);
                pack();
                setOptimalLocation();
            }
        });
    }

    protected void setOptimalLocation() {
        // place the window beside other EzPlugs
        Point location = new Point();

        if (isExternalized()) {
            location = Icy.getMainInterface().getMainFrame().getLocationOnScreen();
            location.y += Icy.getMainInterface().getMainFrame().getHeight();
        }

        boolean validLocation = false;

        final Dimension screenDim = getToolkit().getScreenSize();

        loop:
        while (!validLocation) {
            final AbstractList<? extends Component> comps;

            if (isInternalized()) {
                comps = new ArrayList<>(Arrays.asList(Icy.getMainInterface().getDesktopPane().getAllFrames()));
            }
            else {
                comps = Icy.getMainInterface().getExternalFrames();
            }

            for (final Component gui : comps) {
                if (this.getFrame() == gui || !gui.isShowing())
                    continue;

                // final Point wLoc = isInternalized() ? ezGUIs.get(gui) :
                // gui.getFrame().getLocationOnScreen();
                final Point wLoc = isInternalized() ? gui.getLocation() : gui.getLocationOnScreen();

                if (wLoc.distanceSq(location) < 400) {
                    final Dimension dim = gui.getPreferredSize();
                    location.x += dim.width;
                    if (location.x > screenDim.width - dim.width) {
                        // no more space in x, move along y
                        location.x = 0;
                        location.y += dim.height;

                        if (location.y > screenDim.height - dim.height) {
                            // no more space on screen, break
                            //location.x = 0; // Already assigned to this value
                            location.y = 0;
                            break;
                        }
                    }
                    //validLocation = false; // Already assigned to this value
                    continue loop;
                }
            }
            validLocation = true;
        }
        setLocation(location);
    }

    @Override
    protected IcyInternalFrame createInternalFrame(final String title, final boolean resizable, final boolean closable, final boolean maximizable, final boolean iconifiable) {
        return new EzInternalFrame(title, resizable, closable, maximizable, iconifiable);
    }

    @Override
    protected IcyExternalFrame createExternalFrame(final String title) {
        return new EzExternalFrame(title);
    }

    public void addComponent(final Component component) {
        components.add(component);
    }

    public void addEzComponent(final EzComponent component) {
        addEzComponent(component, true);
    }

    @SuppressWarnings("unchecked")
    protected void addEzComponent(final EzComponent component, final boolean isSingle) {
        // if the component is a group, add its internal components recursively
        if (component instanceof final EzPanel panel) {
            if (panel instanceof EzGroup)
                ((EzGroup) panel).addFoldListener(this);

            for (final EzComponent groupComponent : panel)
                addEzComponent(groupComponent, false);
        }

        // set the parent UI of this component
        component.setUI(this);

        // if the component is an EzVar, activate the internal listener
        // => this will cause the UI to refresh if a var gets shown/hidden
        if (component instanceof @SuppressWarnings("rawtypes") final EzVar var) {
            var.variable.addListener(var);
        }

        if (isSingle)
            components.add(component);
    }

    @Override
    public void foldStateChanged(final boolean newState) {
        repack(true);
    }

    /**
     * Re-packs the user interface. This method should be called if one of the components was
     * changed either in size or visibility state.
     *
     * @param updateParametersPanel Set to true if the visibility of some parameters has been changed and the panel
     *                              should be redrawn before re-packing the frame
     */
    public void repack(final boolean updateParametersPanel) {
        // int width = getWidth();

        if (updateParametersPanel) {
            jPanelParameters.removeAll();

            final GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(2, 5, 2, 5);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            // resize behavior
            gbc.weightx = 1;
            gbc.weighty = 1;

            // split components by columns
            final ArrayList<List<Object>> columns = new ArrayList<>();
            ArrayList<Object> currentColumn = new ArrayList<>();
            columns.add(currentColumn);
            for (final Object component : components) {
                if (component instanceof JSeparator && ((JSeparator) component).getOrientation() == JSeparator.VERTICAL) {
                    currentColumn = new ArrayList<>();
                    columns.add(currentColumn);
                }
                else {
                    currentColumn.add(component);
                }
            }

            for (int i = 0; i < columns.size(); i++) {
                final List<Object> column = columns.get(i);

                if (i > 0 && !column.isEmpty())
                    jPanelParameters.add(new JSeparator(JSeparator.VERTICAL));

                final JPanel columnContainer = new JPanel(new GridBagLayout());

                for (final Object object : column) {
                    if (object instanceof EzComponent) {
                        ((EzComponent) object).addToContainer(columnContainer);
                    }
                    else if (object instanceof final Component component) {
                        final GridBagLayout gridbag = (GridBagLayout) columnContainer.getLayout();

                        component.setFocusable(false);

                        // reset vertical resizing
                        gbc.weighty = 1;
                        if (component instanceof JButton)
                            gbc.weighty = 0;

                        gridbag.setConstraints(component, gbc);

                        columnContainer.add(component);
                    }
                    else
                        throw new UnsupportedOperationException(
                                "Cannot add a " + object.getClass().getSimpleName() + " to the graphical interface");
                }

                jPanelParameters.add(columnContainer);
            }
            jPanelParameters.validate();
            jPanelParameters.repaint();
        }

        if (isDialog) {
            dialog.getContentPane().validate();
            dialog.getContentPane().repaint();
            dialog.pack();
        }
        else {
            pack();
        }

        // setSize(new Dimension(Math.max(width, getWidth()), getPreferredSize().height));
    }

    /**
     * Shows the dialog on the screen and returns only when the dialog is closed (either via the
     * close button, or by calling the {@link #hideDialog()} method). By default, the dialog is
     * modal (other windows are blocked until this dialog is closed)
     */
    public void showDialog() {
        showDialog(true);
    }

    /**
     * Shows the dialog on the screen and returns only when the dialog is closed (either via the
     * close button, or by calling the {@link #hideDialog()} method)
     *
     * @param modal <code>true</code> if the dialog should block other windows until it is closed,
     *              <code>false</code> otherwise
     */
    public void showDialog(final boolean modal) {
        if (!isDialog)
            throw new IcyHandledException("Cannot show an IcyFrame as a Dialog. Use setVisible(true) instead.");

        ThreadUtil.invokeNow(() -> {
            repack(true);
            dialog.setLocationRelativeTo(null);
            dialog.setModal(modal);
            dialog.setVisible(true);
            if (modal)
                hideDialog();
        });
    }

    @Override
    public void close() {
        // TIP: very bad idea to override 'close()' method here as EzDialog override IcyFrame class.
        // Because of that, the dialog remains referenced by the static IcyFrame.frames list !
        // use 'hideDialog()' to hide the dialog and 'close()' to release it
        // if (isDialog) hideDialog();
        // else super.close();

        // need to hide dialog first
        if (isDialog)
            hideDialog();

        // then close
        super.close();
    }

    /**
     * Closes the dialog
     */
    public void hideDialog() {
        if (!isDialog)
            return;

        ThreadUtil.invokeNow(() -> dialog.setVisible(false));
    }

    @Override
    public void onClosed() {
        // dispose all components
        for (final Object object : components) {
            if (object instanceof EzComponent) {
                ((EzComponent) object).dispose();
            }
        }

        components.clear();

        jPanelParameters.removeAll();

        super.onClosed();
    }
}
