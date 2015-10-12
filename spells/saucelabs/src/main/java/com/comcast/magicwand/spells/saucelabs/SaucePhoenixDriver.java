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
package com.comcast.magicwand.spells.saucelabs;

import java.util.Map;

import com.comcast.cookie.CookieHandler;
import com.comcast.magicwand.drivers.AbstractPhoenixDriver;
import com.saucelabs.ci.sauceconnect.SauceTunnelManager;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A driver for Sauce Labs
 *
 * @author Dmitry Jerusalimsky
 *
 */
public class SaucePhoenixDriver extends AbstractPhoenixDriver {

    private static final Logger LOG = LoggerFactory.getLogger(SaucePhoenixDriver.class);

    private WebDriver driver;
    private CookieHandler cookieHandler;
    private String username;
    private String vpnOptions;
    private SauceTunnelManager vpnManager;

    /**
     * Creates an instance of the driver
     *
     * @param driver Underlying remote driver
     * @param cookieHandler cookie handler to use for clearing cookies
     * @param vpnManager Reference to {@link SauceTunnelManager} used to establish and terminate VPN Connections
     * @param customDriverConfig Reference to the custom driver configurations. This is used for some VPN configuration
     */
    public SaucePhoenixDriver(WebDriver driver, CookieHandler cookieHandler, SauceTunnelManager vpnManager, Map<String, Object> customDriverConfig) {
        this.driver = driver;
        this.cookieHandler = cookieHandler;
        this.vpnManager = vpnManager;
        getVPNArgs(customDriverConfig);
    }

    private void getVPNArgs(Map<String, Object> customDriverConfig) {
        this.username = (String) customDriverConfig.get(SauceProvider.USERNAME);
        this.vpnOptions = (String) customDriverConfig.get(SauceProvider.VPN_OPTIONS);
    }

    /**
     * {@inheritDoc}
     */
    public WebDriver getDriver() {
        return this.driver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CookieHandler getCookieHandler() {
        return this.cookieHandler;
    }

    /**
     * Closes a VPN tunnel if one exists
     */
    public void closeVPNConnection() {
        if (null != this.vpnManager) {
            LOG.debug("Disconnecting VPN tunnel");
            this.vpnManager.closeTunnelsForPlan(username, vpnOptions, null);
            this.vpnManager = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quit() {
        super.quit();
        closeVPNConnection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        super.close();
        closeVPNConnection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable {
        closeVPNConnection();
    }
}
