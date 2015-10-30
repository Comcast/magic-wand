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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.comcast.drivethru.api.HTTPRequestManager;
import com.comcast.drivethru.api.HTTPRequestManager.Builder;
import com.comcast.drivethru.api.HTTPRequestManager.METHOD;
import com.comcast.drivethru.constants.ServerStatusCodes;
import com.comcast.drivethru.model.ResponseContainer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to deal with the Appium Server
 *
 * @author Dmitry Jerusalimsky
 *
 */
public class AppiumServerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppiumServerController.class);
    private static final String APPIUM_STATUS_ENDPOINT = "/wd/hub/status";
    private static final String APPIUM_SESSIONS_ENDPOINT = "/wd/hub/sessions";

    private static final String MALFORMED_URL_MSG = "Badly constructed URL: ";
    private Builder builder;

    protected AppiumServerController(Builder httpManagerBuilder) {
        this.builder  = httpManagerBuilder;
    }

    /**
     * Creates an instance of the Appium Server Controller
     */
    public AppiumServerController() {
        this.builder = new HTTPRequestManager.Builder();
    }

    /**
     * Checks server state
     * @param url URL of appium server
     * @return True if server is running; False otherwise
     */
    public boolean checkServerState(URL url) {
        URL appiumStatusUrl = null;
        try {
            appiumStatusUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), APPIUM_STATUS_ENDPOINT);
            return null != callAppiumEndpoint(appiumStatusUrl);
        }
        catch (MalformedURLException e) {
            LOGGER.error(MALFORMED_URL_MSG + appiumStatusUrl, e);
            return false;
        }
    }

    /**
     * Calls an endpoint on an Appium server
     * @param appiumServerEndpoint URL of an endpoint to call
     * @return JSON response or null
     */
    private JsonObject callAppiumEndpoint(URL appiumServerEndpoint) {
        try {
            HTTPRequestManager helper = this.builder
                .url(appiumServerEndpoint.toString())
                .method(METHOD.GET)
                .build();

            ResponseContainer response = helper.sendRequest();

            int responseCode = response.getStatusCode();
            if (ServerStatusCodes.OK != responseCode) {
                if (ServerStatusCodes.NOT_FOUND == responseCode) {
                    LOGGER.error("Server is not running.");
                }
                else {
                    LOGGER.error("Unexpected response code from server... Expected: 200, got: '" +
                            responseCode + "'\nResponse: " + response.getResponseBody());
                }

                return null;
            }

            Gson gson = new Gson();
            return gson.fromJson(response.getResponseBody(), JsonObject.class);
        }
        catch (IOException e) {
            LOGGER.error("Could not communicate with Appium server. URL: '" + appiumServerEndpoint + "'", e);
            return null;
        }
    }

    /**
     * Query the state of the Appium Server by hitting the pre-defined url from Appium
     *
     * @param ipAddress IP Address or Hostname of Appium server to check
     * @param serverPort Port number of the Appium server
     * @return True if server is running and reachable; False otherwise
     */
    public boolean checkServerState(String ipAddress, int serverPort) {
        URL appiumStatusUrl = null;
        try {
            appiumStatusUrl = new URL("http", ipAddress, serverPort, APPIUM_STATUS_ENDPOINT);
            return null != callAppiumEndpoint(appiumStatusUrl);
        }
        catch (MalformedURLException e) {
            LOGGER.error(MALFORMED_URL_MSG + appiumStatusUrl, e);
            return false;
        }
    }

    /**
     * Checks whether appium server has any running sessions
     * @param url URL of a the server
     * @return True if there are active sessions; False otherwise
     */
    @SuppressWarnings("squid:S1155") // ignore 'Use isEmpty() to check whether the collection is empty or not.' error
    public boolean hasRunningSessions(URL url) {
        URL appiumStatusUrl = null;
        try {
            appiumStatusUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), APPIUM_SESSIONS_ENDPOINT);
            JsonObject response = callAppiumEndpoint(appiumStatusUrl);
            JsonArray sessions = response.getAsJsonArray("value");

            return sessions.size() > 0;
        }
        catch (MalformedURLException e) {
            LOGGER.error(MALFORMED_URL_MSG + appiumStatusUrl, e);
            return false;
        }
    }
}
