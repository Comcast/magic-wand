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
package com.comcast.magicwand.spells.web.chrome;

import java.net.URL;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

import com.comcast.magicwand.utils.SystemDetail;
import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.web.AbstractWebPhoenixDriver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Web Driver for controlling Chrome browser
 *
 * @author Trent Schmidt
 *
 */
public class ChromePhoenixDriver extends AbstractWebPhoenixDriver<ChromeDriver> {
    private static final Logger LOG = LoggerFactory.getLogger(ChromePhoenixDriver.class);

    private static final String LATEST_KNOWN_VERSION = "2.19";
    private static final String DRIVER_BASE_URL = "https://chromedriver.storage.googleapis.com";
    private static final String DRIVER_URL_FORMAT = DRIVER_BASE_URL+"/%s/chromedriver%s.zip";
    private static final String LATEST_RELEASE_URL = DRIVER_BASE_URL+"/LATEST_RELEASE";

    public static final String CHROME_DRIVER_VERSION = "chromeDriverVersion";

    private ChromeDriver webDriver;

    /**
     * Gets the latest known version of chromedriver.
     * 
     * @return Latest known version
     */
    public static String getLatestVersion() {
        String latest = LATEST_KNOWN_VERSION; 

        try {
            URL url = new URL(LATEST_RELEASE_URL);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(url.openStream(), baos);
            latest = baos.toString();
        } catch (IOException e) {
            LOG.error("Error retrieving url[{}]: {}", LATEST_RELEASE_URL, e);
        }
        return latest;
    }

    /**
     * Get current OS Type
     * @return String representation of OS Type. Returned values include:<br>
     *     <ul>
     *         <li>_mac32 - for Mac</li>
     *         <li>_win32 - for Windows</li>
     *         <li>_linux32 - for Linux</li>
     *         <li>_linux64 - for Linux x64</li>
     *         <li>empty string - if OS type could not be determined</li>
     *     </ul>
     */
    public static String getCurrentOSType() {
        String osArch = System.getProperty("sun.arch.data.model");

        if(SystemDetail.deviceIsRunningMac())
            return "_mac32";
        else if(SystemDetail.deviceIsRunningWindows())
            return "_win32";
        else if(SystemDetail.deviceIsLinux()) {
            if(osArch.equals("32"))
                return "_linux32";
            else if(osArch.equals("64"))
                return "_linux64";
            else {
                LOG.error("Unsupported OSArch");
                return "";
            }
        }
        else {
            LOG.error("Unsupported OSName");
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDriver getDriver() {
        return this.webDriver;
    }

    /**
     * Creates a ChromePlatformSpecifics for the target chrome driver
     *
     * @return {@link ChromePlatformSpecifics}
     */
    ChromePlatformSpecifics createChromePlatformSpecifics(String chromeDriverVersion) {
        return new ChromePlatformSpecifics(chromeDriverVersion);
    }

    /**
     * Creates an instance of a {@link ChromeDriver}
     * @param dc Desired Capabilities used to create chrome driver
     *
     * @return {@link ChromeDriver}
     */
    protected ChromeDriver createDriver(DesiredCapabilities dc) {
        LOG.debug("Desired Capabilities are: {}", dc);
        ChromeDriver cd = null;
        if (null == dc) {
            cd = new ChromeDriver();
        }
        else {
            cd = new ChromeDriver(dc);
        }

        return cd;
    }

    /**
     * {@inheritDoc}
     */
    public boolean verify(PhoenixDriverIngredients i) {
        Map<String, Object> driverConfigs = i.getDriverConfigs();

        ChromePlatformSpecifics cps = createChromePlatformSpecifics((String)driverConfigs.get(CHROME_DRIVER_VERSION));

        LOG.debug("Using cps[{}, {}, {}]", cps.getSuffix(), cps.getExtension(), cps.getVersion());

        String version = cps.getVersion();
        String osName = cps.getSuffix();
        String pwd = System.getProperty("user.dir");

        //determine the driver name
        String driverName = String.format("chromedriver%s-%s", osName, version);
        String driverTargetDir = Paths.get(pwd, "target", "drivers").toString();
        File driver = Paths.get(driverTargetDir, driverName + cps.getExtension()).toFile();

        if (!driver.exists()) {
            LOG.debug("No cached chromedriver driver found");

            /* Download chromedriver zip */
            File zipDriver = Paths.get(driverTargetDir, driverName+".zip").toFile();
            if (!zipDriver.exists()) {
                String driverURL = String.format(DRIVER_URL_FORMAT, version, osName);
                try {
                    URL driverZipURL = new URL(driverURL);
                    LOG.debug("Will download driver package [{}]", driverURL);

                    FileUtils.copyURLToFile(driverZipURL, zipDriver);
                } catch (IOException e) {
                    LOG.error("Error downloading [{}]: {}", driverURL, e);
                    return false;
                }
            }

            /* Exctract chromedriver zip */
            try {
                extractZip(zipDriver, driverTargetDir);
            } catch (IOException e) {
                LOG.error("Error extracting [{}]: {}", zipDriver, driverTargetDir, e);
                return false;
            }

            /* For caching purposes, rename chromedriver to keep os and version info */
            File genericDriver = Paths.get(driverTargetDir, "chromedriver" + cps.getExtension()).toFile();
            try {
                FileUtils.moveFile(genericDriver, driver);
            } catch (IOException e) {
                LOG.error("Error moving [{}] to [{}]: {}", genericDriver, driver, e);
                return false;
            }

            driver.setExecutable(true);
        }

        LOG.debug("Will use driver at [{}]", driver);
        systemSetProperty("webdriver.chrome.driver", driver.toString());

        this.webDriver = this.createDriver(i.getDriverCapabilities());

        return true;
    }

    private void extractZip(File sourceZipFile, String destinationDir) throws IOException {
        LOG.debug("Extracting [{}] to dir [{}]", sourceZipFile, destinationDir);
        ZipInputStream zis = null;
        ZipEntry entry;

        try {
            zis = new ZipInputStream(FileUtils.openInputStream(sourceZipFile));

            while (null != (entry = zis.getNextEntry())) {
                File dst = Paths.get(destinationDir, entry.getName()).toFile();

                FileOutputStream output = FileUtils.openOutputStream(dst);
                try {
                    IOUtils.copy(zis, output);
                    output.close();
                } finally {
                    IOUtils.closeQuietly(output);
                }
            }
            zis.close();
        } finally {
            IOUtils.closeQuietly(zis);
        }
    }
}
