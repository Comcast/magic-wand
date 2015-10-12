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
package com.comcast.magicwand.spells.web.chrome;

import java.util.Map;

import com.comcast.magicwand.utils.SystemDetail;

class ChromePlatformSpecifics {
    private static final String OS_SUFFIX_ENV_NAME = "MAGICWAND_CHROME_SUFFIX";
    private static final String DRIVER_VERSION_ENV_NAME = "MAGICWAND_CHROME_VERSION";
    private static final String OS_EXT_ENV_NAME = "MAGICWAND_CHROME_EXTENSION";
    private static final String BASENAME = "chromedriver";

    private String artifact;
    private String version;
    private String extension;

    /**
     * Retrieves default environment values for chrome driver versions.
     *
     * Default values are based off of the OS type, as well as the latest version retrieved from the chromedriver repository.  Environment variables are also taken into considration to override the default system values.
     */
    public ChromePlatformSpecifics(String version) {
        this(version, null, null);
    }

    public ChromePlatformSpecifics(String version, String suffix) {
        this(version, suffix, null);
    }

    /**
     * Explicitly sets both the suffix and the version for the chromedriver.
     *
     * An example path is: https://chromedriver.storage.googleapis.com/2.19/chromedriver_linux64.zip
     *
     * @param version The version string that will proceed the chrome driver
     * @param suffix The suffix that will be appended to "chromedriver" for the URI
     * @param extension The string that will be added to "chromedriver" to look for the local file to execute
     */
    public ChromePlatformSpecifics(String version, String suffix, String extension) {
        Map<String, String> env = System.getenv();
        this.version = version;
        this.artifact = suffix;
        this.extension = extension;

        if (null == this.version) {
            this.version = env.get(DRIVER_VERSION_ENV_NAME);
        }
        if (null == this.version) {
            this.version = ChromePhoenixDriver.getLatestVersion();
        }
        if (null == this.artifact) {
            this.artifact = env.get(OS_SUFFIX_ENV_NAME);
        }
        if (null == this.artifact) {
            this.artifact = ChromePhoenixDriver.getCurrentOSType();
        }
        if (null == this.extension) {
            this.extension = env.get(OS_EXT_ENV_NAME);
        }
        if (null == this.extension) {
            this.extension = SystemDetail.deviceIsRunningWindows() ? ".exe" : "";
        }
    }

    /**
     * Gets only the given or generated suffix for the driver
     *
     * @return The suffix to use for the chromedriver
     */
    public String getSuffix() {
        return this.artifact;
    }

    /**
     * Retrieves the artifact string with the suggested basename
     *
     * @return Full artifact string with the basename prepended to the suffix.
     */
    public String getArtifact() {
        return BASENAME + this.artifact;
    }

    /**
     * Retrives the given or generated version string
     *
     * @return The target version for the chrome driver
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Retrieves the extension for the local executable file.
     *
     * On *nix systems, this is typically blank, although it could be anything.  On windows, this is typically .exe.
     *
     * @return The extension for the chromedriver executable.
     */
    public String getExtension() {
        return this.extension;
    }
}
