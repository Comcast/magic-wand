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
package com.comcast.magicwand.spells.web.iexplore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.spells.web.iexplore.IePlatformSpecifics;

import org.mockito.Mockito;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class IePhoenixDriverTest {

    private static final String IE_DRIVER_VERSION_KEY = "ieDriverVersion";
    private static final String IE_DRIVER_ARCH_KEY = "ieDriverArch";

    private class TestIePhoenixDriver extends IePhoenixDriver {
        boolean verifyIngredientsReturn = false;
        IePlatformSpecifics getIePlatformSpecificsReturn;
        InternetExplorerDriver createIeDriverReturn;

        public void setVerifyIngredientsReturn(boolean b) {
            this.verifyIngredientsReturn = b;
        }

        public void setGetIePlatformSpecificsReturn(IePlatformSpecifics i) {
            this.getIePlatformSpecificsReturn = i;
        }

        public void setCreateIeDriverReturn(InternetExplorerDriver i) {
            this.createIeDriverReturn = i;
        }

        @Override
        protected boolean verifyIngredients(PhoenixDriverIngredients i) {
            return this.verifyIngredientsReturn;
        }

        @Override
        protected InternetExplorerDriver createDriver(DesiredCapabilities dc) {
            return this.createIeDriverReturn;
        }
    }

    private class TestData {
        boolean expected;
        boolean verifyIngredientsReturn = false;
        IePlatformSpecifics iePlatformSpecifiecs;
        InternetExplorerDriver internetExplorerDriver;
        private int testIndex;

        public TestData(int testIndex) {
            this.testIndex = testIndex;
        }

        public int getTestIndex() {
            return this.testIndex;
        }

        public TestData setExpected(boolean i) {
            this.expected = i;
            return this;
        }

        public TestData setVerifyIngredientsReturn(boolean i) {
            this.verifyIngredientsReturn = i;
            return this;
        }

        public TestData setIePlatformSpecificsReturn(IePlatformSpecifics i) {
            this.iePlatformSpecifiecs = i;
            return this;
        }

        public TestData setCreateIeDriver(InternetExplorerDriver i) {
            this.internetExplorerDriver = i;
            return this;
        }

        public boolean isExpected() {
            return expected;
        }

        public boolean isVerifyIngredientsReturn() {
            return verifyIngredientsReturn;
        }

        public IePlatformSpecifics getIePlatformSpecifics() {
            return iePlatformSpecifiecs;
        }

        public InternetExplorerDriver getInternetExplorerDriver() {
            return internetExplorerDriver;
        }
    }

    TestIePhoenixDriver myTestObj;

    @BeforeMethod
    public void setupTestsVars() {
        this.myTestObj = new TestIePhoenixDriver();
    }

    @DataProvider(name="verifyTestData")
    public Iterator<Object[]> createData() {
        List<Object[]> rv = new ArrayList<Object[]>();
        int testIndex = 1;

        rv.add(new Object[] {new TestData(testIndex++).setExpected(false)
                .setVerifyIngredientsReturn(false)
                .setIePlatformSpecificsReturn(new IePlatformSpecifics(null, "linux"))
        } );

        rv.add(new Object[] {new TestData(testIndex++).setExpected(false)
                .setVerifyIngredientsReturn(false)
                .setIePlatformSpecificsReturn(new IePlatformSpecifics("bogus_version", null))
        } );

        rv.add(new Object[] {new TestData(testIndex++).setExpected(true)
                .setVerifyIngredientsReturn(true)
                .setIePlatformSpecificsReturn(IePlatformSpecifics.WIN32)
        } );

        rv.add(new Object[] {new TestData(testIndex++).setExpected(true)
                .setVerifyIngredientsReturn(true)
                .setIePlatformSpecificsReturn(IePlatformSpecifics.WIN64)
        } );

        return rv.iterator();
    }

    @Test(dataProvider="verifyTestData")
    public void testVerify(TestData data) {
        //Store value for ie override property and then set to true for testing
        String prev = System.getProperty(IePlatformSpecifics.MAGICWAND_WINDOW_OS_OVERRIDE_PROP, "false");
        System.setProperty(IePlatformSpecifics.MAGICWAND_WINDOW_OS_OVERRIDE_PROP, "true");

        boolean actual;
        InternetExplorerDriver iedMock = Mockito.mock(InternetExplorerDriver.class);

        if (5 == data.getTestIndex() || 6 == data.getTestIndex()) {
            data.getTestIndex();
        }

        IePlatformSpecifics ips = data.getIePlatformSpecifics();

        this.myTestObj.setVerifyIngredientsReturn(data.isVerifyIngredientsReturn());
        this.myTestObj.setGetIePlatformSpecificsReturn(ips);
        this.myTestObj.setCreateIeDriverReturn(iedMock);

        PhoenixDriverIngredients pdi = new PhoenixDriverIngredients();
        pdi.getDriverConfigs().put(IE_DRIVER_VERSION_KEY, ips.getVersion());
        pdi.getDriverConfigs().put(IE_DRIVER_ARCH_KEY, ips.getArch());

        actual = this.myTestObj.verify(pdi);

        //restore the previous value for the override property
        System.setProperty(IePlatformSpecifics.MAGICWAND_WINDOW_OS_OVERRIDE_PROP, prev);
        Assert.assertEquals(actual, data.isExpected());
        if (data.isExpected()) {
            Assert.assertEquals(this.myTestObj.getDriver(), iedMock);
        }
    }
}
