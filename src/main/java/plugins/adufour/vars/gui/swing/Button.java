package plugins.adufour.vars.gui.swing;

import java.awt.event.ActionListener;

import icy.gui.component.button.IcyButton;
import plugins.adufour.vars.lang.VarTrigger;

public class Button extends SwingVarEditor<Integer> {
    private ActionListener listener;

    public Button(VarTrigger variable) {
        super(variable);

        setNameVisible(false);
    }

    @Override
    protected IcyButton createEditorComponent() {
        if (getEditorComponent() != null) deactivateListeners();

        listener = e -> ((VarTrigger) variable).trigger();

        return new IcyButton(variable.getName());
    }

    @Override
    public IcyButton getEditorComponent() {
        return (IcyButton) super.getEditorComponent();
    }

    @Override
    public double getComponentVerticalResizeFactor() {
        return 0.0;
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
        // nothing to do (it's just a button with a name)
    }
}
