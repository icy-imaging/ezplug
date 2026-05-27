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

import plugins.adufour.ezplug.EzVar;
import plugins.adufour.vars.lang.Var;

/**
 * Utility class used to notify users that a variable value is invalid
 *
 * @author Alexandre Dufour
 */
public class VarException extends IllegalArgumentException {
    /**
     * The variable causing the exception
     */
    public final Var<?> source;

    /**
     * Creates a new exception with the specified message
     *
     * @param source  the variable causing the exception (will be used to provide better user experience
     *                when displaying error messages). If the source of the exception is an
     *                {@link EzVar} object, indicate here its underlying variable, accessible via
     *                {@link EzVar#getVariable()}
     * @param message the error message behind this exception
     */
    public VarException(final Var<?> source, final String message) {
        super(message);
        this.source = source;
    }

    @Override
    public String getMessage() {
        if (source == null) return super.getMessage();

        return "Parameter \"" + source.getName() + "\": " + super.getMessage();
    }
}
