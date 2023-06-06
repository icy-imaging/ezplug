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

import icy.system.thread.ThreadUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Class defining a text label for use with EzPlugs.
 *
 * @author Alexandre Dufour
 */
public class EzLabel extends EzComponent {
    private JEditorPane label;

    private String text;

    private Color textColor;

    private int labelWidth = 200;

    /**
     * Creates a new label with given text
     *
     * @param text the text to display. <b>Important note for HTML-formatted text:</b>
     *             <ul>
     *             <li>If no <code>&lt;body&gt;</code> tag is found, one is generated to account for
     *             other formatting properties of this class (e.g. text color or label width)</li>
     *             <li>If a <code>&lt;body&gt;</code> tag already exists, no extra formatting is
     *             performed, leaving full freedom to the developer.</li>
     *             </ul>
     */
    public EzLabel(final String text) {
        this(text, null);
    }

    /**
     * Creates a new label with given text and color
     *
     * @param text      The text to display.
     *                  <b>Important note for HTML-formatted text:</b>
     *                  <ul>
     *                  <li>If no <code>&lt;body&gt;</code> tag is found, one is generated to account for
     *                  other formatting properties of this class (e.g. text color or label width)</li>
     *                  <li>If a <code>&lt;body&gt;</code> tag already exists, no extra formatting is
     *                  performed, leaving full freedom to the developer.</li>
     *                  </ul>
     * @param textColor The default text color. NB: this parameter is not used if the specified text is in
     *                  HTML format.
     */
    public EzLabel(final String text, final Color textColor) {
        super("label");

        this.text = text;
        this.textColor = textColor;

        ThreadUtil.invoke(() -> {
            label = new JEditorPane("text/html", "");
            label.setEditable(false);
            label.setOpaque(false);
            label.setMargin(new Insets(0, 2, 0, 2));
            updateLabel();
        }, !SwingUtilities.isEventDispatchThread());
    }

    /**
     * Sets the text of this label. This text can be given unformatted or in HTML format.
     * <p>
     * <b>Important note for HTML-formatted text:</b>
     * <ul>
     * <li>If no <code>&lt;body&gt;</code> tag is found, one is generated to account for other
     * formatting properties of this class (e.g. text color or label width)</li>
     * <li>If a <code>&lt;body&gt;</code> tag already exists, no extra formatting is performed,
     * leaving full freedom to the developer.</li>
     * </ul>
     *
     * @param text the text to display.
     *             <p>
     *             <b>Important note for HTML-formatted text:</b>
     *             <ul>
     *             <li>If no <code>&lt;body&gt;</code> tag is found, one is generated to account for
     *             other formatting properties of this class (e.g. text color or label width)</li>
     *             <li>If a <code>&lt;body&gt;</code> tag already exists, no extra formatting is
     *             performed, leaving full freedom to the developer.</li>
     *             </ul>
     */
    public void setText(final String text) {
        this.text = text;
        updateLabel();
    }

    /**
     * Overrides the default label width (200px) with the specified value
     *
     * @param width the new label width in pixels
     */
    public void setLabelWidth(final int width) {
        this.labelWidth = width;
        updateLabel();
    }

    /**
     * Sets the color of the text in this label. Setting the color overrides the default color
     * computed for the current look&amp;feel and skin. To revert to this default color, use
     * <code>setColor(null)</code>
     *
     * @param textColor the new text color
     */
    public void setColor(final Color textColor) {
        this.textColor = textColor;
        updateLabel();
    }

    @Override
    protected void addTo(final Container container) {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 10, 2, 10);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        container.add(label, gbc);
    }

    @Override
    public void setToolTipText(final String text) {
        label.setToolTipText(text);
    }

    private static String convertToHTML(final String text, final Color textColor, final int labelWidth) {
        if (text.trim().toLowerCase().startsWith("<html>") && text.toLowerCase().contains("<body")) {
            // A "body" tag already exists => let's assume the developer knows what he's doing
            return text;
        }

        // Convert the string to HTML
        String html = text.trim();
        if (!html.toLowerCase().startsWith("<html>")) {
            // The text seems unformatted
            // Convert it to HTML (managing potential "new line" characters)
            html = "<html>" + html.replace("\n", "<br>") + "</html>";
        }

        // Now that the text is HTML, adjust its style (via CSS)
        // Warning: this is ugly (should use Document.getStyleSheet() instead, etc.)

        final int r, g, b;
        r = textColor.getRed();
        g = textColor.getGreen();
        b = textColor.getBlue();

        String style = "max-width: " + labelWidth + "px; ";
        style += "font: helvetica; ";
        style += "color: rgb(" + r + "," + g + "," + b + "); ";

        html = html.replace("<html>", "<html><body style='" + style + "'>");
        html = html.replace("</html>", "</body></html>");

        return html;
    }

    private void updateLabel() {
        // Check which color to use
        Color color = textColor;

        if (color == null) {
            // Use the default from the current look&feel
            color = label.getForeground();
        }

        final String html = convertToHTML(text, color, labelWidth);
        label.setText(html);
    }
}
