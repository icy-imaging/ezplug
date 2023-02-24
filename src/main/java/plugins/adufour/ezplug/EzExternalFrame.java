package plugins.adufour.ezplug;

import icy.gui.frame.IcyExternalFrame;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;

public class EzExternalFrame extends IcyExternalFrame {
    EzExternalFrame(String title) {
        super(title);

        getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        getContentPane().setLayout(new BorderLayout(0, 0));
    }
}
