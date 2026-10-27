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

package plugins.adufour.ezplug;

import fr.icy.Icy;
import fr.icy.common.type.value.IntegerValue;
import fr.icy.extension.plugin.abstract_.PluginActionable;
import fr.icy.extension.plugin.annotation_.IcyPluginTags;
import fr.icy.shared.logging.CustomLevel;
import fr.icy.system.IcyHandledException;
import org.jetbrains.annotations.NotNull;
import plugins.adufour.vars.util.VarException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main component of the EzPlug framework. EzPlug provides numerous additional features to the
 * {@link fr.icy.extension.plugin.abstract_.Plugin} class to simplify the development of plug-ins for ICY. In a nutshell, it allows
 * to:
 * <ul>
 * <li>a) design intuitive and homogeneous graphical interfaces;</li>
 * <li>b) save/load parameters to/from</li>
 * </ul>
 * disk in a standardized way (e.g. XML).
 * <p>
 * To create an EzPlug and benefit from these features, simply create a class that extends EzPlug
 * instead of {@link fr.icy.extension.plugin.abstract_.Plugin}. EzPlug is abstract and requires to implement the following methods:
 * <ul>
 * <li>The {@link #initialize()} method should be used to declare the parameters, buttons, groups
 * etc.. This is done via the {@link #addEzComponent(EzComponent) addEzComponent} method. EzPlug
 * here uses the Vars library to provide create tailored graphical components for all major data
 * types (e.g. check box for a boolean, spinner for a numerical parameter, combo box for list of
 * values etc.). See the {@link EzVar} class hierarchy for an overview.
 * <p>
 * Several helper methods offer additional possibilities such as: 1) group variables together into a
 * panel which can be folded to save space on the interface; 2) automatically show or hide
 * parameters or groups thereof, depending on the value of other parameters (so-called visibility
 * triggers); 3) automatically respond to parameter input events for enhanced user experience; 4)
 * add traditional or custom AWT or Swing components to the interface (for advanced developers).
 * <li>The {@link #execute()} method holds the main execution code of the plug-in, and is called
 * when the "Run" button is clicked on the interface.
 * <li>The {@link #clean()} method should be used to clean "sensitive" resources (if any) created by
 * the plug-in (e.g. sequence painters, I/O streams, etc.), in order to free memory properly and/or
 * avoid polluting the display. This method is called when the interface (or Icy) is closed.
 * </ul>
 * The final generated interface comes with a sleek title bar and an action panel in the bottom,
 * from where the user may start or {@link EzStoppable stop} the execution process, load and save
 * parameters from/to disk via XML files.
 * <p>
 * An example showing most of the features in action is available online
 * <a href="https://icy.bioimageanalysis.org/plugin/ezplug-sdk/">here</a>.
 *
 * @author Alexandre Dufour
 * @see plugins.adufour.ezplug.EzInternalFrame
 */
@IcyPluginTags("EzPlug")
public abstract class EzPlug extends PluginActionable {
    private static final Logger LOGGER = Logger.getLogger(EzPlug.class.getName());

    /**
     * Number of active instances of this plugin
     */
    private static final IntegerValue nbInstances = new IntegerValue(0);

    private EzGUI ezgui;

    private final HashMap<String, EzVar<?>> ezVars;

    private boolean timeTrial = false;

    private long startTime;

    private Thread executionThread = null;

    private final EzStatus status = new EzStatus();

    public final Runnable EzPlugProcess = () -> {
        executionThread = Thread.currentThread();

        try {
            try {
                startTime = System.nanoTime();

                status.setCompletion(Double.NaN);
                status.setMessage("Running");

                execute();

                if (timeTrial && LOGGER.isLoggable(CustomLevel.TRACE))
                    LOGGER.log(CustomLevel.TRACE, getName() + " executed in " + (System.nanoTime() - startTime) / 1000000 + " ms");
            }
            catch (final VarException e) {
                if (isHeadLess())
                    throw e;

                String message = "Parameter: " + (e.source == null ? "(unknown)" : e.source.getName()) + "\n";
                message += "Message: " + e.getMessage();

                throw new EzException(this, message, true);
            }
        }
        catch (final EzException e) {
            if (e.catchException) {
                String message = "Plugin: " + (e.source == null ? "(unknown)" : e.source.getName()) + "\n";
                message += e.getMessage();

                throw new IcyHandledException(message);
            }

            throw e;
        }
        finally {
            status.done();
        }
    };

    protected EzPlug() {
        ezVars = new HashMap<>();

        synchronized (nbInstances) {
            nbInstances.setValue(nbInstances.getValue() + 1);
        }
    }

    protected void addComponent(final Component component) {
        if (component == null) {
            // the component was not initialized inside the plug-in code
            throw new EzException(this, "null graphical component", false);
        }

        final EzGUI g = getUI();
        if (g != null)
            g.addComponent(component);
    }

    /**
     * Adds a graphical component to the interface. Components are graphically ordered on the
     * interface panel in top-down fashion in the same order as they are added.
     *
     * @param component the component to add
     * @see plugins.adufour.ezplug.EzVar
     * @see plugins.adufour.ezplug.EzButton
     * @see plugins.adufour.ezplug.EzGroup
     */
    protected void addEzComponent(final EzComponent component) {
        if (component == null) {
            // the component was not initialized inside the plug-in code
            throw new EzException(this, "A plug-in variable was not initialized properly", false);
        }

        // if the component is a variable, register it
        if (component instanceof EzVar<?>)
            registerVariable((EzVar<?>) component);

        if (component instanceof EzPanel)
            registerVariables((EzPanel) component);

        final EzGUI g = getUI();
        if (g != null)
            g.addEzComponent(component, true);
    }

    /**
     * Cleans user-defined structures when the plug-in window is closed. This method should be used
     * if this EzPlug has references to some structures which should be cleaned properly before
     * closing the plug window (e.g., painters on a sequence, I/O streams, etc.).<br>
     * NOTE: this method is called after cleaning of the graphical user interface. Hence, any
     * attempt to access graphical components (or change the value of EzVar objects) will result in
     * a null pointer exception.
     */
    public abstract void clean();

    /**
     * Clean method called whenever the user interface (or ICY) is closed, in order to clean
     * internal and user-defined structures properly.
     */
    void cleanFromUI() {
        // clean user-specific items
        try {
            clean();
        }
        catch (final EzException eze) {
            // do not display anything as the interface is being closed
            if (!eze.catchException)
                throw eze;
        }

        // clear all variables

        ezVars.clear();

        ezgui = null;

        synchronized (nbInstances) {
            nbInstances.setValue(nbInstances.getValue() - 1);
        }
    }

    /**
     * Entry point of this EzPlug, which creates the user interface and displays it on the main
     * desktop pane. This method can be called either via ICY's main menu or directly via code.
     */
    @Override
    public void run() {
        if (Icy.getMainInterface().isHeadLess()) {
            // Special scenario: The plugin is running headless (from the command line)
            // => call the execute() method right away and exit
            execute();
            return;
        }

        try {
            // generate the user interface
            createUI();

            // show the interface to the user
            showUI();
        }
        catch (final EzException e) {
            if (e.catchException)
                JOptionPane.showMessageDialog(ezgui.getFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            else
                throw e;
        }
    }

    /**
     * Generates the user interface of this EzPlug. Note that the window is not shown on screen
     * (this can be done by calling the {@link #showUI()} method).
     */
    public void createUI() {
        // generate the main interface
        ezgui = new EzGUI(this);

        // incorporate user-defined parameters
        initialize();

        // pack the frame
        ezgui.repack(true);

        // fire listeners declared in the initialize method
        for (final EzVar<?> var : ezVars.values())
            var.fireVariableChangedInternal();

        addIcyFrame(ezgui);
    }

    /**
     * Main method containing the core execution code of this EzPlug. This method is launched in a
     * separate thread only if it was called from the user interface (via the run button)
     */
    protected abstract void execute();

    /**
     * Generates an EzPlug code fragment that is ready to use and compile
     *
     * @param className the name of the new class
     * @return Code template for the new class.
     */
    public static String generateEzPlugCodeFragment(final String className) {
        final StringWriter sw = new StringWriter();

        sw.write("import " + EzPlug.class.getPackage().getName() + ".*;\n\n");
        sw.write("public class " + className + " extends " + EzPlug.class.getName() + "\n{\n");

        for (final java.lang.reflect.Method m : EzPlug.class.getDeclaredMethods()) {
            if (java.lang.reflect.Modifier.isAbstract(m.getModifiers())) {
                final Class<?> returnType = m.getReturnType();
                sw.write("  public " + (returnType == null ? "void" : returnType.getName()) + " ");

                sw.write(m.getName() + "(");

                final Class<?>[] params = m.getParameterTypes();
                if (params.length > 0) {
                    int cpt = 1;

                    sw.write(params[0].getName() + " arg" + cpt++);

                    for (int i = 1; i < params.length; i++)
                        sw.write(", " + params[i].getName() + " arg" + cpt++);
                }

                sw.write(")\n  {\n");
                sw.write("  // TODO: write your code here\n");
                sw.write("  }\n\n");
            }
        }

        sw.write('}');

        return sw.toString();
    }

    /**
     * Gets the name of this EzPlug (defaults to the class name).
     *
     * @return The name of this EzPlug.
     */
    @Override
    public @NotNull String getName() {
        return getDescriptor().getName();
    }

    /**
     * @return The active (not-destroyed) sequence number of this plug-in.
     */
    public static int getNbInstances() {
        return nbInstances.getValue();
    }

    /**
     * Gets the starting execution time of this EzPlug (in nanoseconds). This method can be used in
     * conjunction with the {@link System#nanoTime()} method to measure elapsed time during the
     * execution process
     *
     * @return the starting execution time in nanoseconds (obtained from the
     * {@link System#nanoTime()} method)
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Gets the graphical interface attached to this EzPlug. This interface gives access to specific
     * methods related to user interaction (e.g., progress bars, highlighting, etc.).
     *
     * @return the graphical interface of the EzPlug, or null if the EzPlug has been created via
     * code without generating the interface
     */
    public EzGUI getUI() {
        return ezgui;
    }

    protected EzStatus getStatus() {
        return status;
    }

    /**
     * Hides the user interface (without destroying it)
     */
    public void hideUI() {
        if (ezgui == null)
            return;

        ezgui.setVisible(false);
    }

    /**
     * @return true if the graphical user interface has not been initialized (e.g., when running
     * without screen or on a server). If this method returns true, then the
     * {@link #getUI()} will return null
     */
    public boolean isHeadLess() {
        return getUI() == null;
    }

    /**
     * This method lets the developer initialize the user interface of this EzPlug by adding
     * variables and other EzComponent objects via the {@link #addEzComponent(EzComponent)} method
     *
     * @see plugins.adufour.ezplug.EzVar
     * @see plugins.adufour.ezplug.EzComponent
     */
    protected abstract void initialize();

    /**
     * Attempts to interrupt the execution of this plug-in, by calling {@link Thread#interrupt()} on
     * the execution thread. Note that this does not guarantee that the execution will indeed stop,
     * as it is up to the implementation of {@link #execute()} to regularly monitor the thread's
     * interrupted flag
     */
    public void stopExecution() {
        if (executionThread != null)
            executionThread.interrupt();
    }

    /**
     * Loads the EzPlug user parameters from the specified XML file.
     *
     * @param file File to load the plugin parameters from.
     * @see EzVarIO
     */
    public void loadParameters(final File file) {
        try {
            EzVarIO.load(this, file, ezVars);
        }
        catch (final EzException e) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, "Unable to load parameters from XML.", e);

            if (!e.catchException)
                throw e;
        }
    }

    <T> void registerVariable(final @NotNull EzVar<T> var) {
        final String varID = var.getID();

        if (ezVars.containsKey(varID))
            throw new IllegalArgumentException("Variable " + varID + " already exists");

        ezVars.put(varID, var);
    }

    void registerVariables(final @NotNull EzPanel ezPanel) {
        for (final EzComponent panelComponent : ezPanel) {
            if (panelComponent instanceof EzVar<?>) {
                registerVariable((EzVar<?>) panelComponent);
            }
            else if (panelComponent instanceof EzPanel) {
                // add recursively
                registerVariables((EzPanel) panelComponent);
            }
        }
    }

    /**
     * Saves the EzPlug user parameters into the specified XML file.
     *
     * @param file File to save the plugin parameters to.
     * @see EzVarIO
     */
    public void saveParameters(final File file) {
        EzVarIO.save(this, ezVars, file);
    }

    /**
     * Displays the user interface on screen. If it didn't exist, the interface is first created
     */
    public void showUI() {
        if (ezgui == null)
            createUI();

        ezgui.setVisible(true);
        ezgui.toFront();
    }

    /**
     * Sets whether the execution time of this EzPlug should be displayed on the console
     *
     * @param displayRunningTime If true, the execution time of this plugin is presented on the console.
     */
    public void setTimeDisplay(final boolean displayRunningTime) {
        this.timeTrial = displayRunningTime;
    }
}