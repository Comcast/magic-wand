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
package com.comcast.magicwand.drivers.web;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.enums.DesktopOS;
import com.comcast.magicwand.enums.OSType;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AbstractWebPhoenixDriverTest {

    private class TestWebPhoenixDriver extends AbstractWebPhoenixDriver<WebDriver> {
        boolean isMac = false;
        boolean isLinux = false;
        boolean isWindows = false;

        @Override
        public WebDriver getDriver() {
            return null;
        }

        public boolean verify(PhoenixDriverIngredients i) {
            return super.verifyIngredients(i);
        }

        public void setRunningMac(boolean b) {
            this.isMac = b;
        }

        public void setRunningLinux(boolean b) {
            this.isLinux = b;
        }

        public void setRunningWindows(boolean b) {
            this.isWindows = b;
        }

        @Override
        protected boolean isRunningMac() {
            return this.isMac;
        }

        @Override
        protected boolean isRunningLinux() {
            return this.isLinux;
        }

        @Override
        protected boolean isRunningWindows() {
            return this.isWindows;
        }

        @Override
        protected WebDriver createDriver(DesiredCapabilities capabilities) {
            return null;
        }
    }

    TestWebPhoenixDriver myTestObj;
    PhoenixDriverIngredients pdiMock;
    DesktopOS d;


    @BeforeMethod
    public void setupTestsVars() {
        this.myTestObj = new TestWebPhoenixDriver();
        this.pdiMock = mock(PhoenixDriverIngredients.class);
        this.d = mock(DesktopOS.class);

        when(this.pdiMock.getDesktopOS()).thenReturn(this.d);
    }

    @Test
    public void testVerifyMac() {
        when(this.d.getType()).thenReturn(OSType.MAC);
        this.myTestObj.setRunningMac(true);

        Assert.assertTrue(this.myTestObj.verify(this.pdiMock));
    }

    @Test
    public void testVerifyLinux() {
        when(this.d.getType()).thenReturn(OSType.LINUX);
        this.myTestObj.setRunningLinux(true);

        Assert.assertTrue(this.myTestObj.verify(this.pdiMock));
    }

    @Test
    public void testVerifyWindows() {
        when(this.d.getType()).thenReturn(OSType.WINDOWS);
        this.myTestObj.setRunningWindows(true);

        Assert.assertTrue(this.myTestObj.verify(this.pdiMock));
    }

    @Test
    public void testVerifyFailure() {
        when(this.d.getType()).thenReturn(OSType.IPHONE);

        Assert.assertFalse(this.myTestObj.verify(this.pdiMock));
    }

    @Test
    public void testVerifyFailure2() {
        when(this.d.getType()).thenReturn(OSType.MAC);
        this.myTestObj.setRunningLinux(true);

        Assert.assertFalse(this.myTestObj.verify(this.pdiMock));
    }
}
