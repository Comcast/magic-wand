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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.PhoenixDriver;

public class ChromePhoenixDriverIT {

    @Test
    public void downloadLatestChromeDriver() {
        ChromeWizardFactory cwf = new ChromeWizardFactory();
        PhoenixDriverIngredients ing = new PhoenixDriverIngredients().verify();

        PhoenixDriver driver = cwf.create(ing);

        driver.close();
    }

    @DataProvider
    public Iterator<Object []> versions() {
        List<Object []> testCases = new ArrayList<Object []>();

        testCases.add(new Object[] {"2.18"});
        testCases.add(new Object[] {"2.15"});
        testCases.add(new Object[] {"2.17"});

        return testCases.iterator();
    }

    @Test(dataProvider="versions")
    public void downloadOlderChromeDriver(String versions) {
        ChromeWizardFactory cwf = new ChromeWizardFactory();
        PhoenixDriverIngredients ing = new PhoenixDriverIngredients().verify();

        ing.addCustomDriverConfiguration(ChromePhoenixDriver.CHROME_DRIVER_VERSION, versions);

        PhoenixDriver driver = cwf.create(ing);

        driver.close();
    }
}
