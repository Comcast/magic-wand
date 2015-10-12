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
package com.comcast.magicwand.spells.appium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import com.comcast.magicwand.utils.VersionUtils;
import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.spells.appium.dawg.meta.MetaAppiumDevice.FamilyType;
import com.comcast.magicwand.spells.appium.dawg.utils.AppiumServerController;
import com.comcast.magicwand.enums.OSType;
import com.comcast.magicwand.exceptions.FlyingPhoenixException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

/**
 * A builder that instantiates an Appium Driver
 *
 * @author Dmitry Jerusalimsky
 *
 */
public class AppiumDriverBuilder {

    public static final String HOST                     = "appiumHost";
    public static final String PORT                     = "appiumPort";
    public static final String DEVICE_ID                = "appiumUdid";
    public static final String AUTOMATION_NAME          = "appiumAutomationName";
    public static final String IS_REAL_DEVICE           = "appiumIsRealDevice";
    public static final String FAMILY_TYPE              = "appiumFamilyType";
    public static final String PLATFORM_VERSION         = "appiumPlatformVersion";
    public static final String APPIUM_SERVER_CONTROLLER = "appiumServerController";

    private static final Logger LOGGER = LoggerFactory.getLogger(AppiumDriverBuilder.class);

    private String host;
    private Integer port;
    private FamilyType familyType;
    private Boolean isRealDevice;
    private String platformVersion;
    private DesiredCapabilities customCapabilities;
    private String automationName;
    private String deviceName;
    private String deviceID;
    private String platformName;
    private PhoenixDriverIngredients ingredients;
    private AppiumServerController asc;

    /**
     * Uses {@link PhoenixDriverIngredients} with driver configuration to construct Appium Driver
     *
     * @param ingredients Ingredients used to create the driver
     * @param capabilities Custom capabilities to use
     */
    public AppiumDriverBuilder(PhoenixDriverIngredients ingredients, DesiredCapabilities capabilities) {
        if (null != ingredients) {
            this.ingredients = ingredients;
            Map<String, Object> driverConfig = ingredients.getDriverConfigs();
            this.host = (String) driverConfig.get(HOST);
            this.port = (Integer) driverConfig.get(PORT);
            this.deviceID = (String) driverConfig.get(DEVICE_ID);
            this.automationName = (String) driverConfig.get(AUTOMATION_NAME);
            this.isRealDevice = (Boolean) driverConfig.get(IS_REAL_DEVICE);
            this.familyType = (FamilyType) driverConfig.get(FAMILY_TYPE);
            this.platformVersion = (String) driverConfig.get(PLATFORM_VERSION);
            this.asc = (AppiumServerController) driverConfig.get(APPIUM_SERVER_CONTROLLER);
        }

        this.customCapabilities = capabilities;
    }

    /**
     * Initializes {@link DesiredCapabilities} for the driver to be used
     *
     * @return Desired Capabilities generated both by metadata from Dawg House and custom capabilities
     */
    private DesiredCapabilities createCapabilities() {
        DesiredCapabilities rv = new DesiredCapabilities();

        LOGGER.debug("Adding 'platformVersion' capability from Dawg Metadata");
        rv.setCapability("platformVersion", this.platformVersion);

        LOGGER.debug("Adding 'platformName' capability from Dawg Metadata");
        rv.setCapability("platformName", this.platformName);

        LOGGER.debug("Adding 'deviceName' capability from Dawg Metadata");
        rv.setCapability("deviceName", this.deviceName);

        if (this.isRealDevice) {
            LOGGER.debug("Adding 'udid' capability from Dawg Metadata");
            rv.setCapability("udid", this.deviceID);
        }

        // customCapabilities should never be null at this point
        for (Entry<String, ?> entry : this.customCapabilities.asMap().entrySet()) {
            String curKey = entry.getKey();
            if (rv.asMap().containsKey(curKey)) {
                LOGGER.warn("Overwriting '" + curKey + "' key with value from Custom capabilities");
            }
            rv.setCapability(curKey, entry.getValue());
        }

        return rv;
    }

    /**
     * Creates an instance of {@link IOSDriver}. This method exists for testing purposes
     *
     * @param url URL of the remote driver
     * @param caps Capabilities to use with the driver
     * @return Instance of an {@link IOSDriver}
     */
    protected IOSDriver createIosDriver(URL url, DesiredCapabilities caps) {
        return new IOSDriver(url, caps);
    }

    /**
     * Creates an instance of {@link AndroidDriver}. This method exists for testing purposes
     *
     * @param url URL of the remote driver
     * @param caps Capabilities to use with the driver
     * @return Instance of an {@link AndroidDriver}
     */
    protected AndroidDriver createAndroidDriver(URL url, DesiredCapabilities caps) {
        return new AndroidDriver(url, caps);
    }

    /**
     * Validates passed in arguments and initializes missing values that are implied by the passed in arguments
     *
     * @throws FlyingPhoenixException If required arguments are missing or are incompatible
     */
    protected void validateRequired() throws FlyingPhoenixException {
        if (null == this.host) {
            throw new FlyingPhoenixException("Appium Host is a required argument");
        }

        if (null == this.port) {
            this.port = 4723;
        }

        validateFamilyType();

        this.isRealDevice = (null == this.isRealDevice) ? Boolean.TRUE : this.isRealDevice;

        validateDeviceId();

        validatePlatformVersion();

        validateRealDevice();

        this.platformName = (this.familyType == FamilyType.ANDROID) ? "Android" : "iOS";

        validateAutomationName();

        if (null == this.asc) {
            this.asc = new AppiumServerController();
        }
    }

    private void validateAutomationName() {
        if ((this.familyType == FamilyType.ANDROID) && (null == this.automationName)) {
            this.automationName = (VersionUtils.compare(this.platformVersion, "4.4") < 0) ? "Selendroid"
                    : "Appium";
        }
    }

    private void validateDeviceId() throws FlyingPhoenixException {
        if (this.isRealDevice && (null == this.deviceID)) {
            throw new FlyingPhoenixException("Device ID is a required parameter for a real device testing");
        }
    }

    private void validateRealDevice() {
        if (false == this.isRealDevice) {
            if (this.familyType == FamilyType.ANDROID) {
                this.deviceID = "Android Emulator";
            }
            else {
                this.deviceID = (this.familyType == FamilyType.IPAD) ? "iPad Simulator" : "iPhone Simulator";
            }
        }
    }

    private void validatePlatformVersion() throws FlyingPhoenixException {
        if (null == this.platformVersion) {
            if ((this.familyType == FamilyType.ANDROID) && (null == this.automationName)) {
                throw new FlyingPhoenixException(
                        "Either platform version or automation name is required for Android devices");
            }
            else if (this.familyType == FamilyType.IPHONE || this.familyType == FamilyType.IPAD) {
                throw new FlyingPhoenixException("Platform Version is a required argument");
            }
        }
    }

    private void validateFamilyType() throws FlyingPhoenixException {
        if (null == this.familyType) {
            if (null == this.ingredients || null == this.ingredients.getMobileOS()) {
                throw new FlyingPhoenixException("FamilyType is a required argument");
            }
            else {
                OSType mos = this.ingredients.getMobileOS().getType();
                if (OSType.ANDROID == mos) {
                    this.familyType = FamilyType.ANDROID;
                }
                else if (OSType.IPAD == mos || OSType.IPHONE == mos) {
                    this.familyType = FamilyType.IPHONE;
                }
                else {
                    throw new FlyingPhoenixException("Invalid mobile os: " + mos);
                }
            }
        }
    }

    /**
     * Creates Appium URL for RemoteWebDriver to connect to
     *
     * @return URL for appium server
     * @throws FlyingPhoenixException If URL is malformed
     */
    protected URL getAppiumURL() throws FlyingPhoenixException {

        URL appiumURL = null;
        try {
            appiumURL = new URL("http", this.host, this.port, "/wd/hub");
        }
        catch (MalformedURLException e) {
            LOGGER.error("Exception is: " + e.getMessage());
            String msg = "There was an error while creating appium URL.";
            throw new FlyingPhoenixException(msg, e);
        }

        return appiumURL;
    }

    /**
     * Validates that Appium server is running and has no sessions
     * @param url URL of an appium server
     * @throws FlyingPhoenixException If server is not running or if it has active session(s)
     */
    protected void validateAppiumState(URL url) throws FlyingPhoenixException {
        if (false == this.asc.checkServerState(url)) {
            throw new FlyingPhoenixException("URL '" + url + "' is not reachable. Could be that Appium server is not running");
        }

        if (this.asc.hasRunningSessions(url)) {
            throw new FlyingPhoenixException("Appium instance has at least one active session.");
        }
    }

    /**
     * Creates an instance of {@link IOSDriver} or {@link AndroidDriver} wrapped in the
     * {@link AbstractAppiumPhoenixDriver}
     *
     * @return Instance of an {@link AbstractAppiumPhoenixDriver}
     * @throws FlyingPhoenixException If data validation fails or URL is in bad form
     */
    public AbstractAppiumPhoenixDriver createDriver() throws FlyingPhoenixException {
        // validate and initialize inputs
        validateRequired();

        DesiredCapabilities caps = createCapabilities();

        URL appiumUrl = getAppiumURL();

        validateAppiumState(appiumUrl);

        AbstractAppiumPhoenixDriver rv = null;
        if (FamilyType.ANDROID == this.familyType) {
            AndroidDriver driver = createAndroidDriver(appiumUrl, caps);
            rv = new AppiumAndroidPhoenixDriver(driver);
        }
        else if (FamilyType.IPAD == this.familyType || FamilyType.IPHONE == this.familyType) {
            IOSDriver driver = createIosDriver(appiumUrl, caps);
            rv = new AppiumIosPhoenixDriver(driver);
        }
        else {
            throw new FlyingPhoenixException("Invalid family type: " + this.familyType);
        }

        return rv;
    }
}
