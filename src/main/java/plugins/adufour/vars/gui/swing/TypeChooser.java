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

package plugins.adufour.vars.gui.swing;

import org.bioimageanalysis.icy.system.thread.ThreadUtil;
import plugins.adufour.vars.lang.VarMutable;
import plugins.adufour.vars.util.TypeChangeListener;
import plugins.adufour.vars.util.TypeUtil;

import javax.swing.*;

public class TypeChooser<T> extends ComboBox<T> implements TypeChangeListener {
    @SuppressWarnings("unchecked")
    public TypeChooser(final VarMutable variable) {
        super(variable);
    }

    @Override
    protected void activateListeners() {
        super.activateListeners();
        ((VarMutable) variable).addTypeChangeListener(this);
    }

    @Override
    protected void deactivateListeners() {
        super.deactivateListeners();
        ((VarMutable) variable).removeTypeChangeListener(this);
    }

    @Override
    protected ListCellRenderer<T> createRenderer() {
        return (list, value, index, isSelected, cellHasFocus) -> {
            if (value == null) return new JLabel("Anything");

            return new JLabel(TypeUtil.toFriendlyString((Class<?>) value));
        };
    }

    @Override
    protected void updateVariableValue() {
        final Class<?> item = (Class<?>) getEditorComponent().getSelectedItem();
        ((VarMutable) variable).setType(item);
    }

    @Override
    protected void updateInterfaceValue() {
        getEditorComponent().setSelectedItem(variable.getType());
    }

    @Override
    public void typeChanged(final Object source, final Class<?> oldType, final Class<?> newType) {
        ThreadUtil.invokeLater(this::updateInterfaceValue);
    }
}
