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
package com.comcast.cookie.handlers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.cookie.CookieHandler;

public class GeneralCookieHandlerTest {

    @Test(groups={"cookie"})
    public void testCookieMockDriver() {
        CookieHandler ch = new GeneralCookieHandler();

        WebDriver wd = mock(WebDriver.class);
        Options o = mock(Options.class);

        when(wd.manage()).thenReturn(o);
        Assert.assertTrue(ch.clearAllCookies(wd));

        verify(wd).manage();
        verify(o).deleteAllCookies();
    }

    @Test(groups={"cookie"})
    public void testCookieNullDriver() {
        CookieHandler ch = new GeneralCookieHandler();

        Assert.assertTrue(ch.clearAllCookies(null));

    }
}
