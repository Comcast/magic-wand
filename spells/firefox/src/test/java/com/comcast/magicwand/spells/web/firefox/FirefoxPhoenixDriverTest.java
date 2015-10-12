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
package com.comcast.magicwand.spells.web.firefox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.comcast.magicwand.builders.PhoenixDriverIngredients;

import org.mockito.Mockito;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FirefoxPhoenixDriverTest {
    private class TestFirefoxPhoenixDriver extends FirefoxPhoenixDriver {
        boolean verifyIngredientsReturn = false;
        ProfilesIni createProfilesIniReturn;
        FirefoxDriver createFirefoxDriverReturn;

        public void setVerifyIngredientsReturn(boolean rv) {
            this.verifyIngredientsReturn = rv;
        }

        @Override
        protected boolean verifyIngredients(PhoenixDriverIngredients i) {
            return this.verifyIngredientsReturn;
        }

        public void setCreateProfilesIni(ProfilesIni profiles) {
            this.createProfilesIniReturn = profiles;
        }

        @Override
        protected ProfilesIni createProfilesIni() {
            return this.createProfilesIniReturn;
        }

        public void setCreateFirefoxDriver(FirefoxDriver driver) {
            this.createFirefoxDriverReturn = driver;
        }

        @Override
        protected FirefoxDriver createDriver(DesiredCapabilities dc) {
            return this.createFirefoxDriverReturn;
        }
    }

    private class TestData {
        boolean expected;
        boolean verifyIngredientsReturn = false;
        ProfilesIni createProfilesIniReturn;

        public boolean isVerifyIngredientsReturn() {
            return verifyIngredientsReturn;
        }

        public TestData setVerifyIngredientsReturn(boolean verifyIngredientsReturn) {
            this.verifyIngredientsReturn = verifyIngredientsReturn;
            return this;
        }

        public ProfilesIni getCreateProfilesIniReturn() {
            return createProfilesIniReturn;
        }

        public TestData setCreateProfilesIniReturn(ProfilesIni createProfilesIniReturn) {
            this.createProfilesIniReturn = createProfilesIniReturn;
            return this;
        }

        public boolean isExpected() {
            return this.expected;
        }

        public TestData setExpected(boolean expected) {
            this.expected = expected;
            return this;
        }
    }

    TestFirefoxPhoenixDriver myTestObj;

    @BeforeMethod
    public void setupTestsVars() {
        this.myTestObj = new TestFirefoxPhoenixDriver();
    }

    @DataProvider(name="verifyTestData")
    public Iterator<Object[]> createData() {
        List<Object[]> rv = new ArrayList<Object[]>();

        rv.add(new Object[] {new TestData().setExpected(false)
                .setVerifyIngredientsReturn(false)
        } );
        rv.add(new Object[] {new TestData().setExpected(true)
                .setVerifyIngredientsReturn(true)
                .setCreateProfilesIniReturn(Mockito.mock(ProfilesIni.class))
        } );

        return rv.iterator();
    }

    @Test(dataProvider="verifyTestData")
    public void testVerify(TestData data) {
        boolean actual;
        FirefoxDriver fdMock = Mockito.mock(FirefoxDriver.class);

        this.myTestObj.setVerifyIngredientsReturn(data.isVerifyIngredientsReturn());
        this.myTestObj.setCreateProfilesIni(data.getCreateProfilesIniReturn());
        this.myTestObj.setCreateFirefoxDriver(fdMock);

        actual = this.myTestObj.verify(new PhoenixDriverIngredients());
        Assert.assertEquals(actual, data.isExpected());
        if (data.isExpected()) {
            Assert.assertEquals(fdMock, this.myTestObj.getDriver());
        }
    }
}
