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
package com.comcast.magicwand.spells.web.chrome;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.spells.web.chrome.ChromePlatformSpecifics;

import org.mockito.Mockito;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ChromePhoenixDriverTest {

    private static final Logger LOG = LoggerFactory.getLogger(ChromePhoenixDriverTest.class);
    private class TestChromePhoenixDriver extends ChromePhoenixDriver {
        boolean verifyIngredientsExpected = false;
        ChromePlatformSpecifics cpsMock;
        ChromeDriver chromeDriverMock;

        public void setVerifyIngredients(boolean expected) {
            this.verifyIngredientsExpected = expected;
        }

        @Override
        protected boolean verifyIngredients(PhoenixDriverIngredients i) {
            return this.verifyIngredientsExpected;
        }

        public void setChromePlatformSpecifics(ChromePlatformSpecifics cps) {
            this.cpsMock = cps;
        }

        public ChromePlatformSpecifics createChromePlatformSpecifics(String chromeDriverVersion)
        {
            return this.cpsMock;
        }

        @Override
        protected ChromeDriver createDriver(DesiredCapabilities dc) {
            return this.chromeDriverMock;
        }

        public void setCreateChromeDriver(ChromeDriver chromeDriver) {
            this.chromeDriverMock = chromeDriver;
        }

        @Override
        protected void systemSetProperty(String key, String value) {
        }
    }

    private class TestData {
        boolean expected;
        boolean verifyIngredientsReturn;
        ChromePlatformSpecifics cpsReturn;
        int testNumber;

        public TestData(int testNum) {
            this.testNumber = testNum;
        }

        public TestData setExpectedReturn(boolean expected) {
            this.expected = expected;
            return this;
        }

        public TestData setVerifyIngredientsReturn(boolean rv) {
            this.verifyIngredientsReturn = rv;
            return this;
        }

        public TestData setCPSReturn(ChromePlatformSpecifics cpsReturn) {
            this.cpsReturn = cpsReturn;
            return this;
        }

        public boolean isExpected() {
            return expected;
        }

        public boolean isVerifyIngredientsReturn() {
            return verifyIngredientsReturn;
        }

        public ChromePlatformSpecifics getCpsReturn() {
            return cpsReturn;
        }

        public PhoenixDriverIngredients getPhoenixDriverIngredientsMock() {
            return new PhoenixDriverIngredients();
        }
    }

    TestChromePhoenixDriver myTestObj;

    @BeforeMethod
    public void setupTestsVars() {
        this.myTestObj = new TestChromePhoenixDriver();
    }

    @DataProvider(name="verifyTestData")
    public Iterator<Object[]> createData() {
        List<Object[]> rv = new ArrayList<Object[]>();
        int testCount = 0;
        final String LATEST_GOOD_VERSION = "2.19";
        final String OLDER_VERSION = "2.18";

        rv.add(new Object[] {new TestData(testCount++).setExpectedReturn(true)
                .setVerifyIngredientsReturn(true)
                .setCPSReturn(new ChromePlatformSpecifics(LATEST_GOOD_VERSION, "_mac32", ""))
        } );

        //test for multiversioning support
        rv.add(new Object[] {new TestData(testCount++).setExpectedReturn(true)
                .setVerifyIngredientsReturn(true)
                .setCPSReturn(new ChromePlatformSpecifics(OLDER_VERSION, "_linux32"))
        } );

        //try to target bogus_version/chromedriver_win32.zip, should fail
        rv.add(new Object[] { new TestData(testCount++).setExpectedReturn(false)
                .setVerifyIngredientsReturn(false)
                .setCPSReturn(new ChromePlatformSpecifics("bogus_version", "_win32", ".exe"))
        } );

        //try to target LATEST_VERSION/chromedriver_beos32.zip, should fail
        rv.add(new Object[] { new TestData(testCount++).setExpectedReturn(false)
                .setVerifyIngredientsReturn(false)
                .setCPSReturn(new ChromePlatformSpecifics(LATEST_GOOD_VERSION, "_beos32"))
        } );

        return rv.iterator();
    }

    @Test(dataProvider="verifyTestData")
    public void testVerify(TestData data) {
        boolean actual;
        ChromeDriver cdMock = Mockito.mock(ChromeDriver.class);

        this.myTestObj.setVerifyIngredients(data.isVerifyIngredientsReturn());
        this.myTestObj.setChromePlatformSpecifics(data.getCpsReturn());
        this.myTestObj.setCreateChromeDriver(cdMock);

        actual = this.myTestObj.verify(data.getPhoenixDriverIngredientsMock());

        {
            String failMsg = String.format("Test[%d] failed to return the proper response from verify", data.testNumber);
            Assert.assertEquals(actual, data.isExpected(), failMsg);
        }

        if (data.isExpected()) {
            String failMsg = String.format("Test[%d] failed to return the expected driver", data.testNumber);
            Assert.assertEquals(cdMock, this.myTestObj.getDriver(), failMsg);
        }
    }
}
