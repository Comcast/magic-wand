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
import io.appium.java_client.NetworkConnectionSetting;
import io.appium.java_client.android.AndroidDeviceActionShortcuts;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.android.AndroidKeyMetastate;
import io.appium.java_client.android.HasNetworkConnection;
import io.appium.java_client.android.PushesFiles;
import io.appium.java_client.android.StartsActivity;

/**
 * A passthrough class for {@link AndroidDriver}
 *
 * @author Dmitry Jerusalimsky
 *
 */
public class AppiumAndroidPhoenixDriver extends AbstractAppiumPhoenixDriver {

    private AndroidDriver androidDriver;

    /**
     * Creates an instance of the driver
     * @param driver Underlying {@link AndroidDriver}
     */
    public AppiumAndroidPhoenixDriver(AndroidDriver driver) {
        super(driver);
        this.androidDriver = driver;
    }

    /**
     * Scroll forward to the element which has a description or name which contains the input text. The scrolling is
     * performed on the first scrollView present on the UI
     *
     * @param text Text to search for
     * @return Mobile element which matches searched text
     */
    public MobileElement scrollTo(String text) {
        return this.androidDriver.scrollTo(text);
    }

    /**
     * Scroll forward to the element which has a description or name which exactly matches the input text. The scrolling
     * is performed on the first scrollView present on the UI
     *
     * @param text Text to search for
     * @return Mobile element which matches searched text
     */
    public MobileElement scrollToExact(String text) {
        return this.androidDriver.scrollToExact(text);
    }

    /**
     * Send a key event to the device
     *
     * @param key code for the key pressed on the device
     */
    public void sendKeyEvent(int key) {
        this.androidDriver.sendKeyEvent(key);
    }

    /**
     * @param key code for the key pressed on the Android device
     * @param metastate metastate for the keypress
     *
     * @see AndroidKeyCode
     * @see AndroidKeyMetastate
     * @see AndroidDeviceActionShortcuts#sendKeyEvent(int, Integer)
     */
    public void sendKeyEvent(int key, Integer metastate) {
        this.androidDriver.sendKeyEvent(key, metastate);
    }

    /**
     * @see HasNetworkConnection#getNetworkConnection()
     * @return Network connection settings
     */
    public NetworkConnectionSetting getNetworkConnection() {
        return this.androidDriver.getNetworkConnection();
    }

    /**
     * @param connection The NetworkConnectionSetting configuration to use for the device
     *
     * @see HasNetworkConnection#setNetworkConnection(NetworkConnectionSetting)
     */
    public void setNetworkConnection(NetworkConnectionSetting connection) {
        this.androidDriver.setNetworkConnection(connection);
    }

    /**
     * @param remotePath Path to file to write data to on remote device
     * @param base64Data Base64 encoded byte array of data to write to remote device
     * @see PushesFiles#pushFile(String, byte[])
     */
    public void pushFile(String remotePath, byte[] base64Data) {
        this.androidDriver.pushFile(remotePath, base64Data);
    }

    /**
     * Starts an activity
     * {@code Ex: driver.startActivity("com.foo.bar", ".MyActivity", null, null); }
     *
     * @param appPackage The package containing the activity. [Required]
     * @param appActivity The activity to start. [Required]
     * @param appWaitPackage Automation will begin after this package starts. [Optional]
     * @param appWaitActivity Automation will begin after this activity starts. [Optional]
     *
     * @see StartsActivity#startActivity(String, String, String, String)
     */
    public void startActivity(String appPackage, String appActivity, String appWaitPackage, String appWaitActivity) {
        this.androidDriver.startActivity(appPackage, appActivity, appWaitPackage, appWaitActivity);
    }

    /**
     * Starts an activity
     *
     * {@code Ex: *.startActivity("com.foo.bar", ".MyActivity");}
     * @param appPackage The package containing the activity. [Required]
     * @param appActivity The activity to start. [Required]
     *
     * @see StartsActivity#startActivity(String, String)
     */
    public void startActivity(String appPackage, String appActivity) {
        this.androidDriver.startActivity(appPackage, appActivity);
    }

    /**
     * Get test-coverage data
     *
     * @param intent intent to broadcast
     * @param path path to .ec file
     */
    public void endTestCoverage(String intent, String path) {
        this.androidDriver.endTestCoverage(intent, path);
    }

    /**
     * Get the current activity being run on the mobile device
     * @return name of a current activity
     */
    public String currentActivity() {
        return this.androidDriver.currentActivity();
    }

    /**
     * Open the notification shade, on Android devices.
     */
    public void openNotifications() {
        this.androidDriver.openNotifications();
    }

    /**
     * Check if the device is locked.
     *
     * @return true if device is locked. False otherwise
     */
    public boolean isLocked() {
        return this.androidDriver.isLocked();
    }

    /**
     * Toggles location services
     */
    public void toggleLocationServices() {
        this.androidDriver.toggleLocationServices();
    }

    /**
     * Set the `ignoreUnimportantViews` setting. *Android-only method*
     *
     * Sets whether Android devices should use `setCompressedLayoutHeirarchy()` which ignores all views which are marked
     * IMPORTANT_FOR_ACCESSIBILITY_NO or IMPORTANT_FOR_ACCESSIBILITY_AUTO (and have been deemed not important by the
     * system), in an attempt to make things less confusing or faster.
     *
     * @param compress ignores unimportant views if true, doesn't ignore otherwise.
     */
    public void ignoreUnimportantViews(Boolean compress) {
        this.androidDriver.ignoreUnimportantViews(compress);
    }

    /**
     * Searches for element using Android's UI Automator
     * @param using Element identifier
     * @return Matching element
     */
    public WebElement findElementByAndroidUIAutomator(String using) {
        return this.androidDriver.findElementByAndroidUIAutomator(using);
    }

    /**
     * Searches for elements using Android's UI Automator
     * @param using Element identifier
     * @return List of found elements
     */
    public List<WebElement> findElementsByAndroidUIAutomator(String using) {
        return this.androidDriver.findElementsByAndroidUIAutomator(using);
    }
}
