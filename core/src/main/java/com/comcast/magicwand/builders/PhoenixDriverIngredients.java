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
package com.comcast.magicwand.builders;

import java.util.HashMap;
import java.util.Map;

import com.comcast.cookie.CookieHandler;
import com.comcast.magicwand.utils.SystemDetail;
import com.comcast.magicwand.drivers.PhoenixDriver;
import com.comcast.magicwand.enums.DesktopOS;
import com.comcast.magicwand.enums.MobileOS;
import com.comcast.magicwand.enums.OSType;

import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Class that is responsible for defining all variables required to build a {@link PhoenixDriver} object
 *
 * @author Dmitry Jerusalimsky
 *
 */
public class PhoenixDriverIngredients {

    private MobileOS mos;
    private String browserType;
    private DesktopOS dos;
    private DesiredCapabilities capabilities = new DesiredCapabilities();

    private Map<String, Object> driverConfig;

    private CookieHandler cookieHandler;

    /**
     * Adds a custom configuration parameter for PhoenixDriver construction
     *
     * @param settingName Name of a configuration parameter (i.e. <code>sauceVPN</code>)
     * @param value Value of a parameter (i.e. <code>true</code>)
     * @return this builder
     */
    public PhoenixDriverIngredients addCustomDriverConfiguration(String settingName, Object value) {
        initDriverConfig();

        this.driverConfig.put(settingName, value);

        return this;
    }

    /**
     * Defines which Host OS is being used.
     *
     * @param os Type of OS used
     * @return this builder
     */
    public PhoenixDriverIngredients addDesktopOS(DesktopOS os) {
        this.dos = os;

        return this;
    }

    /**
     * Defines which Host OS is being used.
     *
     * @param os Type of OS used
     * @return this builder
     */
    public PhoenixDriverIngredients addMobileOS(MobileOS os) {
        this.mos = os;

        return this;
    }

    /**
     * Specifies {@link BrowserType} to be instantiated for <em>web testing</em>
     *
     * @param browser Type of a browser to be used
     * @return this builder
     */
    public PhoenixDriverIngredients addBrowser(String browser) {
        this.browserType = browser;

        return this;
    }

    /**
     * Specifies {@link CookieHandler} to use for cookie clearing
     *
     * @param cookieHandler Cookie hander to use when clearing cookies
     * @return this builder
     */
    public PhoenixDriverIngredients withCookieHandler(CookieHandler cookieHandler) {
        this.cookieHandler = cookieHandler;

        return this;
    }

    /**
     * Gets cookie handler
     *
     * @return Cookie handler specified by {@link PhoenixDriverIngredients#withCookieHandler(CookieHandler)}
     */
    public CookieHandler getCookieHandler() {
        return this.cookieHandler;
    }

    /**
     * Adds a capability to the pool of {@link DesiredCapabilities} to be used by the {@link PhoenixDriver}
     *
     * @param capabilityName Name of the capability to add
     * @param value Value of capability
     * @return this builder
     */
    public PhoenixDriverIngredients addDriverCapability(String capabilityName, Object value) {
        this.capabilities.setCapability(capabilityName, value);

        return this;
    }

    /**
     * Gets Mobile OS required for test
     *
     * @return the OS
     */
    public MobileOS getMobileOS() {
        return mos;
    }

    /**
     * Gets Desktop OS required for the test
     *
     * @return the OS
     */
    public DesktopOS getDesktopOS() {
        if (null == this.dos) {
            OSType ost = getOSType();
            this.dos = new DesktopOS(ost);
        }

        return this.dos;
    }

    /**
     * Gets {@link DesiredCapabilities} used to create driver
     *
     * @return Desired capabilities
     */
    public DesiredCapabilities getDriverCapabilities() {
        return this.capabilities;
    }

    /**
     * Gets browser type required for the test
     *
     * @return Name of the browser
     */
    public String getBrowser() {
        if (null != this.browserType && !this.browserType.trim().isEmpty()) {
            return browserType;
        }

        switch (getDesktopOS().getType()) {
        case MAC:
            this.browserType = BrowserType.SAFARI;
            break;
        case WINDOWS:
            this.browserType = BrowserType.IE;
            break;
        default:
            this.browserType = BrowserType.CHROME;

        }
        return browserType;
    }

    /**
     * Initializes driver configuration map
     */
    private void initDriverConfig() {
        if (null == this.driverConfig) {
            this.driverConfig = new HashMap<String, Object>();
        }
    }

    /**
     * Gets custom driver configurations
     *
     * @return Map containing all custom driver configurations
     */
    public Map<String, Object> getDriverConfigs() {
        initDriverConfig();
        return this.driverConfig;
    }

    protected OSType getOSType() {
        if (SystemDetail.deviceIsLinux()) {
            return OSType.LINUX;
        }
        else if (SystemDetail.deviceIsUnix()) {
            return OSType.UNIX;
        }
        else if (SystemDetail.deviceIsRunningMac()) {
            return OSType.MAC;
        }
        else if (SystemDetail.deviceIsRunningWindows()) {
            return OSType.WINDOWS;
        }

        return null;
    }

    /**
     * Validates that current set of ingredients is valid
     *
     * @return PhoenixDriverIngredients if valid, null otherwise
     */
    public PhoenixDriverIngredients verify() {
        PhoenixDriverIngredients rv = this;

        // verify browser type
        if (null != this.browserType && this.browserType.trim().isEmpty()) {
            rv = null;
        }

        return rv;
    }
}
