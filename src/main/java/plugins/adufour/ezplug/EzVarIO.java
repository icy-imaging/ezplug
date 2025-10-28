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

import org.bioimageanalysis.icy.io.xml.XMLUtil;
import org.bioimageanalysis.icy.system.logging.IcyLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import plugins.adufour.vars.lang.Var;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class allowing EzPlug to load/save parameters from/to disk in XML format
 *
 * @author Alexandre Dufour
 */
class EzVarIO {
    /**
     * Legacy method that saves a set of parameters to XML file
     *
     * @param ezVarMap Map that contains EzVars to be saved
     * @param f        File to be written
     */
    static synchronized void save(final EzPlug source, final HashMap<String, EzVar<?>> ezVarMap, final File f) {
        final Document xml = XMLUtil.createDocument(false);
        final Element root = XMLUtil.createRootElement(xml, "Parameters");

        for (final String id : ezVarMap.keySet()) {
            final EzVar<?> var = ezVarMap.get(id);

            try {
                final Element parameterNode = XMLUtil.addElement(root, "parameter");
                XMLUtil.setAttributeValue(parameterNode, Var.XML_KEY_ID, id);
                var.getVariable().saveToXML(parameterNode);
            }
            catch (final UnsupportedOperationException e) {
                IcyLogger.warn(EzVarIO.class, "Variable " + id + " has not been saved (unsupported variable type)");
            }
        }

        if (!XMLUtil.saveDocument(xml, f)) throw new EzException(source, "unable to save parameters", true);
    }

    /**
     * Legacy method that loads a parameter set from an XML file
     *
     * @param f        File to be written
     * @param ezVarMap Map that contains EzVars to be loaded
     * @throws EzException if XML cannot be read
     */
    static synchronized void load(final EzPlug source, final File f, final HashMap<String, EzVar<?>> ezVarMap) throws EzException {
        final Document xml = XMLUtil.loadDocument(f);

        if (xml == null) throw new EzException(source, "unable to load parameter file", true);

        final Element root = XMLUtil.getRootElement(xml);
        final ArrayList<Element> elements = XMLUtil.getElements(root);
        for (final Element element : elements) {
            final String id = XMLUtil.getAttributeValue(element, Var.XML_KEY_ID, null);
            if (id != null && ezVarMap.containsKey(id)) {
                ezVarMap.get(id).getVariable().loadFromXML(element);
            }
        }
    }
}
