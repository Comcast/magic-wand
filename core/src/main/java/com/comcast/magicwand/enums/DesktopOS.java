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
 * A class that defines a Desktop OS type and version
 *
 * @author Dmitry Jerusalimsky
 */
public class DesktopOS {
    private OSType type;
    private String version;

    /**
     * Creates an instance of the DesktopOS with just OS type
     * @param type Type of the OS this object is representing
     */
    public DesktopOS(OSType type) {
        this(type, null);
    }

    /**
     * Creates an instance of the DesktopOS with OS type and OS version
     * @param type Type of the OS this object is representing
     * @param version Version of the desired OS
     */
    public DesktopOS(OSType type, String version) {
        this.type = type;
        this.version = version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String rv = type.name();

        if (null != version) {
            rv += " " + version;
        }

        return rv;
    }
    /**
     * Gets OS Type
     * @return OSType of current desktop OS
     */
    public OSType getType() {
        return this.type;
    }

    /**
     * Gets OS version of current system
     * @return version
     */
    public String getVersion() {
        return this.version;
    }
}
