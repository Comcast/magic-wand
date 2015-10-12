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
import io.appium.java_client.MobileElement;
import io.appium.java_client.NetworkConnectionSetting;
import io.appium.java_client.android.AndroidDriver;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AppiumAndroidPhoenixDriverTest {
    private AndroidDriver andriodDriverMock;
    private AppiumAndroidPhoenixDriver myTestObj;

    @BeforeMethod
    public void setupTestsVars() {
        this.andriodDriverMock = mock(AndroidDriver.class);
        this.myTestObj = new AppiumAndroidPhoenixDriver(andriodDriverMock);
    }

    @Test
    public void testScrollTo() {
        final MobileElement expected = mock(MobileElement.class);
        MobileElement actual;
        String input = null;

        when(this.andriodDriverMock.scrollTo(input)).thenReturn(expected);
        actual = this.myTestObj.scrollTo(input);
        Assert.assertEquals(expected, actual);

        input = "test";
        when(this.andriodDriverMock.scrollTo(input)).thenReturn(expected);
        actual = this.myTestObj.scrollTo(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testScrollToExact() {
        final MobileElement expected = mock(MobileElement.class);
        MobileElement actual;
        String input = null;

        when(this.andriodDriverMock.scrollToExact(input)).thenReturn(expected);
        actual = this.myTestObj.scrollToExact(null);
        Assert.assertEquals(expected, actual);

        input = "test";
        when(this.andriodDriverMock.scrollToExact(input)).thenReturn(expected);
        actual = this.myTestObj.scrollToExact(input);
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testSendKeyEvent() {
        int input = 5;

        this.myTestObj.sendKeyEvent(input);
        verify(this.andriodDriverMock, only()).sendKeyEvent(input);
    }

    @Test
    public void testSendKeyEvent2() {
        int input = 6;
        Integer metastate = 10;

        this.myTestObj.sendKeyEvent(input, metastate);
        verify(this.andriodDriverMock, only()).sendKeyEvent(input, metastate);
    }

    @Test
    public void testGetNetworkConnection() {
        final NetworkConnectionSetting expected = mock(NetworkConnectionSetting.class);
        NetworkConnectionSetting actual;

        when(this.andriodDriverMock.getNetworkConnection()).thenReturn(expected);
        actual = this.myTestObj.getNetworkConnection();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetNetworkConnection() {
        NetworkConnectionSetting input = mock(NetworkConnectionSetting.class);

        this.myTestObj.setNetworkConnection(input);
        verify(this.andriodDriverMock, only()).setNetworkConnection(input);
    }

    @Test
    public void testPushFile() {
        String remotePath = "/path";
        byte[] base64Data = {0x10, 0x20};

        this.myTestObj.pushFile(remotePath, base64Data);
        verify(this.andriodDriverMock, only()).pushFile(remotePath, base64Data);
    }

    @Test
    public void testStartActivity() {
        String appPackage = "package";
        String appActivity = "activity";
        String appWaitPackage = "wPackage";
        String appWaitActivity = "wActivity";

        this.myTestObj.startActivity(appPackage, appActivity, appWaitPackage, appWaitActivity);
        verify(this.andriodDriverMock, only()).startActivity(appPackage, appActivity, appWaitPackage, appWaitActivity);
    }

    @Test
    public void testStartActivity2() {
        String appPackage = "package";
        String appActivity = "activity";

        this.myTestObj.startActivity(appPackage, appActivity);
        verify(this.andriodDriverMock, only()).startActivity(appPackage, appActivity);
    }

    @Test
    public void testEndTestCoverage() {
        String intent = "intent";
        String path = "path";

        this.myTestObj.endTestCoverage(intent, path);
        verify(this.andriodDriverMock, only()).endTestCoverage(intent, path);
    }

    @Test
    public void testCurrentActivity() {
        final String expected = "test";
        String actual;

        when(this.andriodDriverMock.currentActivity()).thenReturn(expected);
        actual = this.myTestObj.currentActivity();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOpenNotifications() {
        this.myTestObj.openNotifications();
        verify(this.andriodDriverMock, only()).openNotifications();
    }

    @Test
    public void testIsLocked() {
        final boolean expected = true;
        boolean actual;

        when(this.andriodDriverMock.isLocked()).thenReturn(expected);
        actual = this.myTestObj.isLocked();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToggleLocationServices() {
        this.myTestObj.toggleLocationServices();
        verify(this.andriodDriverMock, only()).toggleLocationServices();
    }

    @Test
    public void testIgnoreUnimportantViews() {
        final Boolean input = Boolean.TRUE;

        this.myTestObj.ignoreUnimportantViews(input);
        verify(this.andriodDriverMock, only()).ignoreUnimportantViews(input);
    }

    @Test
    public void testFindElementByAndroidUIAutomator() {
        final WebElement expected = mock(WebElement.class);
        WebElement actual;
        final String input = "test";

        when(this.andriodDriverMock.findElementByAndroidUIAutomator(input)).thenReturn(expected);
        actual = this.myTestObj.findElementByAndroidUIAutomator(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindElementsByAndroidUIAutomator() {
        List<WebElement> expected = new ArrayList<WebElement>();
        List<WebElement> actual;
        final WebElement element = mock(WebElement.class);
        final String input = "test";

        expected.add(element);

        when(this.andriodDriverMock.findElementsByAndroidUIAutomator(input)).thenReturn(expected);
        actual = this.myTestObj.findElementsByAndroidUIAutomator(input);

        Assert.assertEquals(expected, actual);
    }
}
