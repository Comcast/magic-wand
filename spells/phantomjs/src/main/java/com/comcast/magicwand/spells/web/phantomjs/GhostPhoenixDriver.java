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
package com.comcast.magicwand.spells.web.phantomjs;

import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.web.AbstractWebPhoenixDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Web Driver for controlling PhantomJS browser
 *
 * @author Nikita Shah
 *
 */

public class GhostPhoenixDriver extends AbstractWebPhoenixDriver<PhantomJSDriver> {

    private PhantomJSDriver webDriver;

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDriver getDriver() {
        return this.webDriver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verify(PhoenixDriverIngredients ingredients) {
        boolean rv = false;

        if (this.verifyIngredients(ingredients)) {
            rv = true;
            this.webDriver = createDriver(ingredients.getDriverCapabilities());
        }

        return rv;
    }

    /**
     * Creates an instance of a PhantomJS driver
     * @param driverCapabilities Desired capabilities to use when creating the driver
     * @return Instance of a {@link PhantomJSDriver}
     */
    protected PhantomJSDriver createDriver(final DesiredCapabilities driverCapabilities) {
        PhantomJSDriver ghostDriver = null;
        if (null == driverCapabilities) {
            ghostDriver = new PhantomJSDriver();
        }
        else {
            ghostDriver = new PhantomJSDriver(driverCapabilities);
        }
        return ghostDriver;
    }
}
