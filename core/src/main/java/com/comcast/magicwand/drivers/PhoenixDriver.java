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
package com.comcast.magicwand.drivers;

import org.openqa.selenium.WebDriver;

/**
 * Interface for defying a driver
 *
 * @author Dmitry Jerusalimsky
 *
 */
public interface PhoenixDriver extends WebDriver {

    /**
     * Gets the underlying {@link WebDriver} object
     *
     * @return Returns underlying web driver
     */
    WebDriver getDriver();

    /**
     * Takes screenshot of the device and saves it in a specified location
     *
     * @param filePath Absolute path to the file where screenshot is going to be saved
     * @return True if file was created; False otherwise
     */
    boolean takeScreenshot(String filePath);

    /**
     * Clear cookies from this WebDriver
     */
    void clearAllCookies();
}
