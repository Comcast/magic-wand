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
import com.comcast.magicwand.wizards.WizardFactory;
import com.comcast.magicwand.drivers.PhoenixDriver;
import com.comcast.magicwand.exceptions.FlyingPhoenixException;

/**
 * Factory for creating a driver for use with Sauce Labs
 *
 * @author Trent Schmidt
 *
 */
public class SauceWizardFactory implements WizardFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public PhoenixDriver create(PhoenixDriverIngredients ingredients) throws SauceException, FlyingPhoenixException {
        PhoenixDriver rv = null;
        SauceWizard wizard = new SauceWizard(ingredients);

        rv = wizard.createDriver();

        return rv;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWizardFactoryName() {
        return "SauceWizardFactory";
    }
}
