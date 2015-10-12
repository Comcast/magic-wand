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
import io.appium.java_client.ios.IOSDriver;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AppiumIosPhoenixDriverTest {
    private IOSDriver iosDriverMock;
    private AppiumIosPhoenixDriver myTestObj;

    @BeforeMethod
    public void setupTestsVars() {
        this.iosDriverMock = mock(IOSDriver.class);
        this.myTestObj = new AppiumIosPhoenixDriver(this.iosDriverMock);
    }

    @Test
    public void testScrollTo() {
        final MobileElement expected = mock(MobileElement.class);
        MobileElement actual;
        final String input = "test";

        when(this.iosDriverMock.scrollTo(input)).thenReturn(expected);
        actual = this.myTestObj.scrollTo(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testScrollToExact() {
        final MobileElement expected = mock(MobileElement.class);
        MobileElement actual;
        final String input = "test";

        when(this.iosDriverMock.scrollToExact(input)).thenReturn(expected);
        actual = this.myTestObj.scrollToExact(input);
    }

    @Test
    public void testHideKeyboard() {
        final String strategy = "strategy";
        final String keyName = "KeyName";

        this.myTestObj.hideKeyboard(strategy, keyName);
        verify(this.iosDriverMock, only()).hideKeyboard(strategy, keyName);
    }

    @Test
    public void testHideKeyboard2() {
        final String keyName = "keyName";

        this.myTestObj.hideKeyboard(keyName);
        verify(this.iosDriverMock, only()).hideKeyboard(keyName);
    }

    @Test
    public void testShake() {
        this.myTestObj.shake();
        verify(this.iosDriverMock, only()).shake();
    }

    @Test
    public void testGetNamedTextField() {
        final WebElement expected = mock(WebElement.class);
        WebElement actual;
        final String input = "name";

        when(this.iosDriverMock.getNamedTextField(input)).thenReturn(expected);
        actual = this.myTestObj.getNamedTextField(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindElementByIosUIAutomation() {
        final WebElement expected = mock(WebElement.class);
        WebElement actual;
        final String input = "name";

        when(this.iosDriverMock.findElementByIosUIAutomation(input)).thenReturn(expected);
        actual = this.myTestObj.findElementByIosUIAutomation(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindElementsByIosUIAutomation() {
        List<WebElement> expected = new ArrayList<WebElement>();
        List<WebElement> actual;
        final String input = "name";
        final WebElement element = mock(WebElement.class);

        expected.add(element);

        when(this.iosDriverMock.findElementsByIosUIAutomation(input)).thenReturn(expected);
        actual = this.myTestObj.findElementsByIosUIAutomation(input);

        Assert.assertEquals(expected, actual);
    }
}
