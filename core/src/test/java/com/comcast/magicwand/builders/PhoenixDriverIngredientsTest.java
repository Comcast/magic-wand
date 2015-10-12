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
package com.comcast.magicwand.builders;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.Iterator;

import org.openqa.selenium.remote.BrowserType;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.magicwand.utils.SystemDetail;
import com.comcast.magicwand.enums.DesktopOS;
import com.comcast.magicwand.enums.MobileOS;
import com.comcast.magicwand.enums.OSType;

public class PhoenixDriverIngredientsTest {

    private static final String curOS = System.getProperty("os.name");

    @AfterTest
    public void tearDown() {
        System.setProperty("os.name", curOS);
    }

    @AfterMethod
    public void tearDown2() {
        // TODO: figure out why after test did not work
        System.setProperty("os.name", curOS);
    }

    @DataProvider(name = "Configurations")
    public Object[][] generateMacConfigs() {
        DesktopOS mac = new DesktopOS(OSType.MAC);
        DesktopOS linux = new DesktopOS(OSType.LINUX);
        DesktopOS win = new DesktopOS(OSType.WINDOWS);
        MobileOS iphone = new MobileOS(OSType.IPHONE);
        MobileOS android = new MobileOS(OSType.ANDROID);

        OSType expectedOS;
        DesktopOS dynamicOS;
        String dynamicBrowser;
        if (SystemDetail.deviceIsLinux()) {
            expectedOS = OSType.LINUX;
            dynamicOS = linux;
            dynamicBrowser = BrowserType.CHROME;
        }
        else if (SystemDetail.deviceIsRunningMac()) {
            expectedOS = OSType.MAC;
            dynamicOS = mac;
            dynamicBrowser = BrowserType.SAFARI;
        }
        else if (SystemDetail.deviceIsRunningWindows()) {
            expectedOS = OSType.WINDOWS;
            dynamicOS = win;
            dynamicBrowser = BrowserType.IE;
        }
        else {
            expectedOS = null;
            dynamicBrowser = null;
            dynamicOS = null;
        }

        return new Object[][] {
                // fields are:
                // Desktop OS, Mobile OS, Browser Type, expected OS, expected Mobile, expected browser
                // @formatter:off
                new Object[] {dynamicOS, null, null, expectedOS, null, dynamicBrowser},
                new Object[] {mac, null, null, OSType.MAC, null, BrowserType.SAFARI},
                new Object[] {mac, null, BrowserType.FIREFOX, OSType.MAC, null, BrowserType.FIREFOX},
                new Object[] {mac, iphone, BrowserType.SAFARI, OSType.MAC, OSType.IPHONE, BrowserType.SAFARI},
                new Object[] {mac, android, BrowserType.CHROME, OSType.MAC, OSType.ANDROID, BrowserType.CHROME},
                new Object[] {linux, null, null, OSType.LINUX, null, BrowserType.CHROME},
                new Object[] {win, null, null, OSType.WINDOWS, null, BrowserType.IE}
                // @formatter:on
        };
    }

    @Test(dataProvider = "Configurations")
    public void testMacConfigurationDesktop(DesktopOS dos, MobileOS mos, String browser, OSType eDos, OSType eMos, String eBrowser) {
        // @formatter:off
        PhoenixDriverIngredients i = new PhoenixDriverIngredients()
            .addBrowser(browser)
            .addDesktopOS(dos)
            .addMobileOS(mos);
        // @formatter:on

        assertEquals(i.getDesktopOS().getType(), eDos, "Unexpected desktop os");
        if (null == eMos) {
            assertEquals(i.getMobileOS(), null, "Unexpected mobile os");
        }
        else {
            assertEquals(i.getMobileOS().getType(), eMos, "Unexpected mobile os");
        }
        assertEquals(i.getBrowser(), eBrowser, "Unexpected browser");
    }


    @DataProvider(name = "defaultIngredients")
    public Iterator<Object[]> osGenerator() {
        return new ArrayList<Object[]>() {
            {
                // @formatter:off
                add(new Object[] { OSType.MAC, BrowserType.SAFARI });
                add(new Object[] { OSType.LINUX, BrowserType.CHROME });
                add(new Object[] { OSType.WINDOWS, BrowserType.IE });
                add(new Object[] { OSType.UNIX, BrowserType.CHROME });
            }
            // @formatter:on

        }.iterator();
    }

    @DataProvider(name = "validation")
    public Iterator<Object[]> generateValidation() {
        return new ArrayList<Object[]>() {{
            DesktopOS win = new DesktopOS(OSType.WINDOWS);
            DesktopOS mac = new DesktopOS(OSType.MAC);
            MobileOS iphone = new MobileOS(OSType.IPHONE);
            MobileOS ipad = new MobileOS(OSType.IPAD);
            MobileOS android = new MobileOS(OSType.ANDROID);

            // desktop os, mobile os, browser type, is null?
            // @formatter:off
            add(new Object[] {mac, null, "", true});
            add(new Object[] {mac, null, BrowserType.IE, false});
            add(new Object[] {win, iphone, null, false});
            add(new Object[] {win, ipad, null, false});
            add(new Object[] {win, iphone, null, false});
            add(new Object[] {win, android, null, false});
            add(new Object[] {mac, ipad, BrowserType.CHROME, false});
            add(new Object[] {mac, ipad, null, false});
            add(new Object[] {mac, ipad, BrowserType.SAFARI, false});
            add(new Object[] {mac, android, BrowserType.SAFARI, false});
            add(new Object[] {mac, android, BrowserType.CHROME, false});
            // @formatter:on
        }}.iterator();
    }

    @Test(dataProvider = "validation")
    public void testBuildValidation(DesktopOS dos, MobileOS mos, String browserType, boolean isValid) {
        PhoenixDriverIngredients i = new PhoenixDriverIngredients();
        if (null != dos) {
            i.addDesktopOS(dos);
        }
        if (null != mos) {
            i.addMobileOS(mos);
        }
        if (null != browserType) {
            i.addBrowser(browserType);
        }

        i = i.verify();

        if (isValid) {
            assertNull(i, "Ingredients should've been null due to validation");
        }
        else {
            assertNotNull(i, "Ingredients should have been non-null due to validation");
        }
    }

    @Test(dataProvider = "defaultIngredients")
    public void testDefaultRanking(OSType curSystemType, String browserType) {
        switch (curSystemType) {
        case MAC:
            System.setProperty("os.name", "mac");
            break;
        case LINUX:
            System.setProperty("os.name", "nux");
            break;
        case UNIX:
            System.setProperty("os.name", "nix");
            break;
        case WINDOWS:
            System.setProperty("os.name", "win");
            break;

        default:
            break;
        }

        PhoenixDriverIngredients i = new PhoenixDriverIngredients();

        assertEquals(i.getBrowser(), browserType, "Unexpected browser type");
        assertEquals(i.getDesktopOS().getType(), curSystemType, "Unexpected current system type");
    }

    @Test
    public void testIngredientsBuilder() {
        // @formatter:off
        PhoenixDriverIngredients i = new PhoenixDriverIngredients()
            .addDesktopOS(new DesktopOS(OSType.MAC))
            .addBrowser(BrowserType.CHROME);
        // @formatter:on

        assertNotNull(i, "Ingredients are valid");
        assertEquals(i.getDesktopOS().getType(), OSType.MAC, "Unexpected OS Type");
        assertEquals(i.getBrowser(), BrowserType.CHROME, "Unexpected Browser Type");
        assertNull(i.getMobileOS(), "Mobile OS should've been null");
    }
}
