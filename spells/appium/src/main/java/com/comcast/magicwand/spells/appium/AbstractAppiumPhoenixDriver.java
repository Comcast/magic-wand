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

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.DeviceActionShortcuts;
import io.appium.java_client.HasAppStrings;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.InteractsWithFiles;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.TouchShortcuts;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.remote.ExecuteMethod;
import org.openqa.selenium.remote.Response;

import com.comcast.cookie.CookieHandler;
import com.comcast.cookie.handlers.GeneralCookieHandler;
import com.comcast.magicwand.drivers.AbstractPhoenixDriver;
import com.google.gson.JsonObject;

/**
 * Common functionality of Appium Driver
 *
 * @author Dmitry Jerusalimsky
 *
 */
public abstract class AbstractAppiumPhoenixDriver extends AbstractPhoenixDriver {

    protected AppiumDriver driver;
    CookieHandler cookieHandler = new GeneralCookieHandler();

    /**
     * Creates an instance of {@link AbstractAppiumPhoenixDriver}
     *
     * @param driver Underlying {@link AppiumDriver}
     */
    public AbstractAppiumPhoenixDriver(AppiumDriver driver) {
        this.driver = driver;
    }

    /**
     * {@inheritDoc}
     */
    public WebDriver getDriver() {
        return this.driver;
    }

    /**
     * Executes a command
     *
     * @param driverCommand Driver command name to execute
     * @param parameters Parameters used to execute the command
     * @return Response of the execution
     */
    public Response execute(String driverCommand, Map<String, ?> parameters) {
        return this.driver.execute(driverCommand, parameters);
    }

    /**
     * Executes a method
     *
     * @return {@link ExecuteMethod}
     */
    public ExecuteMethod getExecuteMethod() {
        return this.driver.getExecuteMethod();
    }

    /**
     * @see InteractsWithApps#resetApp()
     */
    public void resetApp() {
        this.driver.resetApp();
    }

    /**
     * @see InteractsWithApps#isAppInstalled(String)
     *
     * @param bundleId Bundle ID of an app to check
     * @return True if application is installed or false otherwise
     */
    public boolean isAppInstalled(String bundleId) {
        return this.driver.isAppInstalled(bundleId);
    }

    /**
     * @see InteractsWithApps#installApp(String)
     * @param appPath Path to the application to install
     */
    public void installApp(String appPath) {
        this.driver.installApp(appPath);
    }

    /**
     * @see InteractsWithApps#removeApp(String)
     * @param bundleId Bundle id of an app to remove
     */
    public void removeApp(String bundleId) {
        this.driver.removeApp(bundleId);
    }

    /**
     * @see InteractsWithApps#launchApp()
     */
    public void launchApp() {
        this.driver.launchApp();
    }

    /**
     * @see InteractsWithApps#closeApp()
     */
    public void closeApp() {
        this.driver.closeApp();
    }

    /**
     * @see InteractsWithApps#runAppInBackground(int)
     * @param seconds Number of seconds to run the app for
     */
    public void runAppInBackground(int seconds) {
        this.driver.runAppInBackground(seconds);
    }

    /**
     * @see DeviceActionShortcuts#hideKeyboard()
     */
    public void hideKeyboard() {
        this.driver.hideKeyboard();
    }

    /**
     * @see InteractsWithFiles#pullFile(String)
     * @param remotePath Path to a file to pull
     * @return Byte data representing a pulled file
     */
    public byte[] pullFile(String remotePath) {
        return this.driver.pullFile(remotePath);
    }

    /**
     * @see InteractsWithFiles#pullFolder(String)
     * @param remotePath Path to a folder to pull
     *
     * @return byte data representing a pulled folder
     */
    public byte[] pullFolder(String remotePath) {
        return this.driver.pullFolder(remotePath);
    }

    /**
     * @see PerformsTouchActions#performTouchAction(TouchAction)
     * @param touchAction Action to perform
     * @return Touch action
     */
    public TouchAction performTouchAction(TouchAction touchAction) {
        return this.driver.performTouchAction(touchAction);
    }

    /**
     * @see PerformsTouchActions#performMultiTouchAction(MultiTouchAction)
     * @param multiAction actions to perform
     */
    public void performMultiTouchAction(MultiTouchAction multiAction) {
        this.driver.performMultiTouchAction(multiAction);
    }

    /**
     * @see TouchShortcuts#tap(int, WebElement, int)
     * @param fingers Number of fingers used to perform a tap
     * @param element Element to tap
     * @param duration Duration of a tap
     */
    public void tap(int fingers, WebElement element, int duration) {
        this.driver.tap(fingers, element, duration);
    }

    /**
     * @see TouchShortcuts#tap(int, int, int, int)
     * @param fingers Number of fingers used to perform a tap
     * @param x X coordinate to tap
     * @param y Y coordinate to tap
     * @param duration Duration of a tap
     */
    public void tap(int fingers, int x, int y, int duration) {
        this.driver.tap(fingers, x, y, duration);
    }

    /**
     * @see TouchShortcuts#swipe(int, int, int, int, int)
     * @param startx Starting X position of a swipe
     * @param starty Starting Y position of a swipe
     * @param endx Ending X position of a swipe
     * @param endy Ending Y position of a swipe
     * @param duration Duration of a tap
     */
    public void swipe(int startx, int starty, int endx, int endy, int duration) {
        this.driver.swipe(startx, starty, endx, endy, duration);
    }

    /**
     * Convenience method for pinching an element on the screen. "pinching" refers to the action of two appendages
     * pressing the screen and sliding towards each other. NOTE: This convenience method places the initial touches
     * around the element, if this would happen to place one of them off the screen, appium with return an outOfBounds
     * error. In this case, revert to using the MultiTouchAction api instead of this method.
     *
     * @param el The element to pinch
     */
    public void pinch(WebElement el) {
        this.driver.pinch(el);
    }

    /**
     * Convenience method for pinching an element on the screen. "pinching" refers to the action of two appendages
     * pressing the screen and sliding towards each other. NOTE: This convenience method places the initial touches
     * around the element at a distance, if this would happen to place one of them off the screen, appium will return an
     * outOfBounds error. In this case, revert to using the MultiTouchAction api instead of this method.
     *
     * @param x x coordinate to terminate the pinch on
     * @param y y coordinate to terminate the pinch on
     */
    public void pinch(int x, int y) {
        this.driver.pinch(x, y);
    }

    /**
     * Convenience method for "zooming in" on an element on the screen. "zooming in" refers to the action of two
     * appendages pressing the screen and sliding away from each other. NOTE: This convenience method slides touches
     * away from the element, if this would happen to place one of them off the screen, appium will return an
     * outOfBounds error. In this case, revert to using the MultiTouchAction api instead of this method.
     *
     * @param el The element to pinch
     */
    public void zoom(WebElement el) {
        this.driver.zoom(el);
    }

    /**
     * Convenience method for "zooming in" on an element on the screen. "zooming in" refers to the action of two
     * appendages pressing the screen and sliding away from each other. NOTE: This convenience method slides touches
     * away from the element, if this would happen to place one of them off the screen, appium will return an
     * outOfBounds error. In this case, revert to using the MultiTouchAction api instead of this method.
     *
     * @param x x coordinate to start zoom on
     * @param y y coordinate to start zoom on
     */
    public void zoom(int x, int y) {
        this.driver.zoom(x, y);
    }

    /**
     * Get settings stored for this test session It's probably better to use a convenience function, rather than use
     * this function directly. Try finding the method for the specific setting you want to read
     *
     * @return JsonObject, a straight-up hash of settings
     */
    public JsonObject getSettings() {
        return this.driver.getSettings();
    }

    /**
     * Lock the device (bring it to the lock screen) for a given number of seconds
     *
     * @param seconds number of seconds to lock the screen for
     */
    public void lockScreen(int seconds) {
        this.driver.lockScreen(seconds);
    }

    /**
     * Switches contexts
     *
     * @param name Name of the context to switch to
     * @return Reference of a web driver with a new context
     */
    public WebDriver context(String name) {
        return this.driver.context(name);
    }

    /**
     * Gets names of available contexts
     *
     * @return Names of contexts
     */
    public Set<String> getContextHandles() {
        return this.driver.getContextHandles();
    }

    /**
     * Gets current driver context
     *
     * @return Name of the current context
     */
    public String getContext() {
        return this.driver.getContext();
    }

    /**
     * Rotates device
     *
     * @param orientation Expected final orientation
     */
    public void rotate(ScreenOrientation orientation) {
        this.driver.rotate(orientation);
    }

    /**
     * Gets device orientation
     *
     * @return Screen orientation
     */
    public ScreenOrientation getOrientation() {
        return this.driver.getOrientation();
    }

    /**
     * Searches for element by accessibility id
     *
     * @param using Accessibility ID value to search for
     * @return Element that matched search criteria
     */
    public WebElement findElementByAccessibilityId(String using) {
        return this.driver.findElementByAccessibilityId(using);
    }

    /**
     * Searches for elements by accessibility id
     *
     * @param using Accessibility ID value to search for
     * @return Elements that matched search criteria
     */
    public List<WebElement> findElementsByAccessibilityId(String using) {
        return this.driver.findElementsByAccessibilityId(using);
    }

    /**
     * Gets driver location
     *
     * @return Location
     */
    public Location location() {
        return this.driver.location();
    }

    /**
     * Sets driver location
     *
     * @param location Location to set
     */
    public void setLocation(Location location) {
        this.driver.setLocation(location);
    }

    /**
     * @see HasAppStrings#getAppStrings()
     * @return application strings
     */
    public String getAppStrings() {
        return this.driver.getAppStrings();
    }

    /**
     * @param language strings language code
     * @return a string of all the localized strings defined in the app
     *
     * @see HasAppStrings#getAppStrings(String)
     */
    public String getAppStrings(String language) {
        return this.driver.getAppStrings(language);
    }

    /**
     * Gets driver's remote URL
     *
     * @return URL of the appium server
     */
    public URL getRemoteAddress() {
        return this.driver.getRemoteAddress();
    }

    /**
     * {@inheritDoc}
     */
    protected CookieHandler getCookieHandler() {
        return this.cookieHandler;
    }
}
