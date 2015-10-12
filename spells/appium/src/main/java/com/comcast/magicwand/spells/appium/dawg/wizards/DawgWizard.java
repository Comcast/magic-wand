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
package com.comcast.magicwand.spells.appium.dawg.wizards;

import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.PhoenixDriver;
import com.comcast.magicwand.spells.appium.AbstractAppiumPhoenixDriver;
import com.comcast.magicwand.spells.appium.AppiumDriverBuilder;
import com.comcast.magicwand.spells.appium.dawg.utils.AppiumServerController;
import com.comcast.magicwand.spells.appium.dawg.utils.DawgAppiumProvider;
import com.comcast.magicwand.exceptions.FlyingPhoenixException;
import com.comcast.video.dawg.house.DawgPoundClient;
import com.comcast.magicwand.wizards.AbstractWizard;
import com.comcast.magicwand.spells.appium.dawg.meta.MetaAppiumDevice.FamilyType;

/**
 * Responsible for creating {@link AbstractAppiumPhoenixDriver} based on metadata stored in the Dawg House
 *
 * @author Dmitry Jerusalimsky
 *
 */
public class DawgWizard extends AbstractWizard {

    /**
     * Creates an instance of the {@link DawgWizard}
     *
     * @param ingredients Set of ingredients to create the driver
     */
    public DawgWizard(PhoenixDriverIngredients ingredients) {
        super(ingredients, false);
    }

    /**
     * {@inheritDoc}
     *
     * @throws FlyingPhoenixException If there were problems creating a driver
     */
    @Override
    public PhoenixDriver createDriver() throws FlyingPhoenixException {
        AppiumServerController asc = (AppiumServerController) this.driverConfig
                .get(AppiumDriverBuilder.APPIUM_SERVER_CONTROLLER);
        String dawgToken = (String) this.driverConfig.get(DawgAppiumProvider.DAWG_TOKEN);
        String poundUrl = (String) this.driverConfig.get(DawgAppiumProvider.POUND_URL);
        DawgPoundClient client = (DawgPoundClient) this.driverConfig.get(DawgAppiumProvider.DAWG_POUND_CLIENT);

        asc = (null == asc) ? new AppiumServerController() : asc;
        client = (null == client) ? new DawgPoundClient() : client;
        FamilyType familyType = getFamilyType();

        DawgAppiumProvider dap = new DawgAppiumProvider().withAppiumServerController(asc).forDeviceType(familyType)
                .withDawgClient(client).withCapabilities(this.customCapabilities);

        dap = dap.forDawgPoundURL(poundUrl).forDawgToken(dawgToken);

        // appium-specific capabilities will be generated in the appium provider
        return dap.build();
    }

    /**
     * Gets the family type based on the mobile OS type
     *
     * @return
     */
    private FamilyType getFamilyType() {
        FamilyType rv = null;

        // determine device family type
        switch (this.mobileOS.getType()) {
        case ANDROID:
            rv = FamilyType.ANDROID;
            break;
        case IPAD:
            rv = FamilyType.IPAD;
            break;
        case IPHONE:
            rv = FamilyType.IPHONE;
            break;
        default:
            rv = null;
        }

        return rv;
    }
}
