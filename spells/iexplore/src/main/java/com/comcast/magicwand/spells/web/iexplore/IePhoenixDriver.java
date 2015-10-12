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

import java.io.File;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.net.URL;
import java.util.Map;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.comcast.magicwand.utils.VersionUtils;
import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.web.AbstractWebPhoenixDriver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Web Driver for controlling IE
 *
 * @author Trent Schmidt
 *
 */
public class IePhoenixDriver extends AbstractWebPhoenixDriver<InternetExplorerDriver> {
    private static final Logger LOGGER = LoggerFactory.getLogger(IePhoenixDriver.class);

    private static final String IE_DRIVER_VERSION_KEY = "ieDriverVersion";
    private static final String IE_DRIVER_ARCH_KEY = "ieDriverArch";

    static final String IE_HTTP_BASE_URL = "https://selenium-release.storage.googleapis.com";
    private static final String IE_HTTP_DRIVER_PATH_FORMAT = IE_HTTP_BASE_URL + "/%s/IEDriverServer_%s_%s.zip"; //(version, arch, fullVersion)
    private static final String IE_ZIP_FILE_FORMAT = "IEDriverServer_%s_%s.zip"; //dump to ${user.dir}/target/drivers/iexplore/%s/IEDriverServer_%s_%s.zip
    private static final String IE_EXE_FILE_NAME = "IEDriverServer.exe";
    private static final String IE_XML_DRIVER_LISTING_URL = "https://selenium-release.storage.googleapis.com";
    private static final String IE_XML_DRIVER_LISTING_XPATH = "/ListBucketResult/Contents/Key/text()";

    private WebDriver webDriver;

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDriver getDriver() {
        return this.webDriver;
    }

    /**
     * Creates an instance of a driver
     * @param dc Desired Capabilities to use when creating a driver
     *
     * @return driver
     */
    protected InternetExplorerDriver createDriver(final DesiredCapabilities dc) {
        InternetExplorerDriver ied = null;

        if (null == dc) {
            ied = new InternetExplorerDriver();
        }
        else {
            ied = new InternetExplorerDriver(dc);
        }

        return ied;
    }

    /**
     * Determines if lhs > rhs in a version-wise comparison.
     *
     * If the lhs is null, then it always returns that rhs is greater.  If rhs is null anyway, no changes are made.
     *
     * @param current The current version.
     * @param test The version that is being tested.
     * @return whether test is greater than current
     */
    private static boolean versionGreater(String lhs, String rhs) {
        return VersionUtils.compare(lhs, rhs) < 1;
    }

    private static void extract(File src, String dstDir) throws IOException {
        ZipInputStream zis = null;
        ZipEntry entry;

        try {
            zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(src)));

            while(null != (entry = zis.getNextEntry())) {
                File dst = Paths.get(dstDir, entry.getName()).toFile();
                FileOutputStream fos = new FileOutputStream(dst);

                try {
                    IOUtils.copy(zis, fos);
                    fos.close();
                }
                finally {
                    IOUtils.closeQuietly(fos);
                }
            }

            zis.close();
        }
        finally {
            IOUtils.closeQuietly(zis);
        }
    }

    private IePlatformSpecifics createIePlatformSpecifics(String version, String arch) {
        return new IePlatformSpecifics(version, arch);
    }

    /**
     * {@inheritDoc}
     */
    public boolean verify(PhoenixDriverIngredients i) {
        boolean rv = false;
        Map<String, Object> configs = i.getDriverConfigs();

        LOGGER.debug("[version_property, arch_property] = [{}, {}]", (String)configs.get(IE_DRIVER_VERSION_KEY), (String)configs.get(IE_DRIVER_ARCH_KEY));

        IePlatformSpecifics ips = this.createIePlatformSpecifics((String)configs.get(IE_DRIVER_VERSION_KEY), (String)configs.get(IE_DRIVER_ARCH_KEY));

        if (!ips.isValid()) {
            LOGGER.error("The IePlatformSpecifics retrieved are not valid.");
            return false;
        }

        try {
            XPath xpath = XPathFactory.newInstance().newXPath();

            String pwd = System.getProperty("user.dir");
            String version = ips.getVersion();
            String arch = ips.getArch();

            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(IE_XML_DRIVER_LISTING_URL);

            NodeList nodes = (NodeList)xpath.evaluate(IE_XML_DRIVER_LISTING_XPATH, doc, XPathConstants.NODESET);

            String highestVersion = null;
            String needle = version + "/IEDriverServer_" + arch + "_";

            for(int idx = 0; idx < nodes.getLength(); idx++) {
                Node n = nodes.item(idx);

                String text = n.getTextContent();

                if(text.startsWith(needle)) {
                    text = text.substring(needle.length(), text.length());

                    if(IePhoenixDriver.versionGreater(highestVersion, text)) {
                        highestVersion = text;
                    }
                }
            }

            if (null != highestVersion) {
                highestVersion = FilenameUtils.removeExtension(highestVersion);

                URL url = new URL(String.format(IE_HTTP_DRIVER_PATH_FORMAT, version, arch, highestVersion));
                String zipName = String.format(IE_ZIP_FILE_FORMAT, arch, highestVersion);

                File ieSaveDir = new File(Paths.get(pwd, "target", "drivers", "iexplore", version).toString());

                LOGGER.debug("Will read from \"{}\"", url);
                File zipFile = new File(Paths.get(pwd, "target", "drivers", "iexplore", version, zipName).toString());
                FileUtils.copyURLToFile(url, zipFile);

                extract(zipFile, ieSaveDir.getAbsolutePath());

                File exe = Paths.get(pwd, "target", "drivers", "iexplore", version, IE_EXE_FILE_NAME).toFile();

                if(exe.exists()) {
                    exe.setExecutable(true);
                    systemSetProperty("webdriver.ie.driver", exe.getAbsolutePath());
                    this.webDriver = this.createDriver(i.getDriverCapabilities());
                    rv = true;
                }
                else {
                    LOGGER.error("Extracted zip archive did nto contain \"{}\".", IE_EXE_FILE_NAME);
                }
            } else {
                LOGGER.error("Unable to find any IE Drivers from [{}]", IE_XML_DRIVER_LISTING_XPATH);
            }
        }
        catch(ParserConfigurationException | SAXException | XPathExpressionException err) {
            throw new RuntimeException(err);
        }
        catch(IOException ioe) {
            LOGGER.error("IO failure: {}", ioe);
        }
        return rv;
    }
}
