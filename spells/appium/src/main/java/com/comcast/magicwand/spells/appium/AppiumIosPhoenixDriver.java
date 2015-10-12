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
package com.comcast.magicwand.spells.appium;

import java.util.List;

import org.openqa.selenium.WebElement;

import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.GetsNamedTextField;
import io.appium.java_client.ios.IOSDeviceActionShortcuts;
import io.appium.java_client.ios.IOSDriver;

/**
 * A wrapper around {@link IOSDriver}
 *
 * @author Dmitry Jerusalimsky
 *
 */
public class AppiumIosPhoenixDriver extends AbstractAppiumPhoenixDriver {

    private IOSDriver iosDriver;

    /**
     * Creates an instance of iOS driver
     *
     * @param driver Underlying instance of {@link IOSDriver}
     */
    public AppiumIosPhoenixDriver(IOSDriver driver) {
        super(driver);
        this.iosDriver = driver;
    }

    /**
     * Scroll to the element whose 'text' attribute contains the input text. This scrolling happens within the first
     * UIATableView on the UI. Use the method on IOSElement to scroll from a different ScrollView.
     *
     * @see IOSDriver#scrollTo(String)
     * @param text input text contained in text attribute
     * @return Mobile element
     */
    public MobileElement scrollTo(String text) {
        return this.iosDriver.scrollTo(text);
    }

    /**
     * Scroll to the element whose 'text' attribute is equal to the input text. This scrolling happens within the first
     * UIATableView on the UI. Use the method on IOSElement to scroll from a different ScrollView.
     *
     * @see IOSDriver#scrollToExact(String)
     * @param text input text to match
     * @return Mobile element
     */
    public MobileElement scrollToExact(String text) {
        return this.iosDriver.scrollToExact(text);
    }

    /**
     * @see IOSDeviceActionShortcuts#hideKeyboard(String, String)
     * @param strategy Strategy name
     * @param keyName Name of a key
     */
    public void hideKeyboard(String strategy, String keyName) {
        this.iosDriver.hideKeyboard(strategy, keyName);
    }

    /**
     * @see IOSDeviceActionShortcuts#hideKeyboard(String)
     * @param keyName Name of a key
     */
    public void hideKeyboard(String keyName) {
        this.iosDriver.hideKeyboard(keyName);
    }

    /**
     * @see IOSDeviceActionShortcuts#shake()
     */
    public void shake() {
        this.iosDriver.shake();
    }

    /**
     * @see GetsNamedTextField#getNamedTextField(String)
     * @param name Name to search for
     * @return Element that matched search criteria
     */
    public WebElement getNamedTextField(String name) {
        return this.iosDriver.getNamedTextField(name);
    }

    /**
     * @see IOSDriver#findElementByIosUIAutomation(String)
     * @param using Search string
     * @return Element matching search criteria
     */
    public WebElement findElementByIosUIAutomation(String using) {
        return this.iosDriver.findElementByIosUIAutomation(using);
    }

    /**
     * @see IOSDriver#findElementsByIosUIAutomation(String)
     * @param using Search string
     * @return List of elements matching search criteria
     */
    public List<WebElement> findElementsByIosUIAutomation(String using) {
        return this.iosDriver.findElementsByIosUIAutomation(using);
    }
}
