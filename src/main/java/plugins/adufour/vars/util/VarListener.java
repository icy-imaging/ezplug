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

package plugins.adufour.vars.util;

import plugins.adufour.vars.lang.Var;

/**
 * Interface allowing to listen to value (and reference) change events
 *
 * @param <T> Type of the {@link Var} object that this listener is created for.
 * @author Alexandre Dufour
 */
public interface VarListener<T> {
    /**
     * Called when the value of the source variable changes
     *
     * @param source   the variable firing the listener
     * @param oldValue the old variable value
     * @param newValue the new variable value
     */
    void valueChanged(Var<T> source, T oldValue, T newValue);

    /**
     * Called when the variable reference changes
     *
     * @param source       the variable firing the listener
     * @param oldReference the old variable reference
     * @param newReference the new variable reference
     */
    void referenceChanged(Var<T> source, Var<? extends T> oldReference, Var<? extends T> newReference);
}
