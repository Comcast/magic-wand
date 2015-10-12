/**
 * Copyright 2015 Comcast Cable Communications Management, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.comcast.magicwand.spells.web.iexplore;

import java.util.Map;
import java.io.IOException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.magicwand.utils.SystemDetail;

public class IePlatformSpecifics {
    private static final Logger LOGGER = LoggerFactory.getLogger(IePlatformSpecifics.class);

    private static final String DRIVER_VERSION_ENV_NAME = "MAGICWAND_IE_VERSION";
    private static final String ARCH_ENV_NAME = "MAGICWAND_IE_ARCH";
    private static final String IE_XML_VERSIONS_URL = IePhoenixDriver.IE_HTTP_BASE_URL + "/?delimiter=/&prefix=";
    private static final String IE_XML_VERSIONS_XPATH = "/ListBucketResult/CommonPrefixes/Prefix/text()";
    private static final String IE_LAST_KNOWN_GOOD = "2.47";

    public static final String MAGICWAND_WINDOW_OS_OVERRIDE_PROP = "magicwand.iexplore.force_windows";
    private static final String OS_ARCH_PROP = "sun.arch.data.model";
    private static final String BIT_32 = "Win32";
    private static final String BIT_64 = "x64";

    public static final IePlatformSpecifics WIN32 = new IePlatformSpecifics(null, BIT_32);
    public static final IePlatformSpecifics WIN64 = new IePlatformSpecifics(null, BIT_64);

    private String arch;
    private String version;

    public IePlatformSpecifics(String version, String arch) {
        Map<String, String> env = System.getenv();

        this.version = version;
        this.arch = arch;

        if(null == this.version)
            this.version = env.get(DRIVER_VERSION_ENV_NAME);
        if(null == this.version)
            this.version = IePlatformSpecifics.getLatestVersion();
        if(null == this.version)
            this.version = IE_LAST_KNOWN_GOOD;
        if(null == this.arch)
            this.arch = env.get(ARCH_ENV_NAME);
        if(null == this.arch)
            this.arch = (System.getProperty(OS_ARCH_PROP).equals("32")) ? "Win32" : "x64";
    }

    private static String getLatestVersion() {
        String versionString = null;
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();

            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(IE_XML_VERSIONS_URL);

            NodeList nodes = (NodeList)xpath.evaluate(IE_XML_VERSIONS_XPATH, doc, XPathConstants.NODESET);

            double maxVersion = 0.0;

            for(int i = 0; i < nodes.getLength(); i++) {
                Node el = (Node)nodes.item(i);
                String text = el.getTextContent();
                text = text.substring(0, text.length()-1);

                try {
                    double version = Double.parseDouble(text);

                    if(version > maxVersion) {
                        maxVersion = version;
                        versionString = text;
                    }
                }
                catch(NumberFormatException nfe) { //don't do anything, we expect at least one error
                }
            }
        }
        catch(ParserConfigurationException | XPathExpressionException xpe) {
            throw new RuntimeException(xpe);
        }
        catch(SAXException | IOException err) {
            LOGGER.error("Error reading url[{}]", IE_XML_VERSIONS_URL, err);
        }
        return versionString;
    }

    public String getVersion() {
        return this.version;
    }

    public String getArch() {
        return this.arch;
    }

    /**
     * @return True if this is a valid platform for the IE platfrom configuration, false otherwise
     */
    public boolean isValid() {
        boolean rv = false;

        if(SystemDetail.deviceIsRunningWindows() || System.getProperty(MAGICWAND_WINDOW_OS_OVERRIDE_PROP).equalsIgnoreCase("true")) {
            //Windows always works with BIT32
            if(this.arch.equals(BIT_32)) {
                rv = true;
            }
            //If the desired arch and system arch are both 64-bit, then valid
            else if(System.getProperty(OS_ARCH_PROP).equalsIgnoreCase("64") && this.arch.equalsIgnoreCase(BIT_64)) {
                rv = true;
            }
            else {
                LOGGER.error("Invalid OS Architecture: {}", this.arch);
            }
        }
        else {
            LOGGER.error("Invalid operating system for InternetExplorerDriver.");
        }

        return rv;
    }
}
