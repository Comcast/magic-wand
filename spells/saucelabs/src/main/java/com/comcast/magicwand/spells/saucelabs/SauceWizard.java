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
package com.comcast.magicwand.spells.saucelabs;

import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.PhoenixDriver;
import com.comcast.magicwand.wizards.AbstractWizard;
import com.comcast.magicwand.exceptions.FlyingPhoenixException;

/**
 * A wizard responsible for creating a driver for Sauce Labs
 * @author Trent Schmidt
 *
 */
public class SauceWizard extends AbstractWizard {

    /**
     * Creates an instance of the SauceWizard
     * @param ingredients Ingredients to use to create a driver
     */
    public SauceWizard(PhoenixDriverIngredients ingredients) {
        super(ingredients, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhoenixDriver createDriver() throws SauceException, FlyingPhoenixException {
        SauceProvider sauceDriverProvider = new SauceProvider(this.ingredients, this.ingredients.getDriverCapabilities());
        return sauceDriverProvider.buildDriver();
    }
}
