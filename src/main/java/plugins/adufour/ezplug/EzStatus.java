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

package plugins.adufour.ezplug;

import plugins.adufour.vars.lang.VarDouble;
import plugins.adufour.vars.lang.VarString;
import plugins.adufour.vars.util.VarListener;

/**
 * Utility class to monitor the status and progress of a task
 *
 * @author Alexandre Dufour
 */
public class EzStatus {
    /**
     * A completion level between 0 and 1
     */
    private final VarDouble progress = new VarDouble("Progress value", 0.0);

    /**
     * A text indicating what the task is currently doing
     */
    private final VarString message = new VarString("Progress message", "");

    /**
     * Registers a listener to the progress value
     *
     * @param listener the listener to register
     */
    public void addProgressListener(final VarListener<Double> listener) {
        progress.addListener(listener);
    }

    /**
     * Registers a listener to the message value
     *
     * @param listener the listener to register
     */
    public void addMessageListener(final VarListener<String> listener) {
        message.addListener(listener);
    }

    /**
     * Notify this status object (and potential listeners) that the task has completed (and
     * therefore resets the completion and message)
     */
    public void done() {
        setCompletion(1d);
        setMessage("Done");
    }

    /**
     * @return the variable containing the completion value
     */
    public VarDouble getProgressVariable() {
        return progress;
    }

    /**
     * @return The progress value (between 0 and 1)
     */
    public double getProgressValue() {
        return progress.getValue();
    }

    /**
     * @return <code>true</code> if the task is completed
     */
    public boolean isCompleted() {
        return getProgressValue() == 1.0;
    }

    /**
     * @return The variable containing the message value.
     */
    public VarString getMessageVariable() {
        return message;
    }

    /**
     * @return A text indicating what the task is currently doing.
     */
    public String getMessageValue() {
        return message.getValue();
    }

    /**
     * Unregisters a listener of the progress value.
     *
     * @param listener The listener to unregister.
     */
    public void removeProgressListener(final VarListener<Double> listener) {
        progress.addListener(listener);
    }

    /**
     * Unregisters a listener of the message value.
     *
     * @param listener The listener to unregister.
     */
    public void removeMessageListener(final VarListener<String> listener) {
        message.addListener(listener);
    }

    /**
     * Sets the status message.
     *
     * @param message Message to be presented on the status.
     */
    public void setMessage(final String message) {
        this.message.setValue(message);
    }

    /**
     * Sets the progression stage as a [0-1] percentage
     *
     * @param completion Progress value between 0.0 and 1.0 inclusive.
     */
    public void setCompletion(final double completion) {
        this.progress.setValue(completion);
    }
}
