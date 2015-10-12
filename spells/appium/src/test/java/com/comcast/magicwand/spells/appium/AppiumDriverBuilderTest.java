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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mockito.Mockito;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.spells.appium.dawg.meta.MetaAppiumDevice.FamilyType;
import com.comcast.magicwand.spells.appium.dawg.utils.AppiumServerController;
import com.comcast.magicwand.enums.MobileOS;
import com.comcast.magicwand.enums.OSType;
import com.comcast.magicwand.exceptions.FlyingPhoenixException;

public class AppiumDriverBuilderTest {

    private class MyADB extends AppiumDriverBuilder {
        public MyADB(PhoenixDriverIngredients ingredients, DesiredCapabilities capabilities) {
            super(ingredients, capabilities);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected AndroidDriver createAndroidDriver(URL url, DesiredCapabilities caps) {
            return Mockito.mock(AndroidDriver.class);
        }

        @Override
        protected IOSDriver createIosDriver(URL url, DesiredCapabilities caps) {
            return Mockito.mock(IOSDriver.class);
        }
    }

    private class ADBTData {
        private String host;
        private Integer port;
        private String deviceID;
        private String pv;
        private FamilyType ft;
        private Boolean isReal;
        private String exceptionMsg;
        private String automationName;

        public ADBTData host(String host) {
            this.host = host;
            return this;
        }

        public ADBTData port(Integer port) {
            this.port = port;
            return this;
        }

        public ADBTData deviceID(String did) {
            this.deviceID = did;
            return this;
        }

        public ADBTData platformVersion(String pv) {
            this.pv = pv;
            return this;
        }

        public ADBTData familyType(FamilyType ft) {
            this.ft = ft;
            return this;
        }

        public ADBTData realDevice(Boolean isReal) {
            this.isReal = isReal;
            return this;
        }

        public ADBTData exceptionMsg(String msg) {
            this.exceptionMsg = msg;
            return this;
        }

        public ADBTData automationName(String automationName) {
            this.automationName = automationName;
            return this;
        }
    }

    @Test(dataProvider = "ingredientsData")
    public void testADBWithIngredients(ADBTData data) {
        AppiumServerController asc = Mockito.mock(AppiumServerController.class);
        Mockito.when(asc.checkServerState(Mockito.any(URL.class))).thenReturn(true);
        Mockito.when(asc.hasRunningSessions(Mockito.any(URL.class))).thenReturn(false);

        // @formatter:off
        PhoenixDriverIngredients pdi = new PhoenixDriverIngredients()
            .addCustomDriverConfiguration(AppiumDriverBuilder.AUTOMATION_NAME, data.automationName)
            .addCustomDriverConfiguration(AppiumDriverBuilder.DEVICE_ID, data.deviceID)
            .addCustomDriverConfiguration(AppiumDriverBuilder.FAMILY_TYPE, data.ft)
            .addCustomDriverConfiguration(AppiumDriverBuilder.HOST, data.host)
            .addCustomDriverConfiguration(AppiumDriverBuilder.IS_REAL_DEVICE, data.isReal)
            .addCustomDriverConfiguration(AppiumDriverBuilder.PLATFORM_VERSION, data.pv)
            .addCustomDriverConfiguration(AppiumDriverBuilder.PORT, data.port)
            .addCustomDriverConfiguration(AppiumDriverBuilder.APPIUM_SERVER_CONTROLLER, asc);
        // @formatter:on

        try {
            AbstractAppiumPhoenixDriver driver = new MyADB(pdi, new DesiredCapabilities()).createDriver();
            if (data.ft == FamilyType.IPHONE || data.ft == FamilyType.IPAD) {
                assertEquals(driver.getClass(), AppiumIosPhoenixDriver.class, "Unexpected driver type");
            }
            else {
                assertEquals(driver.getClass(), AppiumAndroidPhoenixDriver.class, "Unexpected driver type");
            }
        }
        catch (FlyingPhoenixException e) {
            if (null == data.exceptionMsg) {
                fail("Should've built the driver..." + e.getLocalizedMessage());
            }
            assertEquals(e.getMessage(), data.exceptionMsg, "Exception messages didn't match");
        }
    }

    @Test
    public void testNotRunning() {
        AppiumServerController asc = Mockito.mock(AppiumServerController.class);
        Mockito.when(asc.checkServerState(Mockito.any(URL.class))).thenReturn(false);

        // @formatter:off
        PhoenixDriverIngredients pid = new PhoenixDriverIngredients()
            .addMobileOS(new MobileOS(OSType.IPHONE))
            .addDriverCapability("browserName", "Safari")
            .addCustomDriverConfiguration(AppiumDriverBuilder.HOST, "localhost")
            .addCustomDriverConfiguration(AppiumDriverBuilder.PORT, 4723)
            .addCustomDriverConfiguration(AppiumDriverBuilder.PLATFORM_VERSION, "8.1")
            .addCustomDriverConfiguration(AppiumDriverBuilder.IS_REAL_DEVICE, false)
            .addCustomDriverConfiguration(AppiumDriverBuilder.APPIUM_SERVER_CONTROLLER, asc);


        // @formatter:on
        AppiumDriverBuilder adb = new AppiumDriverBuilder(pid, new DesiredCapabilities());
        try {
            adb.createDriver();
            fail("Driver should not have been created");
        }
        catch (FlyingPhoenixException e) {
            assertEquals(e.getMessage(), "URL 'http://localhost:4723/wd/hub' is not reachable. Could be that Appium server is not running", "Unexpected exception message");
        }
    }

    @Test
    public void testHasSessions() {
        AppiumServerController asc = Mockito.mock(AppiumServerController.class);
        Mockito.when(asc.checkServerState(Mockito.any(URL.class))).thenReturn(true);
        Mockito.when(asc.hasRunningSessions(Mockito.any(URL.class))).thenReturn(true);

        // @formatter:off
        PhoenixDriverIngredients pid = new PhoenixDriverIngredients()
        .addMobileOS(new MobileOS(OSType.IPHONE))
        .addDriverCapability("browserName", "Safari")
        .addCustomDriverConfiguration(AppiumDriverBuilder.HOST, "localhost")
        .addCustomDriverConfiguration(AppiumDriverBuilder.PORT, 4723)
        .addCustomDriverConfiguration(AppiumDriverBuilder.PLATFORM_VERSION, "8.1")
        .addCustomDriverConfiguration(AppiumDriverBuilder.IS_REAL_DEVICE, false)
        .addCustomDriverConfiguration(AppiumDriverBuilder.APPIUM_SERVER_CONTROLLER, asc);


        // @formatter:on
        AppiumDriverBuilder adb = new AppiumDriverBuilder(pid, new DesiredCapabilities());
        try {
            adb.createDriver();
            fail("Driver should not have been created");
        }
        catch (FlyingPhoenixException e) {
            assertEquals(e.getMessage(), "Appium instance has at least one active session.", "Unexpected exception message");
        }
    }

    @DataProvider(name = "ingredientsData")
    public Iterator<Object[]> ingrData() {
        List<Object[]> rv = new ArrayList<Object[]>();

        // @formatter:off
        rv.add(new Object[] {
                new ADBTData().host("localhost").port(4823).deviceID("iPhone Simulator").platformVersion("8.1").familyType(FamilyType.IPHONE).realDevice(false)
        });
        rv.add(new Object[] {
                new ADBTData().host("localhost").port(4823).deviceID("Android Emulator").platformVersion("8.1").familyType(FamilyType.ANDROID).realDevice(false)
        });
        rv.add(new Object[] {
                new ADBTData().exceptionMsg("Appium Host is a required argument")
        });
        rv.add(new Object[] {
                new ADBTData().host("localhost").deviceID("iPhone Simulator").platformVersion("8.1").familyType(FamilyType.IPHONE).realDevice(false)
        });
        rv.add(new Object[] {
                new ADBTData().host("localhost").deviceID("iPhone Simulator").platformVersion("8.1").familyType(FamilyType.IPHONE).realDevice(true)
        });
        rv.add(new Object[] {
                new ADBTData().host("localhost").exceptionMsg("FamilyType is a required argument")
        });
        rv.add(new Object[] {
                new ADBTData().host("localhost").familyType(FamilyType.IPHONE).deviceID("iPhone Simulator").exceptionMsg("Platform Version is a required argument")
        });
        rv.add(new Object[] {
                new ADBTData().host("localhost").familyType(FamilyType.ANDROID).deviceID("Android Emulator").exceptionMsg("Either platform version or automation name is required for Android devices")
        });
        rv.add(new Object[] {
                new ADBTData().host("localhost").familyType(FamilyType.IPAD).deviceID("iPad Simulator").automationName("appium").exceptionMsg("Platform Version is a required argument")
        });
        rv.add(new Object[] {
                new ADBTData().host("localhost").familyType(FamilyType.ANDROID).automationName("appium").realDevice(false)
        });
        rv.add(new Object[] {
                new ADBTData().host("localhost").familyType(FamilyType.IPHONE).realDevice(true).platformVersion("8.1").exceptionMsg("Device ID is a required parameter for a real device testing")
        });
        rv.add(new Object[] {
                new ADBTData().host("localhost").familyType(FamilyType.ANDROID).realDevice(false).platformVersion("4.4")
        });
        rv.add(new Object[] {
                new ADBTData().host("localhost").familyType(FamilyType.IPHONE).realDevice(false).platformVersion("8.1")
        });
        rv.add(new Object[] {
                new ADBTData().host("localhost").familyType(FamilyType.IPAD).realDevice(false).platformVersion("8.1")
        });
        rv.add(new Object[] {
                new ADBTData().host("localhost").familyType(FamilyType.ANDROID).realDevice(false).platformVersion("3.1")
        });
        rv.add(new Object[] {
                new ADBTData().host("localhost").familyType(FamilyType.ANDROID).realDevice(false).platformVersion("4.4")
        });
        rv.add(new Object[] {
                new ADBTData().host("local host").familyType(FamilyType.ANDROID).realDevice(false).platformVersion("4.4").exceptionMsg("1There was an error while creating appium URL.")
        });
        rv.add(new Object[] {
                new ADBTData().host("local host").familyType(FamilyType.ANDROID).realDevice(false).platformVersion("4.4").exceptionMsg("1There was an error while creating appium URL.")
        });
        // @formatter:on

        return rv.iterator();
    }
}
