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
package com.comcast.magicwand.wizards;

import java.util.Map;
import java.util.Map.Entry;

import com.comcast.cookie.CookieHandler;
import com.comcast.cookie.handlers.GeneralCookieHandler;
import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.PhoenixDriver;
import com.comcast.magicwand.enums.DesktopOS;
import com.comcast.magicwand.enums.MobileOS;
import com.comcast.magicwand.enums.OSType;

import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * A base class for all wizards. Contains some common functionality that all wizards should have
 *
 */
public abstract class AbstractWizard {

    protected PhoenixDriverIngredients ingredients;
    protected DesktopOS desktopOS;
    protected MobileOS mobileOS;
    protected String browserType;
    protected DesiredCapabilities customCapabilities;
    protected Map<String, Object> driverConfig;
    protected CookieHandler cookieHanlder;

    /**
     * Initializes some variables to be used by the wizards
     *
     * @param ingredients A set of ingredients used to define the driver
     * @param initCapabilities Specifies whether capabilities should be initialized / created
     */
    protected AbstractWizard(PhoenixDriverIngredients ingredients, boolean initCapabilities) {
        this.ingredients = ingredients;
        this.customCapabilities = ingredients.getDriverCapabilities();

        this.desktopOS = ingredients.getDesktopOS();
        this.mobileOS = ingredients.getMobileOS();
        this.browserType = ingredients.getBrowser();
        this.driverConfig = ingredients.getDriverConfigs();

        this.cookieHanlder = ingredients.getCookieHandler();

        if (null == cookieHanlder) {
            this.cookieHanlder = new GeneralCookieHandler();
        }

        if (initCapabilities) {
            createCapabilities();
        }
    }

    /**
     * Creates a {@link PhoenixDriver} object from the ingredients specified
     *
     * @return Instance of a driver
     * @throws Throwable If there are any issues while creating a driver
     */
    public abstract PhoenixDriver createDriver() throws Throwable;

    /**
     * Checks whether mobile device is requested
     *
     * @return True if mobile OS was specified; False otherwise
     */
    protected boolean isMobile() {
        return null != this.mobileOS;
    }

    /**
     * Creates default desired capabilities based on a device type
     */
    protected void createCapabilities() {
        DesiredCapabilities curCaps = null;
        if (isMobile()) {
            OSType mos = this.mobileOS.getType();
            switch (mos) {
                case ANDROID:
                    curCaps = DesiredCapabilities.android();
                    break;
                case IPAD:
                    curCaps = DesiredCapabilities.ipad();
                    break;
                case IPHONE:
                    curCaps = DesiredCapabilities.iphone();
                    break;
                default:
                    // nothing to do here
                    break;
            }
        }
        else {
            if (BrowserType.FIREFOX.equals(browserType)) {
                curCaps = DesiredCapabilities.firefox();
            }
            else if (BrowserType.SAFARI.equals(browserType)) {
                curCaps = DesiredCapabilities.safari();
            }
            else if (BrowserType.IE.equals(browserType)) {
                curCaps = DesiredCapabilities.internetExplorer();
            }
            else if (BrowserType.CHROME.equals(browserType)) {
                curCaps = DesiredCapabilities.chrome();
            }
        }

        // we need to iterate over each capability because
        // method DesiredCapabilities#merge is invoking
        // HashMap#putAll method which overwrites entries
        DesiredCapabilities iDc = ingredients.getDriverCapabilities();
        for (Entry<String, ?> cap : curCaps.asMap().entrySet()) {
            if (null == iDc.getCapability(cap.getKey())) {
                iDc.setCapability(cap.getKey(), cap.getValue());
            }
        }
    }
}
