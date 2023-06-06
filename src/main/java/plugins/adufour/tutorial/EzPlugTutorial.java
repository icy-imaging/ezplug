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

package plugins.adufour.tutorial;

import icy.gui.frame.progress.AnnounceFrame;
import plugins.adufour.ezplug.*;

import java.util.Arrays;

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

        System.out.println(varBoolean.name + " = " + varBoolean.getValue());
        System.out.println(varDouble.name + " = " + varDouble.getValue());
        System.out.println(varDoubleA.name + " = " + Arrays.toString(varDoubleA.getValue()));
        System.out.println(varEnum.name + " = " + varEnum.getValue().name());
        System.out.println(varFile.name + " = " + varFile.getValue());
        System.out.println(varSequence.name + " = " + varSequence.getValue());
        new AnnounceFrame(varText.getValue());

        stopFlag = false;

        super.getUI().setProgressBarMessage("Waiting...");

        int cpt = 0;
        while (!stopFlag) {
            cpt++;
            if (cpt % 10 == 0) super.getUI().setProgressBarValue((cpt % 5000000) / 5000000.0);
            Thread.yield();
        }
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
