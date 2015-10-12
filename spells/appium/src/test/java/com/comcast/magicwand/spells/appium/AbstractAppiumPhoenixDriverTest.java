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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.remote.ExecuteMethod;
import org.openqa.selenium.remote.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;

public class AbstractAppiumPhoenixDriverTest {

    private class MyAppiumPhoenixDriver extends AbstractAppiumPhoenixDriver {
        public MyAppiumPhoenixDriver(AppiumDriver ad) {
            super(ad);
        }
    }

    private AppiumDriver appiumDriverMock;
    private MyAppiumPhoenixDriver myTestObj;

    @BeforeMethod
    public void setupTestsVars() {
        this.appiumDriverMock = mock(AppiumDriver.class);
        this.myTestObj = new MyAppiumPhoenixDriver(this.appiumDriverMock);
    }

    @Test
    public void testExecute() {
        final Response expected = mock(Response.class);
        Response actual;

        final String command = "command";
        Map<String, String> map = new HashMap<String, String>(1);

        map.put("key", "value");

        when(this.appiumDriverMock.execute(command, map)).thenReturn(expected);

        actual = this.myTestObj.execute(command, map);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetExecuteMethod() {
        final ExecuteMethod expected = mock(ExecuteMethod.class);
        ExecuteMethod actual;

        when(this.appiumDriverMock.getExecuteMethod()).thenReturn(expected);

        actual = this.myTestObj.getExecuteMethod();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResetApp() {
        this.myTestObj.resetApp();

        verify(this.appiumDriverMock, only()).resetApp();
    }

    @Test
    public void testIsAppInstalled() {
        final boolean expected = true;
        boolean actual;
        final String s = "input";

        when(this.appiumDriverMock.isAppInstalled(s)).thenReturn(expected);
        actual = this.myTestObj.isAppInstalled(s);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInstallApp() {
        final String s = "input";
        this.myTestObj.installApp(s);

        verify(this.appiumDriverMock, only()).installApp(s);
    }

    @Test
    public void testRemoveApp() {
        final String s = "input";
        this.myTestObj.removeApp(s);

        verify(this.appiumDriverMock, only()).removeApp(s);
    }

    @Test
    public void testLaunchApp() {
        this.myTestObj.launchApp();

        verify(this.appiumDriverMock, only()).launchApp();
    }

    @Test
    public void testCloseApp() {
        this.myTestObj.closeApp();

        verify(this.appiumDriverMock, only()).closeApp();
    }

    @Test
    public void testRunAppInBackground() {
        final int seconds = 5;

        this.myTestObj.runAppInBackground(seconds);
        verify(this.appiumDriverMock, only()).runAppInBackground(seconds);
    }

    @Test
    public void testHideKeyboard() {
        this.myTestObj.hideKeyboard();

        verify(this.appiumDriverMock, only()).hideKeyboard();
    }

    @Test
    public void testPullFile() {
        final String s = "remotePath";
        final byte[] expected = {0x00, 0x04};
        byte[] actual;

        when(this.appiumDriverMock.pullFile(s)).thenReturn(expected);
        actual = this.myTestObj.pullFile(s);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPullFolder() {
        final String s = "remotePath";
        final byte[] expected = {0x10, 0x40};
        byte[] actual;

        when(this.appiumDriverMock.pullFolder(s)).thenReturn(expected);
        actual = this.myTestObj.pullFolder(s);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPerformTouchAction() {
        final TouchAction expected = mock(TouchAction.class);
        TouchAction actual;
        final TouchAction input = mock(TouchAction.class);

        when(this.appiumDriverMock.performTouchAction(input)).thenReturn(expected);
        actual = this.myTestObj.performTouchAction(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPerformMultiTouchAction() {
        final MultiTouchAction input = mock(MultiTouchAction.class);

        this.myTestObj.performMultiTouchAction(input);
        verify(this.appiumDriverMock, only()).performMultiTouchAction(input);
    }

    @Test
    public void testTap() {
        final int fingers = 5;
        final WebElement element = mock(WebElement.class);
        final int duration = 100;

        this.myTestObj.tap(fingers, element, duration);
        verify(this.appiumDriverMock, only()).tap(fingers, element, duration);
    }

    @Test
    public void testTap2() {
        final int fingers = 3;
        final int x = 100;
        final int y = 150;
        final int duration = 100;

        this.myTestObj.tap(fingers, x, y, duration);
        verify(this.appiumDriverMock, only()).tap(fingers, x, y, duration);
    }

    @Test
    public void testSwipe() {
        final int startx = 10;
        final int starty = 20;
        final int endx = 40;
        final int endy = 50;
        final int duration = 100;

        this.myTestObj.swipe(startx, starty, endx, endy, duration);
        verify(this.appiumDriverMock, only()).swipe(startx, starty, endx, endy, duration);
    }

    @Test
    public void testPinch() {
        final WebElement input = mock(WebElement.class);

        this.myTestObj.pinch(input);
        verify(this.appiumDriverMock, only()).pinch(input);
    }

    @Test
    public void testPinch2() {
        final int x = 10;
        final int y = 20;

        this.myTestObj.pinch(x, y);
        verify(this.appiumDriverMock, only()).pinch(x, y);
    }

    @Test
    public void testZoom() {
        final WebElement input = mock(WebElement.class);

        this.myTestObj.zoom(input);
        verify(this.appiumDriverMock, only()).zoom(input);
    }

    @Test
    public void testZoom2() {
        final int x = 20;
        final int y = 10;

        this.myTestObj.zoom(x, y);
        verify(this.appiumDriverMock, only()).zoom(x, y);
    }

    @Test
    public void testGetSettings() {
        final JsonObject expected = new JsonObject();
        JsonObject actual;

        when(this.appiumDriverMock.getSettings()).thenReturn(expected);
        actual = this.myTestObj.getSettings();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLockScreen() {
        final int seconds = 5;

        this.myTestObj.lockScreen(seconds);
        verify(this.appiumDriverMock, only()).lockScreen(seconds);
    }

    @Test
    public void testContext() {
        final WebDriver expected = mock(WebDriver.class);
        WebDriver actual;
        final String name = "name";

        when(this.appiumDriverMock.context(name)).thenReturn(expected);
        actual = this.myTestObj.context(name);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetContextHandles() {
        Set<String> expected = new HashSet<String>();
        Set<String> actual;
        final String s = "string";

        expected.add(s);

        when(this.appiumDriverMock.getContextHandles()).thenReturn(expected);
        actual = this.myTestObj.getContextHandles();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetContext() {
        final String expected = "context";
        String actual;

        when(this.appiumDriverMock.getContext()).thenReturn(expected);
        actual = this.myTestObj.getContext();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRotate() {
        final ScreenOrientation input = ScreenOrientation.LANDSCAPE;

        this.myTestObj.rotate(input);
        verify(this.appiumDriverMock, only()).rotate(input);
    }

    @Test
    public void testGetOrientation() {
        final ScreenOrientation expected = ScreenOrientation.PORTRAIT;
        ScreenOrientation actual;

        when(this.appiumDriverMock.getOrientation()).thenReturn(expected);
        actual = this.myTestObj.getOrientation();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindElementByAccessibilityId() {
        final WebElement expected = mock(WebElement.class);
        WebElement actual;
        final String input = "input";

        when(this.appiumDriverMock.findElementByAccessibilityId(input)).thenReturn(expected);
        actual = this.myTestObj.findElementByAccessibilityId(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindElementsByAccessibilityId() {
        List<WebElement> expected = new ArrayList<WebElement>();
        List<WebElement> actual;
        final WebElement element = mock(WebElement.class);
        final String input = "using";

        expected.add(element);

        when(this.appiumDriverMock.findElementsByAccessibilityId(input)).thenReturn(expected);
        actual = this.myTestObj.findElementsByAccessibilityId(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLocation() {
        final Location expected = mock(Location.class);
        Location actual;

        when(this.appiumDriverMock.location()).thenReturn(expected);
        actual = this.myTestObj.location();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetLocation() {
        final Location expected = mock(Location.class);

        this.myTestObj.setLocation(expected);
        verify(this.appiumDriverMock, only()).setLocation(expected);
    }

    @Test
    public void testGetAppStrings() {
        final String expected = "expected";
        String actual;

        when(this.appiumDriverMock.getAppStrings()).thenReturn(expected);
        actual = this.myTestObj.getAppStrings();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetAppStrings2() {
        final String expected = "expected";
        String actual;
        final String input = "language";

        when(this.appiumDriverMock.getAppStrings(input)).thenReturn(expected);
        actual = this.myTestObj.getAppStrings(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetRemoteAddress() throws MalformedURLException {
        final URL expected = new URL("http://test.com/blue");
        URL actual;

        when(this.appiumDriverMock.getRemoteAddress()).thenReturn(expected);
        actual = this.myTestObj.getRemoteAddress();

        Assert.assertEquals(expected, actual);
    }
}
