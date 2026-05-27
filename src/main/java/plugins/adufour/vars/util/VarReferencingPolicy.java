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

/**
 * Enumeration of possible referencing policies of a Variable
 *
 * @author Alexandre Dufour
 */
public enum VarReferencingPolicy {
    /**
     * The variable may neither reference nor be referenced by another variable
     */
    NONE,
    /**
     * The variable may reference but not be referenced by another variable
     */
    IN,
    /**
     * The variable may not reference but may be referenced by another variable
     */
    OUT,
    /**
     * The variable may reference or be referenced by another variable (default)
     */
    BOTH
}
