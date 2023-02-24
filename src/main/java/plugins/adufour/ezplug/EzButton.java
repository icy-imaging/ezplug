package plugins.adufour.ezplug;

import icy.system.thread.ThreadUtil;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

/**
 * Class defining a button for use with EzPlugs. An EzButton adopts the same layout as other EzComponents, and fires an action listener whenever it is clicked
 * from the interface.
 *
 * @author Alexandre Dufour
 */
public class EzButton extends EzComponent {
    private JButton jButton;

    /**
     * Creates a new EzButton with given title and action listener (i.e. the method which will be called when the button is clicked)
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
    public void addTo(Container container) {
        GridBagConstraints gbc = new GridBagConstraints();
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
    public void addAction(ActionListener listener) {
        jButton.addActionListener(listener);
    }

    /**
     * Removes an action that is executed when the button is clicked. No error is thrown if this
     * action was not registered
     *
     * @param listener Listener to add.
     */
    public void removeAction(ActionListener listener) {
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
