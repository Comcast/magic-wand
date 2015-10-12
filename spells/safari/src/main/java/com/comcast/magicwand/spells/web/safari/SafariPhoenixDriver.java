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
package com.comcast.magicwand.spells.web.safari;

import com.comcast.cookie.CookieHandler;
import com.comcast.cookie.handlers.ProcessCookieHandler;
import com.comcast.cookie.utils.IProcessBuilder;
import com.comcast.cookie.utils.SystemProcess;
import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.web.AbstractWebPhoenixDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

/**
 * Web Driver for controlling Safari browser
 *
 * @author Trent Schmidt
 *
 */
public class SafariPhoenixDriver extends AbstractWebPhoenixDriver<SafariDriver> {
    private IProcessBuilder[] cmds = {
            new SystemProcess().command("killall", "cookied"),
            new SystemProcess().command("rm", "-rf", "~/Library/Caches/com.apple.Safari/*"),
            new SystemProcess().command("rm", "-rf,", "~/Library/Safari/LocalStorage/*"),
            new SystemProcess().command("rm", "-rf", "~/Library/Cookies/*")
    };
    private SafariDriver webDriver;

    /**
     * {@inheritDoc}
     */
    @Override
    public CookieHandler getCookieHandler() {
        return new ProcessCookieHandler(cmds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDriver getDriver() {
        return this.webDriver;
    }

    /**
     * Creates an instance of a safari driver
     * @param dc Capabilities to use when creating a driver
     * @return driver
     */
    protected SafariDriver createDriver(final DesiredCapabilities dc) {
        SafariDriver rv = null;

        if (null == dc) {
            rv = new SafariDriver();
        } else {
            rv = new SafariDriver(dc);
        }

        return rv;
    }

    /**
     * {@inheritDoc}
     */
    public boolean verify(PhoenixDriverIngredients i) {
        boolean rv = this.verifyIngredients(i);

        if (rv) {
            this.webDriver = createDriver(i.getDriverCapabilities());
        }

        return rv;
    }
}
