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

import org.bioimageanalysis.icy.extension.plugin.PluginDescriptor;
import org.bioimageanalysis.icy.model.sequence.Sequence;
import org.bioimageanalysis.icy.model.swimmingPool.SwimmingObject;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.lang.*;

import java.awt.*;

public class SwingFactory extends VarEditorFactory {
    @Override
    public VarEditor<Integer> createButton(final VarTrigger variable) {
        return new Button(variable);
    }

    @Override
    public VarEditor<Integer> createChannelSelector(final VarChannel varChannel, final Var<Sequence> sequence, final boolean allowAllChannels) {
        return new ChannelSelector(varChannel, sequence, allowAllChannels);
    }

    @Override
    public VarEditor<Boolean> createCheckBox(final Var<Boolean> variable) {
        return new CheckBox(variable);
    }

    @Override
    public <V> VarEditor<V> createComboBox(final Var<V> variable) {
        return new ComboBox<>(variable);
    }

    @Override
    public VarEditor<Color> createColorChooser(final Var<Color> variable) {
        return new ColorChooser(variable);
    }

    @Override
    public VarEditor<Integer> createFrameSelector(final VarFrame varFrame, final Var<Sequence> sequence, final boolean allowAllFrames) {
        return new FrameSelector(varFrame, sequence, allowAllFrames);
    }

    @Override
    public <V> VarEditor<V> createLabel(final Var<V> variable) {
        return new Label<>(variable);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public VarEditor createMutableVarEditor(final VarMutable varMutable) {
        return new MutableVarEditor(varMutable);
    }

    @Override
    public VarEditor<String> createPasswordField(final Var<String> variable) {
        return new PasswordField(variable);
    }

    @Override
    public VarEditor<PluginDescriptor> createPluginChooser(final Var<PluginDescriptor> variable) {
        return new PluginChooser<>(variable);
    }

    @Override
    public VarEditor<Sequence> createSequenceChooser(final Var<Sequence> variable) {
        return new SequenceChooser(variable);
    }

    @Override
    public VarEditor<Sequence[]> createSequenceList(final VarGenericArray<Sequence[]> varSequenceArray) {
        return new SequenceList(varSequenceArray);
    }

    @Override
    public VarEditor<Sequence> createSequenceViewer(final Var<Sequence> variable) {
        return new SequenceViewer(variable);
    }

    @Override
    public VarEditor<Integer> createSliceSelector(final VarSlice varSlice, final Var<Sequence> sequence, final boolean allowAllSlices) {
        return new SliceSelector(varSlice, sequence, allowAllSlices);
    }

    @Override
    public <N extends Number> VarEditor<N> createSlider(final Var<N> variable) {
        return new Slider<>(variable);
    }

    @Override
    public <N extends Number> VarEditor<N> createSpinner(final Var<N> variable) {
        return new Spinner<>(variable);
    }

    @Override
    public VarEditor<SwimmingObject> createSwimmingObjectChooser(final Var<SwimmingObject> variable) {
        return new SwimmingObjectChooser(variable);
    }

    @Override
    public <V> VarEditor<V> createTextArea(final Var<V> variable, final int rows) {
        return new TextArea<>(variable, rows);
    }

    @Override
    public <V> VarEditor<V> createTextField(final Var<V> variable) {
        return new TextField<>(variable);
    }

    @Override
    public VarEditor<VarMutable> createTypeChooser(final VarMutable variable) {
        return new TypeChooser<>(variable);
    }
}
