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

import com.comcast.video.dawg.common.MetaStb;

/**
 * Metadata for a mobile device. Outlines required fields that are extra to what is used by the Stb
 * @author Dmitry Jerusalimsky
 *
 */
public class MetaDevice extends MetaStb {

    public static final String ID           = "id";
    public static final String TAGS         = "tags";
    public static final String NAME         = "name";
    public static final String MAKE         = "make";
    public static final String MODEL        = "model";
    public static final String IPADDRESS    = "ipAddress";
    public static final String CAPABILITIES = "capabilities";
    public static final String DEVICE_ID    = "deviceId";
    public static final String DRIVER       = "automationDriver";

    protected Map<String, Object> myData;

    /**
     * Creates an instance of MetaDevice from the metadata of Dawg House
     * @param data underlying metadata from Dawg House
     */
    public MetaDevice(Map<String, Object> data) {
        this.myData = data;
    }

    /**
     * Gets data used for MetaMobile object
     */
    @Override
    public Map<String, Object> getData() {
        return myData;
    }
}
