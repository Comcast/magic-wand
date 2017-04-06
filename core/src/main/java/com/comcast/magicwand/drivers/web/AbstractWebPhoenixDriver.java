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
package com.comcast.magicwand.drivers.web;

import com.comcast.cookie.CookieHandler;
import com.comcast.cookie.handlers.GeneralCookieHandler;
import com.comcast.magicwand.utils.SystemDetail;
import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.AbstractPhoenixDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Defines Web driver
 *
 * @author Trent Schmidt
 *
 */
public abstract class AbstractWebPhoenixDriver<T extends WebDriver> extends AbstractPhoenixDriver {

    /**
     * {@inheritDoc}
     */
    protected CookieHandler getCookieHandler() {
        return new GeneralCookieHandler();
    }

    /**
     * {@link SystemDetail#deviceIsRunningMac()}
     * @return True if system is mac; False otherwise
     */
    protected boolean isRunningMac() {
        return SystemDetail.deviceIsRunningMac();
    }

    /**
     * {@link SystemDetail#deviceIsLinux()}
     * @return True if system is Linux; False otherwise
     */
    protected boolean isRunningLinux() {
        return SystemDetail.deviceIsLinux();
    }


    /**
     * {@link SystemDetail#deviceIsRunningWindows()}
     * @return True if system is Windows; False otherwise
     */
    protected boolean isRunningWindows() {
        return SystemDetail.deviceIsRunningWindows();
    }

    /**
     * Set property into system
     *
     * {@link System#setProperty(String, String)}
     *
     * @param key Name of property to set
     * @param value Value of property
     */
    protected void systemSetProperty(String key, String value) {
        System.setProperty(key, value);
    }

    /**
     * Verify the running conditions of this driver have been met
     *
     * @param i Ingredients to get the desktop os type
     * @return true if the conditions are met, false otherwise
     */
    public abstract boolean verify(PhoenixDriverIngredients i);

    /**
     * Verify the running conditions of this driver have been met
     *
     * @param i Ingredients to get the desktop os type
     * @return true if the conditions are met, false otherwise
     */
    protected boolean verifyIngredients(PhoenixDriverIngredients i) {
        boolean rv = false;

        switch (i.getDesktopOS().getType()) {
            case MAC:
                rv = this.isRunningMac();
                break;
            case LINUX:
                rv = this.isRunningLinux();
                break;
            case WINDOWS:
                rv = this.isRunningWindows();
                break;
            default:
                break;
        }

        return rv;
    }

    /**
     * Creates an instance of a WebDriver
     * @param capabilities Desired capabilities used to create the driver
     * @return Instance of a WebDriver
     */
    protected abstract T createDriver(DesiredCapabilities capabilities);
}
