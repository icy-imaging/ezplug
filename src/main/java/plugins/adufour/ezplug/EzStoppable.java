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

import icy.plugin.PluginDescriptor;
import icy.system.IcyExceptionHandler;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Interface implemented by EzPlugs which handle a stop operation during the process. <br>
 * When implemented, a stop button will appear on the graphical interface.
 *
 * @author Alexandre Dufour
 * @version 2010-11-09: First implementation<br>
 */
public interface EzStoppable {
    /**
     * Requests the implementing EzPlug to stop the currently running process.
     */
    void stopExecution();

    class ForcedInterruptionHandler implements UncaughtExceptionHandler {
        private final PluginDescriptor source;

        public ForcedInterruptionHandler(final PluginDescriptor source) {
            this.source = source;
        }

        @Override
        public void uncaughtException(final Thread t, final Throwable e) {
            // this method is only called when the EzPlug process has been killed
            final String message = "Plug-in " + source.getName() + " has requested to kill thread " + t.getName() + " while it was still running.\n"
                    + "Implement the EzStoppable interface to handle clean exit calls and avoid this message.";
            IcyExceptionHandler.handleException(source, new InterruptedException(message), false);
        }
    }
}
