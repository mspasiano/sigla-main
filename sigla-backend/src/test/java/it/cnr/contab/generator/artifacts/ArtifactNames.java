/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.generator.artifacts;

import it.cnr.contab.generator.model.GeneratorBean;
import it.cnr.contab.generator.properties.Generation;

/**
 * Incapsula i nomi degli artifatti da generare
 *
 * @author Marco Spasiano
 * @version 2.0 [22-Aug-2006] - utilizzo di BulkGeneratorDefaults
 */
public class ArtifactNames {
    public final static String
            JAVA_EXT = Generation.java_ext.trim(),
            BIXML_EXT = Generation.bixml_ext.trim(),
            BPIXML_EXT = Generation.bpixml_ext.trim();
    public final static int
            JAVA_BASE = 0,
            JAVA_KEY = 1,
            JAVA_HOME = 2,
            JAVA_BULK = 3,
            XML_BASE = 4,
            XML_KEY = 5,
            XML_BULK = 6,
            XML_INFO = 7;
    public final String[] artifactNames = new String[8];
    private GeneratorBean bean;

    public ArtifactNames(GeneratorBean bean) {
        this.bean = bean;

        artifactNames[JAVA_BASE] = getJavaName(Generation.java_base.trim());
        artifactNames[JAVA_KEY] = getJavaName(Generation.java_key.trim());
        artifactNames[JAVA_HOME] = getJavaName(Generation.java_home.trim());
        artifactNames[JAVA_BULK] = getJavaName(Generation.java_bulk.trim());

        artifactNames[XML_BASE] = getBulkPersistentInfoXmlName(Generation.xml_base);
        artifactNames[XML_KEY] = getBulkPersistentInfoXmlName(Generation.xml_key);
        artifactNames[XML_BULK] = getBulkPersistentInfoXmlName(Generation.xml_bulk);
        artifactNames[XML_INFO] = getBulkInfoXmlName(Generation.xml_info);
    }

    public static String getName(String fullName) {
        if (fullName.endsWith(JAVA_EXT)) {
            int from = fullName.length() - (JAVA_EXT.length() + 1);
            int index = fullName.lastIndexOf('.', from);
            if (index >= 0)
                return fullName.substring(index + 1);
        }
        return fullName;
    }

    // aggiunge al prefisso del nome il suffisso indicato e l'estensione
    private String getJavaName(String suffix) {
        return getPrefix(suffix.trim() + JAVA_EXT);
    }

    // aggiunge al prefisso del nome il suffisso indicato e l'estensione
    private String getBulkInfoXmlName(String suffix) {
        String result = bean.getPrefix();
        StringBuffer name = new StringBuffer();
        name.append(result.replace('.', '/') + suffix + BIXML_EXT);
        return name.toString();
    }

    private String getBulkPersistentInfoXmlName(String suffix) {
        String result = bean.getPrefix();
        StringBuffer name = new StringBuffer();
        name.append(result.replace('.', '/') + suffix + BPIXML_EXT);
        return name.toString();
    }

    /**
     * ritorna l'array dei nomi
     */
    public String[] getArtifactNames() {
        return artifactNames;
    }

    /**
     * ritorna il nome dell'artefatto corrispondente
     */
    public String getArtifactName(int i) {
        return artifactNames[i];
    }

    /**
     * ritorna il nome completo della classe (package + name)
     */
    public String getJavaClass(int i) {
        String temp = artifactNames[i];
        if (temp.endsWith(JAVA_EXT)) {
            int end = temp.length() - (JAVA_EXT.length());
            return temp.substring(0, end);
        } else {
            return null;
        }
    }

    private String getPrefix(String s) {
        StringBuffer sb = new StringBuffer();
        sb.append(bean.getPrefix());
        sb.append(s);
        return sb.toString();
    }

    /**
     * ritorna il generatore associato all'indice indicato
     */
    public ArtifactContents getGenerator(int i) {
        switch (i) {
            case JAVA_BASE:
                return new JavaBase(bean);
            case JAVA_BULK:
                return new JavaBulk(bean);
            case JAVA_HOME:
                return new JavaHome(bean);
            case JAVA_KEY:
                return new JavaKey(bean);
            case XML_BASE:
                return new XMLBasePersistentInfo(bean);
            case XML_BULK:
                return new XMLBulkPersistentInfo(bean);
            case XML_INFO:
                return new XMLBulkInfo(bean);
            case XML_KEY:
                return new XMLKeyPersistentInfo(bean);
            default:
                return null;
        }
    }

    public boolean isJavaName(int i) {
        if (i < 0 || i > artifactNames.length)
            return false;
        if (artifactNames[i].endsWith(JAVA_EXT))
            return true;
        return false;
    }

    public boolean isXMLName(int i) {
        if (i < 0 || i > artifactNames.length)
            return false;
        if (artifactNames[i].endsWith(BIXML_EXT) || artifactNames[i].endsWith(BPIXML_EXT))
            return true;
        return false;
    }
}
