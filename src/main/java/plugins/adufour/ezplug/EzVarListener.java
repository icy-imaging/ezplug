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

package plugins.adufour.ezplug;

/**
 * Interface used to fire events occurring on variables.
 *
 * @param <T> Type held by the variable that changes are fired.
 * @author Alexandre Dufour
 */
public interface EzVarListener<T> {
    /**
     * @param source   Variable on which the value change has been done.
     * @param newValue The new value of the variable.
     */
    void variableChanged(EzVar<T> source, T newValue);
}
