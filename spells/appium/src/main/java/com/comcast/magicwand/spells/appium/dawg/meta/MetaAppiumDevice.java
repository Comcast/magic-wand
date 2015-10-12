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
package com.comcast.magicwand.spells.appium.dawg.meta;

import java.util.Map;

import com.comcast.magicwand.spells.appium.dawg.meta.MetaDevice;
import com.comcast.magicwand.spells.appium.AppiumDeviceProperties;

/**
 * A class for accessing metadata for a mobile device in Dawg House
 *
 * @author Dmitry Jerusalimsky
 *
 */
public class MetaAppiumDevice extends MetaDevice implements AppiumDeviceProperties {

    public static final String FAMILY                   = "family";
    public static final String PLATFORM_VERSION         = "platformVersion";
    public static final String PLATFORM_NAME            = "platformName";
    public static final String SERVER_PORT_NUMBER       = "serverPortNumber";
    public static final String BOOTSTRAP_PORT_NUMBER    = "bootstrapPortNumber";
    public static final String REAL_DEVICE              = "isRealDevice";
    public static final String USERNAME                 = "username";
    public static final String PASSWORD                 = "password";

    /**
     * Device family: IPHONE, ANDROID, OTHER
     *
     * @author Dmitry Jerusalimsky
     *
     */
    public enum FamilyType {
        IPHONE, ANDROID, OTHER, IPAD
    }

    /**
     * Creates an instance of a MetaAppiumDevice
     *
     * @param data Metadata that comes from DawgHouse
     */
    public MetaAppiumDevice(Map<String, Object> data) {
        super(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlatformVersion() {
        return (String) this.myData.get(PLATFORM_VERSION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlatformVersion(String platformVersion) {
        this.myData.put(PLATFORM_VERSION, platformVersion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlatformName() {
        return (String) this.myData.get(PLATFORM_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlatformName(String platformName) {
        this.myData.put(PLATFORM_NAME, platformName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getServerPortNumber() {
        String portNumber = (String) this.myData.get(SERVER_PORT_NUMBER);
        if (portNumber == null || portNumber.trim().length() == 0) {
            return -1;
        }
        return Integer.parseInt(portNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setServerPortNumber(int portNumber) {
        this.myData.put(SERVER_PORT_NUMBER, portNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getBootstrapPortNumber() {
        String portNumber = (String) this.myData.get(BOOTSTRAP_PORT_NUMBER);
        if (portNumber == null || portNumber.trim().length() == 0) {
            return -1;
        }

        return Integer.parseInt(portNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBootstrapPortNumber(int portNumber) {
        this.myData.put(BOOTSTRAP_PORT_NUMBER, portNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeviceId() {
        return (String) this.myData.get(MetaDevice.DEVICE_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDeviceId(String deviceId) {
        this.myData.put(MetaDevice.DEVICE_ID, deviceId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername() {
        return (String) this.myData.get(USERNAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUsername(String username) {
        this.myData.put(MetaDevice.DEVICE_ID, username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return (String) this.myData.get(PASSWORD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPassword(String password) {
        this.myData.put(MetaDevice.DEVICE_ID, password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getIsRealDevice() {
        return Boolean.parseBoolean((String) this.myData.get(REAL_DEVICE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIsRealDevice(boolean isRealDevice) {
        this.myData.put(REAL_DEVICE, isRealDevice);
    }

    /**
     * Gets device's family type
     *
     * @return enum {@link FamilyType}
     */
    public FamilyType getFamilyType() {
        String familyName = getPlatformName();
        if ("iphone".equalsIgnoreCase(familyName) || "ios".equalsIgnoreCase(familyName)) {
            return FamilyType.IPHONE;
        }
        else if ("android".equalsIgnoreCase(familyName) || "selendroid".equalsIgnoreCase(familyName)) {
            return FamilyType.ANDROID;
        }
        else {
            return FamilyType.OTHER;
        }
    }
}
