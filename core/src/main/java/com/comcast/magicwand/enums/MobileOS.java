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
package com.comcast.magicwand.enums;

/**
 * Defines OS type for mobile devices
 *
 * @author Dmitry Jerusalimsky
 *
 */
public class MobileOS {

    private OSType type;
    private String version;

    /**
     * Creates an instance of Mobile OS with a specified OS type
     * @param type OS Type
     */
    public MobileOS(OSType type) {
        this(type, null);
    }

    /**
     * Creates an instance of Mobile OS with a specified OS type and OS version
     * @param type OS type
     * @param version os version
     */
    public MobileOS(OSType type, String version) {
        this.type = type;
        this.version = version;
    }

    /**
     * Gets OS Type of current mobile os
     * @return os type
     */
    public OSType getType() {
        return this.type;
    }

    /**
     * Gets OS version of current mobile os
     * @return version
     */
    public String getVersion() {
        return this.version;
    }
}
