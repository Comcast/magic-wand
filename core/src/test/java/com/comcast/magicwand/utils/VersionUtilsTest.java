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
package com.comcast.magicwand.utils;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class VersionUtilsTest {

    @DataProvider(name = "versions")
    public Object[][] version() {
        return new Object[][] {
            new Object[] { "1.2.3" , "1.2.3" , 0  } ,
            new Object[] { "1.2"   , "1.2.0" , -1 } ,
            new Object[] { "a.b.c" , "1.2.3" , 1  } ,
            new Object[] { "1.2"   , "1.1"   , 1  }
        };
    }

    @Test(dataProvider = "versions")
    public void testVersionCompare(String version1, String version2, Integer expectedResult) {
        Integer res = VersionUtils.compare(version1, version2);

        Assert.assertEquals(res, expectedResult);
    }
}
