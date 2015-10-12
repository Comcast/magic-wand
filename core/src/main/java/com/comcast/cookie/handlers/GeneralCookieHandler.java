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

import com.comcast.cookie.CookieHandler;

import org.openqa.selenium.WebDriver;

/**
 * Specifies default cookie handling mechanism
 *
 * @author Trent Schmidt
 *
 */
public class GeneralCookieHandler implements CookieHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean clearAllCookies(WebDriver wd) {
        if (null != wd) {
            wd.manage().deleteAllCookies();
        }
        return true;
    }
}
