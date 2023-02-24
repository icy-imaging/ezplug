package plugins.adufour.vars.gui.swing;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;

import icy.gui.component.button.ColorChooserButton;

import plugins.adufour.vars.lang.Var;

/**
 * A color chooser component that appear as a button (filled with the current color value), and
 * which pops up a {@link JColorChooser} component when clicked
 *
 * @author Alexandre Dufour
 */
public class ColorChooser extends SwingVarEditor<Color> {
    private ActionListener listener;

    public ColorChooser(Var<Color> variable) {
        super(variable);
    }

    @Override
    protected JComponent createEditorComponent() {
        if (getEditorComponent() != null) deactivateListeners();

        listener = arg0 -> {
            Color defaultColor = variable.getDefaultValue();
            Color newColor = JColorChooser.showDialog(getEditorComponent(), variable.getName(), defaultColor);
            if (newColor == null) return;
            variable.setValue(newColor);
        };

        ColorChooserButton button = new ColorChooserButton(variable.getValue());

        /*IcyButton button = new IcyButton(new EmptyIcon(16, 16)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(variable.getValue());
                g.fill3DRect(0, 0, getWidth(), getHeight(), true);
            }
        };*/
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        return button;
    }

    @Override
    public JButton getEditorComponent() {
        return (JButton) super.getEditorComponent();
    }

    @Override
    protected void activateListeners() {
        getEditorComponent().addActionListener(listener);
    }

    @Override
    protected void deactivateListeners() {
        getEditorComponent().removeActionListener(listener);
    }

    @Override
    protected void updateInterfaceValue() {
        // nothing to do (the button will repaint itself automatically)
    }

}
