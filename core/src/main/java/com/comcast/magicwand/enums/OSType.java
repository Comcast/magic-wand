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
 * An enum that describes OS name
 *
 * @author Dmitry Jerusalimsky
 *
 */
public enum OSType {
    // @formatter:off
    MAC     ( "mac"),
    LINUX   ( "linux"),
    UNIX    ( "unix"),
    WINDOWS ( "windows"),
    ANDROID ( "android"),
    IPHONE  ( "iphone"),
    IPAD    ( "ipad");
    // @formatter:on

    private final String name;

    private OSType(String name) {
        this.name = name;
    }

    /**
     * Gets the name of current enum value
     * @return Name of the enum value
     */
    public String getName() {
        return this.name;
    }
}
