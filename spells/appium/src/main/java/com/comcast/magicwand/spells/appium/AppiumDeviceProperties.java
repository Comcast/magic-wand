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
package com.comcast.magicwand.spells.appium;

/**
 * Properties to be defined by the <em>Appium</em> Device.
 *
 * @author Dmitry Jerusalimsky
 *
 */
public interface AppiumDeviceProperties {

    /**
     * Get the platform version.
     *
     * @return platform version
     */
    String getPlatformVersion();

    /**
     * Sets the platform version
     *
     * @param platformVersion of the device to be set.
     */
    void setPlatformVersion(String platformVersion);

    /**
     * Gets the platform name.
     *
     * @return platform name
     */
    String getPlatformName();

    /**
     * Sets the platform name.
     *
     * @param platformName of the device.
     */
    void setPlatformName(String platformName);

    /**
     * Gets the server port number.
     *
     * @return port number
     */
    int getServerPortNumber();

    /**
     * Sets server port number.
     *
     * @param portNumber of the server
     */
    void setServerPortNumber(int portNumber);

    /**
     * Gets the server port number.
     *
     * @return port number
     */
    int getBootstrapPortNumber();

    /**
     * Sets server port number.
     *
     * @param portNumber of the server
     */
    void setBootstrapPortNumber(int portNumber);

    /**
     * Gets device ID.
     *
     * @return id of the real device to use
     */
    String getDeviceId();

    /**
     * Sets device id.
     *
     * @param deviceId of the device
     */
    void setDeviceId(String deviceId);

    /**
     * Gets device type (real or simulator).
     *
     * @return whether device is real or simulated
     */
    boolean getIsRealDevice();

    /**
     * Sets whether device is real or simulated
     *
     * @param realDevice determines whether testing against real device or simulator
     */
    void setIsRealDevice(boolean realDevice);

    /**
     * Sets username associated with current device
     *
     * @param username Username associated with current device
     */
    void setUsername(String username);

    /**
     * Gets username associated with current device
     *
     * @return username
     */
    String getUsername();

    /**
     * Sets password associated with current device
     *
     * @param password Password associated with current device
     */
    void setPassword(String password);

    /**
     * Gets password associated with current device
     *
     * @return password
     */
    String getPassword();
}
