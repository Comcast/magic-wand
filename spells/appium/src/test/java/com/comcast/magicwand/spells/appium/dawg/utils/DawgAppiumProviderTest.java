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
package com.comcast.magicwand.spells.appium.dawg.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.mockito.Mockito;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.spells.appium.dawg.meta.MetaAppiumDevice;
import com.comcast.magicwand.spells.appium.dawg.meta.MetaAppiumDevice.FamilyType;
import com.comcast.magicwand.drivers.PhoenixDriver;
import com.comcast.magicwand.spells.appium.AppiumAndroidPhoenixDriver;
import com.comcast.magicwand.spells.appium.AppiumDriverBuilder;
import com.comcast.magicwand.spells.appium.AppiumIosPhoenixDriver;
import com.comcast.magicwand.exceptions.FlyingPhoenixException;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.house.DawgPoundClient;

import com.comcast.magicwand.spells.appium.dawg.utils.AppiumServerController;
import com.comcast.magicwand.spells.appium.dawg.utils.DawgAppiumProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DawgAppiumProviderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DawgAppiumProviderTest.class);

    private static final String DAWG_POUND_URL = "http://dawg-pound.cvs-a.ula.comcast.net:8080/dawg-pound/reservations";

    private class TestProvider {
        private int testCount;

        public String token;
        public String poundUrl;
        public DawgPoundClient dpc;
        public AppiumServerController appiumServerController;
        public FamilyType ft;
        public Class<? extends Exception> expectedException;
        public String expectedExceptionMsg;
        public Class<? extends PhoenixDriver> expectedDriver;
        public DawgAppiumProvider newDawgAppiumProvider = new DawgAppiumProvider();

        private Class<? extends Exception> cause;

        private String causeMsg;

        public TestProvider(int instanceCount) {
            this.testCount = instanceCount;
        }

        public TestProvider setDawgAppiumProvider(DawgAppiumProvider dap) {
            this.newDawgAppiumProvider = dap;
            return this;
        }


        public DawgAppiumProvider newDawgAppiumProvider() {
            return this.newDawgAppiumProvider;
        }

        public int getTestCount() {
            return testCount;
        }

        public String getToken() {
            return token;
        }
        public TestProvider setToken(String token) {
            this.token = token;
            return this;
        }
        public String getPoundUrl() {
            return poundUrl;
        }
        public TestProvider setPoundUrl(String poundUrl) {
            this.poundUrl = poundUrl;
            return this;
        }
        public DawgPoundClient getDpc() {
            return dpc;
        }
        public TestProvider setDpc(DawgPoundClient dpc) {
            this.dpc = dpc;
            return this;
        }
        public AppiumServerController getAppiumServerController() {
            return appiumServerController;
        }
        public TestProvider setAppiumServerController(
                AppiumServerController appiumServerController) {
            this.appiumServerController = appiumServerController;
            return this;
        }
        public FamilyType getFt() {
            return ft;
        }
        public TestProvider setFt(FamilyType ft) {
            this.ft = ft;
            return this;
        }
        public Class<? extends Exception> getExpectedException() {
            return expectedException;
        }
        public TestProvider setExpectedException(Class<? extends Exception> expectedException) {
            this.expectedException = expectedException;
            return this;
        }

        public TestProvider setExpectedExceptionCause(Class<? extends Exception> cause) {
            this.cause = cause;
            return this;
        }
        public String getExpectedExceptionMsg() {
            return expectedExceptionMsg;
        }
        public TestProvider setExpectedExceptionMsg(String expectedExceptionMsg) {
            this.expectedExceptionMsg = expectedExceptionMsg;
            return this;
        }
        public TestProvider setCauseMsg(String causeMsg) {
            this.causeMsg = causeMsg;
            return this;
        }
        public Class<? extends PhoenixDriver> getExpectedDriver() {
            return expectedDriver;
        }
        public TestProvider setExpectedDriver(Class<? extends PhoenixDriver> expectedDriver) {
            this.expectedDriver = expectedDriver;
            return this;
        }
    }

    private List<MetaStb> android;
    private List<MetaStb> iphone;
    private List<MetaStb> both;
    private DawgPoundClient client;
    private AppiumServerController asc;

    @SuppressWarnings("unchecked")
    private List<MetaStb> createSTBFromResource(String resourceName) throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
        List<MetaStb> rv = new ArrayList<MetaStb>();

        URL path = getClass().getResource(resourceName);
        File f = new File(path.toURI());
        HashMap<String, Object> devObj = new ObjectMapper().readValue(f, HashMap.class);
        MetaStb stb = new MetaStb(devObj);

        rv.add(stb);

        return rv;
    }

    @BeforeClass
    public void setup() {
        try {
            this.android = createSTBFromResource("/android.json");
            this.iphone = createSTBFromResource("/ios.json");
            this.both = new ArrayList<MetaStb>(this.android);
            this.both.addAll(iphone);

            this.client = mock(DawgPoundClient.class);
            when(client.getReservedDevices(Mockito.matches("android"))).thenReturn(this.android);
            when(client.getReservedDevices(Mockito.matches("iphone"))).thenReturn(this.iphone);

            asc = mock(AppiumServerController.class);
            when(asc.checkServerState(Mockito.any(URL.class))).thenReturn(true);
            when(asc.hasRunningSessions(Mockito.any(URL.class))).thenReturn(false);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @DataProvider(name = "testData")
    public Iterator<Object[]> generateTestData() {
        List<Object[]> testData = new ArrayList<Object[]>();
        int i = 1;

        testData.add(new Object[] { new TestProvider(i++)
            .setToken("android")
            .setPoundUrl(DAWG_POUND_URL)
            .setDpc(this.client)
            .setAppiumServerController(this.asc)
            .setFt(FamilyType.ANDROID)
            .setExpectedException(null)
            .setExpectedExceptionMsg(null)
            .setExpectedDriver(AppiumAndroidPhoenixDriver.class)
        });

        testData.add(new Object[] { new TestProvider(i++)
            .setToken("iphone")
            .setPoundUrl(DAWG_POUND_URL)
            .setDpc(this.client)
            .setAppiumServerController(this.asc)
            .setFt(FamilyType.IPHONE)
            .setExpectedException(null)
            .setExpectedExceptionMsg(null)
            .setExpectedDriver(AppiumIosPhoenixDriver.class)
        });

        testData.add(new Object[] { new TestProvider(i++)
            .setToken("android")
            .setPoundUrl(DAWG_POUND_URL)
            .setDpc(this.client)
            .setAppiumServerController(this.asc)
            .setFt(FamilyType.IPHONE)
            .setExpectedException(null)
            .setExpectedExceptionMsg(null)
            .setExpectedDriver(null)
        });

        testData.add(new Object[] { new TestProvider(i++)
            .setToken("iphone")
            .setPoundUrl(DAWG_POUND_URL)
            .setDpc(this.client)
            .setAppiumServerController(this.asc)
            .setFt(FamilyType.ANDROID)
            .setExpectedException(null)
            .setExpectedExceptionMsg(null)
            .setExpectedDriver(null)
        });

        testData.add(new Object[] { new TestProvider(i++)
            .setToken("android")
            .setPoundUrl(DAWG_POUND_URL)
            .setDpc(null)
            .setAppiumServerController(this.asc)
            .setFt(FamilyType.ANDROID)
            .setExpectedException(FlyingPhoenixException.class)
            .setExpectedExceptionMsg("Errors while validating builder parameters")
            .setExpectedExceptionCause(FlyingPhoenixException.class)
            .setCauseMsg("You must specify DawgClient")
            .setExpectedDriver(null)
            .setDawgAppiumProvider(new DawgAppiumProvider() {
                    @Override
                    protected DawgPoundClient newDawgPoundClient() {
                        DawgPoundClient m = Mockito.mock(DawgPoundClient.class);
                        return m;
                    }
                })
        });

        testData.add(new Object[] { new TestProvider(i++)
            .setToken("android")
            .setPoundUrl(DAWG_POUND_URL)
            .setDpc(this.client)
            .setAppiumServerController(this.asc)
            .setFt(null)
            .setExpectedException(FlyingPhoenixException.class)
            .setExpectedExceptionMsg("Errors while validating builder parameters")
            .setExpectedExceptionCause(FlyingPhoenixException.class)
            .setCauseMsg("You must specify device family type")
            .setExpectedDriver(null)
        });

        return testData.iterator();
    }

    @Test(dataProvider = "testData")
    public void testDAP(TestProvider testData) {
        if (8 == testData.getTestCount() ) {
            LOGGER.debug("testCount");
        }
        try {

            MetaAppiumDevice appiumMeta = null;
            if ("android".equals(testData.getToken())) {
                appiumMeta = new MetaAppiumDevice(this.android.get(0).getData());
            }
            else if("iphone".equals(testData.getToken())) {
                appiumMeta = new MetaAppiumDevice(this.iphone.get(0).getData());
            }


            PhoenixDriverIngredients ingredients = null;
            if (null != appiumMeta) {
                // @formatter:off
                ingredients = new PhoenixDriverIngredients()
                    .addCustomDriverConfiguration(AppiumDriverBuilder.DEVICE_ID, appiumMeta.getDeviceId())
                    .addCustomDriverConfiguration(AppiumDriverBuilder.FAMILY_TYPE, appiumMeta.getFamilyType())
                    .addCustomDriverConfiguration(AppiumDriverBuilder.HOST, appiumMeta.getIpAddress().getHostAddress())
                    .addCustomDriverConfiguration(AppiumDriverBuilder.IS_REAL_DEVICE, appiumMeta.getIsRealDevice())
                    .addCustomDriverConfiguration(AppiumDriverBuilder.PLATFORM_VERSION, appiumMeta.getPlatformVersion())
                    .addCustomDriverConfiguration(AppiumDriverBuilder.PORT, appiumMeta.getServerPortNumber())
                    .addCustomDriverConfiguration(AppiumDriverBuilder.APPIUM_SERVER_CONTROLLER, this.asc);
                // @formatter:on
            }

            AppiumDriverBuilder adb = new MyADB(ingredients, new DesiredCapabilities());
            DawgAppiumProvider dap = testData.newDawgAppiumProvider()
                .withAppiumDriverBuilder(adb)
                .forDawgToken(testData.getToken())
                .forDawgPoundURL(testData.getPoundUrl())
                .withDawgClient(testData.getDpc())
                .forDeviceType(testData.getFt())
                .withAppiumServerController(testData.getAppiumServerController());

            PhoenixDriver driver = dap.build();

            if (null == testData.getExpectedDriver()) {
                assertNull(driver, "Driver should not have been created!");
            }
            else {
                assertNotNull(driver, "Driver should have been created");
                assertEquals(driver.getClass(), testData.getExpectedDriver(), "Unexpected driver was created");
            }
        }
        catch (FlyingPhoenixException e) {
            assertEquals(e.getClass(), testData.getExpectedException(), "Unexpected exception. " + e.getMessage());
            assertEquals(e.getMessage(), testData.expectedExceptionMsg, "Unexpected exception cause message");
            if (null != testData.cause) {
                assertEquals(e.getCause().getClass(), testData.cause, "Unexpected exception cause. " + e.getCause().getMessage());
                assertEquals(e.getCause().getMessage(), testData.causeMsg, "Unexpected exception cause message");
            }
        }
    }

    private class MyADB extends AppiumDriverBuilder {

        public MyADB(PhoenixDriverIngredients ingredients, DesiredCapabilities capabilities) {
            super(ingredients, capabilities);
        }
        private AndroidDriver androidDriver = Mockito.mock(AndroidDriver.class);
        private IOSDriver iosDriver = Mockito.mock(IOSDriver.class);


        @Override
        protected AndroidDriver createAndroidDriver(URL url, DesiredCapabilities caps) {
            return this.androidDriver;
        }

        @Override
        protected IOSDriver createIosDriver(URL url, DesiredCapabilities caps) {
            return this.iosDriver;
        }

        /**
         * @return the androidDriver
         */
        @SuppressWarnings("unused")
        public AndroidDriver getAndroidDriver() {
            return androidDriver;
        }

        /**
         * @param androidDriver the androidDriver to set
         */
        @SuppressWarnings("unused")
        public void setAndroidDriver(AndroidDriver androidDriver) {
            this.androidDriver = androidDriver;
        }

        /**
         * @return the iosDriver
         */
        @SuppressWarnings("unused")
        public IOSDriver getIosDriver() {
            return iosDriver;
        }

        /**
         * @param iosDriver the iosDriver to set
         */
        @SuppressWarnings("unused")
        public void setIosDriver(IOSDriver iosDriver) {
            this.iosDriver = iosDriver;
        }
    }
}
