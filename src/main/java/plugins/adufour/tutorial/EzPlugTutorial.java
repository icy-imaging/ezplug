/*
 * Copyright (c) 2010-2026. Institut Pasteur.
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

package plugins.adufour.tutorial;

import fr.icy.extension.plugin.annotation_.IcyPluginName;
import fr.icy.gui.frame.progress.AnnounceFrame;
import fr.icy.system.logging.IcyLogger;
import plugins.adufour.ezplug.*;

import java.util.Arrays;

@IcyPluginName("EzPlug tutorial")
public class EzPlugTutorial extends EzPlug implements EzStoppable {
    private enum SomeEnumeration {
        CHOICE_1, CHOICE_2, CHOICE_3, SOME_other_CHOICE,
    }

    // declare here the variables you wish to use
    // in this tutorial I will use one of each

    EzVarBoolean varBoolean;
    EzVarDouble varDouble;
    EzVarDoubleArrayNative varDoubleA;
    EzVarEnum<SomeEnumeration> varEnum;
    EzVarFile varFile;
    EzVarFileArray varFileA;
    EzVarFolder varFolder;
    EzVarInteger varInt;
    EzVarIntegerArrayNative varIntA;
    EzVarSequence varSequence;
    EzVarText varText;

    // some other data
    boolean stopFlag;

    @Override
    protected void initialize() {
        // 1) variables must be initialized

        varBoolean = new EzVarBoolean("boolean var.", false);
        varBoolean.setToolTipText("Boolean input, with default value to false");
        varDouble = new EzVarDouble("double with free input");
        varDoubleA = new EzVarDoubleArrayNative("double array without input", new double[][]{new double[]{2.2, 4.4, 6.6}, new double[]{1.1, 3.3, 5.5}}, false);
        varEnum = new EzVarEnum<>("enumeration", SomeEnumeration.values(), SomeEnumeration.SOME_other_CHOICE);
        varFile = new EzVarFile("some file", null);
        varFileA = new EzVarFileArray("some files", null);
        varFolder = new EzVarFolder("a folder", null);
        varInt = new EzVarInteger("bounded integer", 4, -34, 28, 2);
        varIntA = new EzVarIntegerArrayNative("int array with input", new int[][]{new int[]{}, new int[]{}}, true);
        varSequence = new EzVarSequence("Input sequence");
        varText = new EzVarText("Some text", new String[]{"yes", "no", "maybe?"}, 2, true);

        // 2) and added to the interface in the desired order

        super.addEzComponent(varText);
        super.addEzComponent(varEnum);

        // let's group other variables per type

        final EzGroup groupNumeric = new EzGroup("Numeric variables", varDouble, varInt, varDoubleA, varIntA);
        super.addEzComponent(groupNumeric);

        final EzGroup groupFiles = new EzGroup("File choosers", varFile, varFileA, varFolder);
        super.addEzComponent(groupFiles);

        // let's add a description label
        final EzLabel label = new EzLabel("Check above to show/hide a variable");
        final EzGroup groupSequence = new EzGroup("Sequence group", varBoolean, label, varSequence);
        super.addEzComponent(groupSequence);

        // now let's add a trigger that shows varSequence when varBoolean is checked...
        varBoolean.addVisibilityTriggerTo(varSequence, true);
        // or when varEnum is on "CHOICE_2" or "CHOICE_3"
        varEnum.addVisibilityTriggerTo(varSequence, SomeEnumeration.CHOICE_2, SomeEnumeration.CHOICE_3);
    }

    @Override
    protected void execute() {
        // the main plugin code goes here, and runs in a separate thread

        IcyLogger.info(this.getClass(), varBoolean.name + " = " + varBoolean.getValue());
        super.getUI().setProgressBarValue(.1d);
        IcyLogger.info(this.getClass(), varDouble.name + " = " + varDouble.getValue());
        super.getUI().setProgressBarValue(.3d);
        IcyLogger.info(this.getClass(), varDoubleA.name + " = " + Arrays.toString(varDoubleA.getValue()));
        super.getUI().setProgressBarValue(.5d);
        IcyLogger.info(this.getClass(), varEnum.name + " = " + varEnum.getValue().name());
        super.getUI().setProgressBarValue(.6d);
        IcyLogger.info(this.getClass(), varFile.name + " = " + varFile.getValue());
        super.getUI().setProgressBarValue(.8d);
        IcyLogger.info(this.getClass(), varSequence.name + " = " + varSequence.getValue());
        super.getUI().setProgressBarValue(1d);
        new AnnounceFrame(varText.getValue());

        stopFlag = false;

        super.getUI().setProgressBarMessage("Waiting...");

        // Some code example to simulate a long process
        for (double cpt = 0d; cpt <= 1d; cpt += .01d) {
            // To prevent NullPointerException with setProgressBarValue() if EzPlug window is closed during its process
            if (super.getUI() == null)
                break;
            super.getUI().setProgressBarValue(cpt);
            if (stopFlag) {
                new AnnounceFrame("Process interrupted");
                break;
            }
            try {
                Thread.sleep(100);
            }
            catch (final InterruptedException e) {
                new AnnounceFrame("Process interrupted");
            }
        }

        getStatus().done();
    }

    @Override
    public void clean() {
        // use this method to clean local variables or input streams (if any) to avoid memory leaks
    }

    @Override
    public void stopExecution() {
        // this method is from the EzStoppable interface
        // if this interface is implemented, a "stop" button is displayed,
        // and this method is called when the user hits the "stop" button
        stopFlag = true;
    }
}
