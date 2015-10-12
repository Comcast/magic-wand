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
package com.comcast.magicwand.spells.web.firefox;

import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.web.AbstractWebPhoenixDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Web Driver for controlling Firefox browser
 *
 * @author Trent Schmidt
 *
 */
public class FirefoxPhoenixDriver extends AbstractWebPhoenixDriver<FirefoxDriver> {
    private FirefoxDriver webDriver;

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDriver getDriver() {
        return this.webDriver;
    }

    /**
     * Creates a firefox driver with a specified profile
     * @param dc Desired Capabilities to use when creating a driver
     * @return Instance of a driver
     */
    protected FirefoxDriver createDriver(final DesiredCapabilities dc) {
        // no need to check if dc is null as we at least have the firefox profile set
        return new FirefoxDriver(dc);
    }

    /**
     * Instantiates {@link ProfilesIni}
     * @return {@link ProfilesIni}
     */
    protected ProfilesIni createProfilesIni() {
        return new ProfilesIni();
    }

    /**
     * {@inheritDoc}
     */
    public boolean verify(PhoenixDriverIngredients i) {
        boolean rv = false;

        if (this.verifyIngredients(i)) {
            rv = true;
            ProfilesIni allProfiles = this.createProfilesIni();
            FirefoxProfile fp = allProfiles.getProfile("default");

            DesiredCapabilities dc = i.getDriverCapabilities();

            if (null == dc.getCapability(FirefoxDriver.PROFILE)) {
                dc.setCapability(FirefoxDriver.PROFILE, fp);
            }

            this.webDriver = createDriver(dc);
        }

        return rv;
    }
}
