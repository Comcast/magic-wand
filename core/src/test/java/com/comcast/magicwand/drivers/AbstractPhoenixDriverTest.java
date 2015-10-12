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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.comcast.cookie.CookieHandler;

public class AbstractPhoenixDriverTest {

    private class MyPhoenixDriver extends AbstractPhoenixDriver {
        WebDriver webDriver;
        CookieHandler cookieHandler;
        IOException fileUtilsCopyFileException;

        public MyPhoenixDriver(WebDriver wd, CookieHandler ch) {
            this.webDriver = wd;
            this.cookieHandler = ch;
        }

        @Override
        protected CookieHandler getCookieHandler() {
            return this.cookieHandler;
        }

        public void setFileUtilsCopyFileException(IOException e) {
            this.fileUtilsCopyFileException = e;
        }

        @Override
        protected void fileUtilsCopyFile(File src, File dst) throws IOException {
            if (null != this.fileUtilsCopyFileException) {
                throw this.fileUtilsCopyFileException;
            }
        }

        @Override
        public WebDriver getDriver() {
            return this.webDriver;
        }
    }

    private RemoteWebDriver webDriverMock;
    private MyPhoenixDriver myTestObj;
    private CookieHandler cookieMock;

    @BeforeMethod
    public void setupTestsVars() {
        this.webDriverMock = mock(RemoteWebDriver.class);
        this.cookieMock = mock(CookieHandler.class);
        this.myTestObj = new MyPhoenixDriver(this.webDriverMock, this.cookieMock);
    }

    @Test
    public void testGet() {
        final String input = "test";
        this.myTestObj.get(input);

        verify(this.webDriverMock, only()).get(input);
    }

    @Test
    public void testGetNull() {
        final String input = "test";
        this.myTestObj.webDriver = null;

        this.myTestObj.get(input);
        verify(this.webDriverMock, never()).get(Mockito.anyString());
    }

    @Test
    public void testGetCurrentUrl() {
        final String expected = "test";
        String actual;

        when(this.webDriverMock.getCurrentUrl()).thenReturn(expected);
        actual = this.myTestObj.getCurrentUrl();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetCurrentUrlNull() {
        String actual;
        this.myTestObj.webDriver = null;

        actual = this.myTestObj.getCurrentUrl();
        Assert.assertNull(actual);
    }

    @Test
    public void testGetTitle() {
        final String expected = "title";
        String actual;

        when(this.webDriverMock.getTitle()).thenReturn(expected);
        actual = this.myTestObj.getTitle();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetTitleNull() {
        String actual;
        this.myTestObj.webDriver = null;

        actual = this.myTestObj.getTitle();
        Assert.assertNull(actual);
    }

    @Test
    public void testFindElements() {
        List<WebElement> expected = new ArrayList<WebElement>();
        List<WebElement> actual;
        final WebElement element = mock(WebElement.class);
        final By input = mock(By.class);

        expected.add(element);

        when(this.webDriverMock.findElements(input)).thenReturn(expected);
        actual = this.myTestObj.findElements(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindElementsNull() {
        final By input = mock(By.class);
        List<WebElement> actual;

        this.myTestObj.webDriver = null;
        actual = this.myTestObj.findElements(input);

        Assert.assertNull(actual);
    }

    @Test
    public void testFindElement() {
        final WebElement expected = mock(WebElement.class);
        WebElement actual;
        final By input = mock(By.class);
        when(this.webDriverMock.findElement(input)).thenReturn(expected);

        actual = this.myTestObj.findElement(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindElementNull() {
        WebElement actual;
        this.myTestObj.webDriver = null;
        final By input = mock(By.class);

        actual = this.myTestObj.findElement(input);

        Assert.assertNull(actual);
    }

    @Test
    public void testGetPageSource() {
        final String expected = "string";
        String actual;

        when(this.webDriverMock.getPageSource()).thenReturn(expected);
        actual = this.myTestObj.getPageSource();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPageSourceNull() {
        String actual;

        this.myTestObj.webDriver = null;
        actual = this.myTestObj.getPageSource();
        Assert.assertNull(actual);
    }

    @Test
    public void testClose() {
        this.myTestObj.close();
        verify(this.webDriverMock, only()).close();
    }

    @Test
    public void testCloseNull() {
        this.myTestObj.webDriver = null;
        this.myTestObj.close();
        verify(this.webDriverMock, never()).close();
    }

    @Test
    public void testQuit() {
        this.myTestObj.quit();
        verify(this.webDriverMock, only()).quit();
    }

    @Test
    public void testQuitNull() {
        this.myTestObj.webDriver = null;
        this.myTestObj.quit();
        verify(this.webDriverMock, never()).quit();
    }

    @Test
    public void testGetWindowHandles() {
        Set<String> expected = new HashSet<String>(1);
        Set<String> actual;
        final String element = "element";

        expected.add(element);

        when(this.webDriverMock.getWindowHandles()).thenReturn(expected);
        actual = this.myTestObj.getWindowHandles();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetWindowHandlesNull() {
        Set<String> actual;
        this.myTestObj.webDriver = null;

        actual = this.myTestObj.getWindowHandles();

        Assert.assertNull(actual);
    }

    @Test
    public void testGetWindowHandle() {
        final String expected = "test";
        String actual;

        when(this.webDriverMock.getWindowHandle()).thenReturn(expected);
        actual = this.myTestObj.getWindowHandle();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetWindowHandleNull() {
        String actual;
        this.myTestObj.webDriver = null;

        actual = this.myTestObj.getWindowHandle();

        Assert.assertNull(actual);
    }

    @Test
    public void testSwitchTo() {
        final TargetLocator expected = mock(TargetLocator.class);
        TargetLocator actual;

        when(this.webDriverMock.switchTo()).thenReturn(expected);
        actual = this.myTestObj.switchTo();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSwitchToNull() {
        TargetLocator actual;

        this.myTestObj.webDriver = null;
        actual = this.myTestObj.switchTo();

        Assert.assertNull(actual);
    }

    @Test
    public void testNavigate() {
        final Navigation expected = mock(Navigation.class);
        Navigation actual;

        when(this.webDriverMock.navigate()).thenReturn(expected);
        actual = this.myTestObj.navigate();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNavigateNull() {
        Navigation actual;
        this.myTestObj.webDriver = null;

        actual = this.myTestObj.navigate();

        Assert.assertNull(actual);
    }

    @Test
    public void testManage() {
        final Options expected = mock(Options.class);
        Options actual;

        when(this.webDriverMock.manage()).thenReturn(expected);
        actual = this.myTestObj.manage();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testManageNull() {
        Options actual;
        this.myTestObj.webDriver = null;

        actual = this.myTestObj.manage();

        Assert.assertNull(actual);
    }

    @Test
    public void testTakeScreenshotWebDriverException() {
        final boolean expected = false;
        boolean actual;
        final String input = "filepath";

        when(((TakesScreenshot)this.webDriverMock).getScreenshotAs((OutputType<File>) Mockito.any())).thenThrow(new WebDriverException());
        actual = this.myTestObj.takeScreenshot(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTakeScreenshotIOException() {
        final boolean expected = false;
        boolean actual;
        final String input = "filepath";
        IOException e = mock(IOException.class);

        this.myTestObj.setFileUtilsCopyFileException(e);
        actual = this.myTestObj.takeScreenshot(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTakeScreenshot() {
        final boolean expected = true;
        boolean actual;
        final String input = "filepath";

        actual = this.myTestObj.takeScreenshot(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTakeScreenshotNullWebDriver() {
        final boolean expected = false;
        boolean actual;
        final String input = "test";

        this.myTestObj.webDriver = null;
        actual = this.myTestObj.takeScreenshot(input);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetDriver() {
        WebDriver actual;

        actual = this.myTestObj.getDriver();

        Assert.assertEquals(this.webDriverMock, actual);
    }

    @Test
    public void testClearAllCookies() {
        this.myTestObj.clearAllCookies();
        verify(this.cookieMock, only()).clearAllCookies(this.webDriverMock);
    }
}
