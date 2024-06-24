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

import org.bioimageanalysis.icy.common.reflect.ClassUtil;
import org.bioimageanalysis.icy.common.string.StringUtil;
import org.bioimageanalysis.icy.extension.plugin.PluginDescriptor;
import org.bioimageanalysis.icy.extension.plugin.PluginLauncher;
import org.bioimageanalysis.icy.extension.plugin.PluginLoader;
import org.bioimageanalysis.icy.extension.plugin.interface_.PluginBundled;
import org.bioimageanalysis.icy.gui.component.button.IcyButton;
import org.bioimageanalysis.icy.gui.component.icon.SVGIcon;
import org.bioimageanalysis.icy.network.NetworkUtil;
import org.bioimageanalysis.icy.system.logging.IcyLogger;
import org.bioimageanalysis.icy.system.thread.ThreadUtil;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarDouble;
import plugins.adufour.vars.util.VarListener;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EzGUI extends EzDialog implements ActionListener {
    private EzPlug ezPlug;

    private Thread executionThread;

    private final JPanel jPanelBottom;

    private final JPanel jPanelButtons;

    private final IcyButton jButtonRun;

    private IcyButton jButtonStop;

    private final IcyButton jButtonSaveParameters;

    private final IcyButton jButtonLoadParameters;

    private final IcyButton jButtonHelp;

    private boolean jButtonsParametersVisible = true;

    private final JProgressBar jProgressBar;

    private final VarListener<Double> statusProgressListener = new VarListener<>() {
        @Override
        public void valueChanged(final Var<Double> source, final Double oldValue, final Double newValue) {
            ThreadUtil.invokeLater(() -> {
                //jButtonRun.setEnabled(newValue == 0);
                jButtonRun.setEnabled(newValue.equals(0d) || newValue.equals(1d));
                if (ezPlug instanceof EzStoppable)
                    //jButtonStop.setEnabled(newValue != 0);
                    jButtonStop.setEnabled(!newValue.equals(0d) && !newValue.equals(1d));

                if (!Double.isNaN(newValue))
                    jProgressBar.setValue((int) (Math.max(0, Math.min(1.0, newValue)) * 100));

                jProgressBar.setIndeterminate(Double.isNaN(newValue));
            });
        }

        @Override
        public void referenceChanged(final Var<Double> source, final Var<? extends Double> oldReference, final Var<? extends Double> newReference) {

        }
    };

    private final VarListener<String> statusMessageListener = new VarListener<>() {
        @Override
        public void valueChanged(final Var<String> source, final String oldValue, final String newValue) {
            ThreadUtil.invokeLater(() -> {
                jProgressBar.setString(newValue);
                jProgressBar.setStringPainted(!newValue.trim().isEmpty());
            });
        }

        @Override
        public void referenceChanged(final Var<String> source, final Var<? extends String> oldReference, final Var<? extends String> newReference) {

        }
    };

    public EzGUI(final EzPlug ezPlug) {
        super(ezPlug.getName(), false);

        this.ezPlug = ezPlug;

        jPanelBottom = new JPanel(new GridLayout(2, 1));

        jPanelButtons = new JPanel(new GridLayout(1, 5));
        jPanelBottom.add(jPanelButtons);

        jButtonRun = new IcyButton(SVGIcon.PLAY_CIRCLE);
        jButtonRun.setToolTipText("Start the plug-in...");
        jButtonRun.addActionListener(this);
        jPanelButtons.add(jButtonRun);

        if (ezPlug instanceof EzStoppable) {
            jButtonStop = new IcyButton(SVGIcon.STOP_CIRCLE);
            jButtonStop.setToolTipText("Stop the plug-in...");
            jButtonStop.setEnabled(false);
            jButtonStop.addActionListener(this);
            jPanelButtons.add(jButtonStop);
        }

        jButtonSaveParameters = new IcyButton(SVGIcon.SAVE);
        jButtonSaveParameters.setToolTipText("Save the parameters to a file...");
        jButtonSaveParameters.addActionListener(this);
        jPanelButtons.add(jButtonSaveParameters);

        jButtonLoadParameters = new IcyButton(SVGIcon.FOLDER);
        jButtonLoadParameters.setToolTipText("Load the parameters from a file...");
        jButtonLoadParameters.addActionListener(this);
        jPanelButtons.add(jButtonLoadParameters);

        jButtonHelp = new IcyButton(SVGIcon.HELP);
        jButtonHelp.setToolTipText("Access the online help for this plug-in...");
        jButtonHelp.addActionListener(this);
        jPanelButtons.add(jButtonHelp);

        jProgressBar = new JProgressBar();
        jProgressBar.setString("Running...");
        jProgressBar.setFont(new Font("default", Font.BOLD, 14));
        jPanelBottom.add(jProgressBar);

        ezPlug.getStatus().addProgressListener(statusProgressListener);
        ezPlug.getStatus().addMessageListener(statusMessageListener);

        getContentPane().add(jPanelBottom, BorderLayout.SOUTH);

        pack();

        setOptimalLocation();
    }

    /**
     * Sets the state of the "Run" button on the interface
     *
     * @param enabled Enables (or disables) the run button.
     */
    public void setRunButtonEnabled(final boolean enabled) {
        ThreadUtil.invokeLater(() -> jButtonRun.setEnabled(enabled));
    }

    /**
     * Sets the text of the run button.
     *
     * @param text Text presented on the button component.
     */
    public void setRunButtonText(final String text) {
        ThreadUtil.invokeLater(() -> jButtonRun.setText(text));
    }

    /**
     * Sets the text of the run button when it is running.
     *
     * @param text The string used to set the text of the run button.
     */
    public void setStopButtonText(final String text) {
        ThreadUtil.invokeLater(() -> jButtonStop.setText(text));
    }

    /**
     * Sets whether the action panel (buttons and progress bar) are visible or not.
     *
     * @param visible the new visibility state of the action panel
     */
    public void setActionPanelVisible(final boolean visible) {
        ThreadUtil.invokeLater(() -> jPanelBottom.setVisible(visible));
    }

    /**
     * Returns the variable used by the interface's progress bar. Any change to this variable will
     * automatically affect the corresponding progress bar
     *
     * @return the variable controlling the progress bar
     */
    public VarDouble getProgressBarValue() {
        return ezPlug.getStatus().getProgressVariable();
    }

    public void setProgressBarMessage(final String string) {
        ezPlug.getStatus().setMessage(string);
    }

    /**
     * Sets the progress indicator for this plug-in
     *
     * @param value <ul>
     *                     <li>0: no animation (default)</li>
     *                     <li>NaN: started (infinitely running)</li>
     *                     <li>from 0 to 1: a progress percentage indicator</li>
     *                     </ul>
     */
    public void setProgressBarValue(final double value) {
        ezPlug.getStatus().setCompletion(value);
    }

    public void setProgressBarVisible(final boolean visible) {
        ThreadUtil.invokeLater(() -> jProgressBar.setVisible(visible));
    }

    public void setParametersIOVisible(final boolean visible) {
        if (visible == jButtonsParametersVisible)
            return;

        ThreadUtil.invokeLater(() -> {
            if (visible) {
                jPanelButtons.add(jButtonLoadParameters);
                jPanelButtons.add(jButtonSaveParameters);
            }
            else {
                jPanelButtons.remove(jButtonLoadParameters);
                jPanelButtons.remove(jButtonSaveParameters);
            }
        });

        jButtonsParametersVisible = visible;
    }

    /**
     * Simulates a click on the run button (useful to execute the plug-in right after
     * initialization)
     */
    public void clickRun() {
        executionThread = new Thread(ezPlug.EzPlugProcess, ezPlug.getName());
        executionThread.start();
    }

    // ActionListener //

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource().equals(jButtonRun)) {
            clickRun();
        }
        else if (e.getSource().equals(jButtonStop)) {
            if (ezPlug instanceof EzStoppable)
                ((EzStoppable) ezPlug).stopExecution();
        }
        else if (e.getSource().equals(jButtonLoadParameters)) {
            final JFileChooser jfc = currentParametersPath == null ? new JFileChooser(FileSystemView.getFileSystemView()) : new JFileChooser(currentParametersPath);

            if (jfc.showOpenDialog(getContentPane()) != JFileChooser.APPROVE_OPTION)
                return;

            currentParametersPath = jfc.getCurrentDirectory();

            ezPlug.loadParameters(jfc.getSelectedFile());

        }
        else if (e.getSource().equals(jButtonSaveParameters)) {
            final JFileChooser jfc = currentParametersPath == null ? new JFileChooser(FileSystemView.getFileSystemView()) : new JFileChooser(currentParametersPath);

            if (jfc.showSaveDialog(getContentPane()) != JFileChooser.APPROVE_OPTION)
                return;

            currentParametersPath = jfc.getCurrentDirectory();

            ezPlug.saveParameters(jfc.getSelectedFile());
        }
        else if (e.getSource().equals(jButtonHelp)) {
            final String web = getMainPlugin(ezPlug.getDescriptor()).getWeb();
            if (!web.isBlank()) {
                final String s = getMainPlugin(ezPlug.getDescriptor()).getWeb() + "#documentation";
                IcyLogger.debug(EzGUI.class, "Open EzPlug help: " + s);
                NetworkUtil.openBrowser(s);
            }
        }
        else {
            throw new UnsupportedOperationException("Action event not recognized for source " + e.getSource());
        }
    }

    @Override
    public void onClosed() {
        super.onClosed();

        if (ezPlug == null)
            return;

        if (executionThread != null && executionThread.isAlive()) {
            ezPlug.stopExecution();

            // stop the execution if it was still running
            if (!(ezPlug instanceof EzStoppable)) {
                // special case: the process needs to be force-killed.
                // => use the dedicated interruption handler
                executionThread.setUncaughtExceptionHandler(new EzStoppable.ForcedInterruptionHandler(ezPlug.getDescriptor()));
                executionThread.interrupt();
            }
        }

        ezPlug.cleanFromUI();

        // remove all listeners

        jButtonRun.removeActionListener(this);
        if (jButtonStop != null)
            jButtonStop.removeActionListener(this);
        jButtonLoadParameters.removeActionListener(this);
        jButtonSaveParameters.removeActionListener(this);
        ezPlug.getStatus().removeProgressListener(statusProgressListener);
        ezPlug.getStatus().removeMessageListener(statusMessageListener);

        ezPlug = null;
    }

    @SuppressWarnings("resource")
    private static PluginDescriptor getMainPlugin(final PluginDescriptor bundledPlugin) {
        /*if (!bundledPlugin.isBundled())
            return bundledPlugin;*/

        try {
            // get the main plugin
            return PluginLoader.getPlugin(((PluginBundled) PluginLauncher.create(bundledPlugin)).getMainPluginClassName());
        }
        catch (final Throwable t) {
            // try alternate method
            final List<PluginDescriptor> allPlugins = PluginLoader.getPlugins();

            String packageName = bundledPlugin.getPackageName();

            while (!StringUtil.isEmpty(packageName)) {
                PluginDescriptor matchingPlugin = null;

                for (final PluginDescriptor p : allPlugins) {
                    // package name matches ? --> increase
                    if (p.getClassName().startsWith(packageName)) {
                        // several results ? --> no need to go further
                        if (matchingPlugin != null) {
                            // we use this to know that we have several matches (no way to recover origin plugin)
                            matchingPlugin = bundledPlugin;
                            // stop here
                            break;
                        }

                        // store matching plugin
                        matchingPlugin = p;
                    }
                }

                // several results --> cannot recover main plugin
                if (matchingPlugin == bundledPlugin)
                    return bundledPlugin;

                // we found the main plugin
                if (matchingPlugin != null)
                    // return origin plugin
                    return matchingPlugin;

                // continue with parent package
                packageName = ClassUtil.getPackageName(packageName);
            }
        }

        // not found
        return bundledPlugin;
    }
}
