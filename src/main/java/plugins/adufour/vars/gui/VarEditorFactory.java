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

package plugins.adufour.vars.gui;

import fr.icy.extension.plugin.PluginDescriptor;
import fr.icy.model.sequence.Sequence;
import fr.icy.model.swimmingPool.SwimmingObject;
import org.jetbrains.annotations.NotNull;
import plugins.adufour.vars.gui.swing.SwingFactory;
import plugins.adufour.vars.lang.*;

import java.awt.*;

public abstract class VarEditorFactory {
    static {
        // FIXME put this in a place where this can be changed
        setDefaultFactory(VarEditorFactoryType.SWING);
    }

    public enum VarEditorFactoryType {
        /**
         * Graphical components are built using the AWT/SWING toolkit
         */
        SWING
    }

    private static VarEditorFactory defaultFactory;

    /**
     * @return The default factory used to create graphical components. This method may return
     * <code>null</code> if no factory has been selected (see the
     * {@link #setDefaultFactory(VarEditorFactoryType)}) method to select one
     */
    public static VarEditorFactory getDefaultFactory() {
        return defaultFactory;
    }

    public static VarEditorFactory setDefaultFactory(final @NotNull VarEditorFactoryType factoryType) {
        return switch (factoryType) {
            case SWING -> defaultFactory = new SwingFactory();
            //default -> throw new IllegalArgumentException("Cannot load the \"" + factoryType + "\" factory");
        };
    }

    public abstract VarEditor<Integer> createButton(VarTrigger variable);

    public abstract VarEditor<Integer> createChannelSelector(VarChannel varChannel, Var<Sequence> sequence, boolean allowAllChannels);

    public abstract VarEditor<Integer> createFrameSelector(VarFrame varFrame, Var<Sequence> sequence, boolean allowAllFrames);

    public abstract VarEditor<Integer> createSliceSelector(VarSlice varSlice, Var<Sequence> sequence, boolean allowAllSlices);

    public abstract VarEditor<Boolean> createCheckBox(Var<Boolean> variable);

    public abstract VarEditor<Color> createColorChooser(Var<Color> variable);

    public abstract <V> VarEditor<V> createComboBox(Var<V> variable);

    public abstract <V> VarEditor<V> createLabel(Var<V> variable);

    public abstract VarEditor<?> createMutableVarEditor(VarMutable varMutable);

    public abstract VarEditor<PluginDescriptor> createPluginChooser(Var<PluginDescriptor> variable);

    public abstract VarEditor<Sequence> createSequenceChooser(Var<Sequence> variable);

    public abstract VarEditor<Sequence[]> createSequenceList(VarGenericArray<Sequence[]> varSequenceArray);

    public abstract VarEditor<Sequence> createSequenceViewer(Var<Sequence> variable);

    public abstract <N extends Number> VarEditor<N> createSlider(Var<N> variable);

    public abstract VarEditor<SwimmingObject> createSwimmingObjectChooser(Var<SwimmingObject> variable);

    public abstract <N extends Number> VarEditor<N> createSpinner(Var<N> variable);

    public abstract <V> VarEditor<V> createTextArea(Var<V> variable, int rows);

    public abstract <V> VarEditor<V> createTextField(Var<V> variable);

    public abstract VarEditor<String> createPasswordField(Var<String> variable);

    public abstract VarEditor<VarMutable> createTypeChooser(VarMutable variable);
}
