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

import java.util.Collection;

import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.spells.appium.dawg.meta.MetaAppiumDevice;
import com.comcast.magicwand.spells.appium.dawg.meta.MetaAppiumDevice.FamilyType;
import com.comcast.magicwand.drivers.PhoenixDriver;
import com.comcast.magicwand.spells.appium.dawg.utils.AppiumServerController;
import com.comcast.magicwand.spells.appium.AbstractAppiumPhoenixDriver;
import com.comcast.magicwand.spells.appium.AppiumDriverBuilder;
import com.comcast.magicwand.exceptions.FlyingPhoenixException;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.house.DawgPoundClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

/**
 * Gets appium drivers from Dawg House
 *
 * @author Dmitry Jerusalimsky
 *
 */
public class DawgAppiumProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DawgAppiumProvider.class);

    public static final String DAWG_TOKEN = "dawgToken";
    public static final String POUND_URL = "poundUrl";
    public static final String DAWG_POUND_CLIENT = "dawgPoundClient";
    public static final String DAWG_PROPERTIES = "dawgProperties";

    private String poundURL;
    private String dawgToken;
    private DawgPoundClient client;
    private FamilyType familyType;
    private AppiumServerController appiumServerController;
    private DesiredCapabilities customCapabilities;

    private AppiumDriverBuilder appiumDriverBuilder;

    /**
     * Manually specified dawg reservation token <br>
     * <em><b>NOTE</b></em>: calling {@link DawgAppiumProvider#withProperties(DawgProperties)} after calling this method
     * would overwrite this value
     *
     * @param dawgToken Dawg reservation token to use to get the reserved devices
     * @return this builder
     */
    public DawgAppiumProvider forDawgToken(String dawgToken) {
        this.dawgToken = dawgToken;

        return this;
    }

    /**
     * Manually specified dawg pund URL <br>
     * <em><b>NOTE</b></em>: calling {@link DawgAppiumProvider#withProperties(DawgProperties)} after calling this method
     * would overwrite this value <br>
     *
     * @param dawgPoundURL Dawg reservation URL
     * @return this builder
     */
    public DawgAppiumProvider forDawgPoundURL(String dawgPoundURL) {
        this.poundURL = dawgPoundURL;

        return this;
    }

    /**
     * Reference of a {@link DawgPoundClient} object to be used to retrieved list of reserved devices
     *
     * @param client Client to be used to retrieve reserved devices
     * @return this builder
     */
    public DawgAppiumProvider withDawgClient(DawgPoundClient client) {
        this.client = client;

        return this;
    }

    /**
     * @return new {@link DawgPoundClient}
     */
    protected DawgPoundClient newDawgPoundClient() {
        return new DawgPoundClient();
    }

    /**
     * Family type of a desired device. Valid values are:
     * <ul>
     * <li>{@link FamilyType#ANDROID}</li>
     * <li>{@link FamilyType#IPHONE}</li>
     * </ul>
     *
     * @param familyType Family type used to filter devices
     * @return this builder
     */
    public DawgAppiumProvider forDeviceType(FamilyType familyType) {
        this.familyType = familyType;

        return this;
    }

    /**
     * Extra capabilities to be used with the created AppiumDriver <em><b>NOTE:</b></em> These capabilities will
     * <em><b>NOTE:</b></em> These capabilities will overwrite potential defaults overwrite potential defaults
     *
     * @param capabilities Extra capabilities to use for AppiumDriver creation
     * @return this builder
     */
    public DawgAppiumProvider withCapabilities(DesiredCapabilities capabilities) {
        this.customCapabilities = capabilities;

        return this;
    }

    /**
     * Sets Appium Server Controller used to determine if Appium server is running and if sessions could be created
     * <em><b>NOTE:</b></em> If this is not set, default controller is used which is defined by
     * {@link AppiumServerController}
     *
     * @param controller Reference to the {@link AppiumServerController} object
     * @return this builder
     */
    public DawgAppiumProvider withAppiumServerController(AppiumServerController controller) {
        this.appiumServerController = controller;

        return this;
    }

    /**
     * Validates that all required parameters have been set by the builder
     *
     * @throws DeviceProviderException In case any parameters were missed. Required parameters are:
     * <ul>
     * <li>Dawg Pound Client. Set using {@link DawgAppiumProvider#withDawgClient(DawgPoundClient)}</li>
     * <li>Dawg Pound URL. Set using {@link DawgAppiumProvider#forDawgPoundURL(String)} or
     * {@link DawgAppiumProvider#withProperties(DawgProperties)}</li>
     * <li>Dawg Pound Token. Set using {@link DawgAppiumProvider#forDawgToken(String)} or
     * {@link DawgAppiumProvider#withProperties(DawgProperties)}</li>
     * {@link DawgAppiumProvider#withProperties(DawgProperties)}</li>
     * <li>Device Family Type. Set using {@link DawgAppiumProvider#forDeviceType(FamilyType)}</li>
     * </ul>
     */
    protected void validateBuilderFields() throws FlyingPhoenixException {
        if (null == this.poundURL) {
            throw new FlyingPhoenixException("You must set the property via DawgAppiumProvider#forDawgPoundURL(String)");
        }

        if (null == this.dawgToken) {
            throw new FlyingPhoenixException("You must set the property DawgAppiumProvider#forDawgToken(String)");
        }

        if (null == this.familyType) {
            throw new FlyingPhoenixException("You must specify device family type");
        }

        if (this.familyType == FamilyType.OTHER) {
            throw new FlyingPhoenixException("Family type must be either Android or iOS");
        }

        if (null == this.client) {
            this.client = newDawgPoundClient();
        }
    }

    /**
     * Generates defaults for fields which can safely be guessed. These must be lazily instantiated.
     */
    protected void generateDefaultsForFields() {
        if (null == this.poundURL && null == this.dawgToken) {
            /* Place defaults here */
        }
    }

    /**
     * Creates a {@link PhoenixDriver} from a reserved device in Dawg House. If no devices were found, <code>null</code>
     * is returned
     *
     * @return Instance of a driver
     * @throws DeviceProviderException In case any parameters were missed. Required parameters are:
     * <ul>
     * <li>Dawg Pound URL. Set using {@link DawgAppiumProvider#forDawgPoundURL(String)} or
     * {@link DawgAppiumProvider#withProperties(DawgProperties)}</li>
     * <li>Dawg Pound Token. Set using {@link DawgAppiumProvider#forDawgToken(String)} or
     * {@link DawgAppiumProvider#withProperties(DawgProperties)}</li>
     * <li>Device Family Type. Set using {@link DawgAppiumProvider#forDeviceType(FamilyType)}</li>
     * </ul>
     * @throws FlyingPhoenixException If Appium is not running or cannot be connected to. This is thrown during
     * {@link IOSDriver} or {@link AndroidDriver} creation
     */
    public PhoenixDriver build() throws FlyingPhoenixException {
        PhoenixDriver rv = null;

        generateDefaultsForFields();
        try {
            validateBuilderFields();
        }
        catch (FlyingPhoenixException e) {
            throw new FlyingPhoenixException("Errors while validating builder parameters", e);
        }

        LOG.info("Loading devices reserved by '" + this.dawgToken + "' user");
        Collection<MetaStb> devices = client.getReservedDevices(this.dawgToken);

        for (MetaStb device : devices) {
            MetaAppiumDevice appiumMeta = new MetaAppiumDevice(device.getData());

            // if device type does not match desired type, go to the next device
            if (this.familyType == appiumMeta.getFamilyType()) {
                try {
                    rv = createDriver(appiumMeta);
                }
                catch (UnreachableBrowserException e) {
                    throw new FlyingPhoenixException("There was a problem creating a driver", e);
                }
            }

            if (null != rv) {
                break;
            }
        }

        return rv;
    }

    /**
     * Creates a driver
     *
     * @param appiumMeta Metadata describing device from Dawg House
     * @return Instance of a driver
     * @throws FlyingPhoenixException If there was an error creating a driver
     */
    protected AbstractAppiumPhoenixDriver createDriver(MetaAppiumDevice appiumMeta) throws FlyingPhoenixException {

        // @formatter:off
        PhoenixDriverIngredients ingredients = new PhoenixDriverIngredients()
            .addCustomDriverConfiguration(AppiumDriverBuilder.DEVICE_ID, appiumMeta.getDeviceId())
            .addCustomDriverConfiguration(AppiumDriverBuilder.FAMILY_TYPE, appiumMeta.getFamilyType())
            .addCustomDriverConfiguration(AppiumDriverBuilder.HOST, appiumMeta.getIpAddress().getHostAddress())
            .addCustomDriverConfiguration(AppiumDriverBuilder.IS_REAL_DEVICE, appiumMeta.getIsRealDevice())
            .addCustomDriverConfiguration(AppiumDriverBuilder.PLATFORM_VERSION, appiumMeta.getPlatformVersion())
            .addCustomDriverConfiguration(AppiumDriverBuilder.PORT, appiumMeta.getServerPortNumber())
            .addCustomDriverConfiguration(AppiumDriverBuilder.APPIUM_SERVER_CONTROLLER, this.appiumServerController);
        // @formatter:on
        this.appiumDriverBuilder = (null == this.appiumDriverBuilder) ? new AppiumDriverBuilder(ingredients,
                this.customCapabilities) : this.appiumDriverBuilder;

        return this.appiumDriverBuilder.createDriver();
    }

    /**
     * Sets appium driver builder
     *
     * @param adb Appium Driver builder to use for driver creation
     * @return this builder
     */
    protected DawgAppiumProvider withAppiumDriverBuilder(AppiumDriverBuilder adb) {
        this.appiumDriverBuilder = adb;

        return this;
    }
}
