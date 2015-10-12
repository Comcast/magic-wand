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
package com.comcast.magicwand.test;

import java.util.List;
import java.util.Iterator;

import com.comcast.magicwand.wizards.WizardFactory;
import com.comcast.magicwand.builders.PhoenixDriverBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;

public class PhoenixDriverTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(PhoenixDriverTest.class);

    private static final int EXPECTED_COUNT = 7;

    private List<WizardFactory> factories;

    @BeforeMethod
    public void setup() {
        this.factories = PhoenixDriverBuilder.loadAllFactories();
    }

    @Test
    public void LoadedIncludedFactories() {
        if(this.factories.size() >= EXPECTED_COUNT) {
            LOGGER.debug("Found {} WizardFactory's", this.factories.size());
            int fact_idx = 0;
            Iterator<WizardFactory> iter = this.factories.iterator();
            while(iter.hasNext())
                LOGGER.debug("\tfactory[{}]: {}", fact_idx++, iter.next());
        }
        else {
            Assert.fail(String.format("Expected at least %d WizardFactory's, but found %d WizardFactory's", EXPECTED_COUNT, this.factories.size()));
        }
    }
}
