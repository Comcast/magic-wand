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
package com.comcast.magicwand.wizards;

import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.PhoenixDriver;

/**
 * An interface for defining new wizards
 * @author Trent Schmidt
 *
 */
public interface WizardFactory {

    /**
     * Responsible for creating {@link PhoenixDriver}
     *
     * @param ingredients Set of settings used to create the driver
     * @return Instance of a driver
     * @throws Throwable If there were any issues while creating a driver
     */
    PhoenixDriver create(PhoenixDriverIngredients ingredients) throws Throwable;

    /**
     * @return The string identifier for this wizard factory
     */
    String getWizardFactoryName();
}
