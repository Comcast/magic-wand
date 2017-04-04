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
package com.comcast.magicwand.spells.saucelabs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comcast.cookie.CookieHandler;
import com.comcast.cookie.handlers.GeneralCookieHandler;
import com.comcast.drivethru.api.HTTPRequestManager;
import com.comcast.drivethru.api.HTTPRequestManager.Builder;
import com.comcast.drivethru.api.HTTPRequestManager.METHOD;
import com.comcast.drivethru.constants.ServerStatusCodes;
import com.comcast.drivethru.model.ResponseContainer;
import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.PhoenixDriver;
import com.comcast.magicwand.enums.DesktopOS;
import com.comcast.magicwand.exceptions.FlyingPhoenixException;
import com.saucelabs.ci.sauceconnect.SauceConnectFourManager;
import com.saucelabs.ci.sauceconnect.SauceTunnelManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.android.AndroidDriver;

/**
 * Provides a {@link RemoteWebDriver} for SauceLabs
 * @author Dmitry Jerusalimsky
 *
 */
public class SauceProvider {

    private static final String DEFAULT_URL               = "http://ondemand.saucelabs.com:80/wd/hub";
    private static final String DEFAULT_SAUCE_STORAGE_URL = "https://saucelabs.com/rest/v1/storage";

    private static final int ZERO_FILE_UPLOAD_SIZE = 0;

    private static final Logger LOG = LoggerFactory.getLogger(SauceProvider.class);

    public static final String USERNAME           = "sauceUser";
    public static final String API_KEY            = "sauceApiKey";
    public static final String URL                = "sauceUrl";
    public static final String VPN                = "sauceVPN";
    public static final String VPN_PORT           = "sauceVPNPort";
    public static final String VPN_QUIET_MODE     = "sauceVPNQuiteMode";
    public static final String VPN_OPTIONS        = "sauceVPNOptions";
    public static final String VPN_VERSION        = "sauceVPNVersion";
    public static final String COOKIE_HANDLER     = "cookieHandler";
    public static final String SAUCE_STOTAGE_URL  = "sauceStorageUrl";
    public static final String SAUCE_FILES_UPLOAD = "sauceFilesUpload";

    private String username;
    private String apiKey;
    private String urlStr;
    private String sauceStorageUrlStr;
    private List filesToUpload;

    private DesiredCapabilities customCapabilities;

    private CookieHandler cookieHandler;

    // this will be used when we'll get sauce connect incorporated
    private boolean useVpn = false;

    private DesktopOS desktopOS;

    private Map<String, Object> driverCustomConfig;

    /**
     * Creates an instance of a SauceProvider class
     * @param ingridients Set of {@link PhoenixDriverIngredients} to use for driver creation
     * @param capabilities Set of {@link DesiredCapabilities} to use for driver creation
     *
     */
    public SauceProvider(PhoenixDriverIngredients ingridients, DesiredCapabilities capabilities) {
        // extract data
        this.driverCustomConfig = ingridients.getDriverConfigs();
        this.apiKey = (String) driverCustomConfig.get(API_KEY);
        this.cookieHandler = (CookieHandler) driverCustomConfig.get(COOKIE_HANDLER);
        String url = (String) driverCustomConfig.get(URL);

        urlStr = (null == url) ? DEFAULT_URL : url;

        // url for sauce storage location
        String sauceStorageUrl = (String) driverCustomConfig.get(SAUCE_STOTAGE_URL);
        sauceStorageUrlStr = (null == sauceStorageUrl) ? DEFAULT_SAUCE_STORAGE_URL : sauceStorageUrl;

        // Initializing to empty list
        filesToUpload = new ArrayList<String>();
        Object filesObject = this.driverCustomConfig.get(SAUCE_FILES_UPLOAD);
        // Adding a null check so that SAUCE_FILES_UPLOAD will not be a mandatory field
        if (null != filesObject) {
            if (filesObject instanceof List<?>) {
                filesToUpload = (List) filesObject;
            }
            else {
                LOG.error(
                        "Error: Invalid data type for driver ingredient {}; continuing with empty file list. Expected data type: List holding file path",
                        SAUCE_FILES_UPLOAD);
            }
        }

        this.username = (String) driverCustomConfig.get(USERNAME);

        Boolean vpn = (Boolean) driverCustomConfig.get(VPN);
        this.useVpn = (null == vpn) ? false : vpn.booleanValue();

        this.desktopOS = ingridients.getDesktopOS();

        this.customCapabilities = capabilities;
    }

    /**
     * Null safe check to see if the string starts or ends with the substring
     *
     * @param subject Subject string to check
     * @param substring Substring to search for at the beginning or end of the subject
     * @return true the substring is the beginning or end of the subject, false otherwise
     *         false if the subject is null
     */
    private boolean nullSafeStartsEnds(String subject, String substring) {
        if (    (null == subject)
             || (null == substring)) {
            return false;
        }

        return (subject.startsWith(substring) || subject.endsWith(substring));
    }

    /**
     * Gets userInfo string from either the URL or system properties
     * @param url URL that might contain userInfo
     * @return String containing userInfo
     * @throws FlyingPhoenixException If one of the following is true:
     * <ul>
     * <li>Credentials were NOT specified via system properties or URL</li>
     * <li>Credentials were specified by BOTH system properties and URL</li>
     * <li>Part of the credentials is missing; No username or no API key was found</li>
     * </ul>
     *
     */
    protected String getUserInfo(URL url) throws FlyingPhoenixException {
        String urlUserInfo = url.getUserInfo();
        String credsUserInfo = null;

        if (    (null != this.username)
             || (null != this.apiKey) ) {
            credsUserInfo = String.format("%s:%s",
                    (null==this.username?"":this.username),
                    (null == this.apiKey ? "" : this.apiKey));
        }

        // make sure that only 1 set of creds is present
        if ((null != urlUserInfo) && (null != credsUserInfo)) {
            String msg = String.format("URL userInfo '%s' cannot be specified if '%s' or '%s' is defined",
                    urlUserInfo, USERNAME, API_KEY);
            throw new FlyingPhoenixException(msg);
        }
        // if both sources of credentials are missing, throw an exception
        else if ((null == urlUserInfo) && (null == credsUserInfo)) {
            String msg = String.format("Credentials are missing. Please include them using '%s' and '%s' properties or as part of a URL.", USERNAME, API_KEY);
            throw new FlyingPhoenixException(msg);
        }
        // make sure that both username and api key are present
        else if (nullSafeStartsEnds(credsUserInfo, ":") || nullSafeStartsEnds(urlUserInfo, ":")) {
            String msg = String.format("Either set '%s' and '%s' properties or define userInfo in the url",
                    USERNAME, API_KEY);
            throw new FlyingPhoenixException(msg);
        }

        return (null == urlUserInfo) ? credsUserInfo : urlUserInfo;
    }

    /**
     * Generates URL to use in order to connect to SauceLabs
     * @return URL for SauceLabs
     * @throws FlyingPhoenixException If URL is malformed or invalid
     */
    protected URL generateSauceURL() throws FlyingPhoenixException {
        try {
            URL baseUrl = new URL(urlStr);
            String userInfo = getUserInfo(baseUrl);

            String auth = String.format("http://%s@%s:%d%s", userInfo,
                    baseUrl.getHost(), baseUrl.getPort(),
                    baseUrl.getFile());
            URL remoteAddress = new URL(auth);
            LOG.debug("Remote URL is: " + remoteAddress.toURI());
            return remoteAddress;
        }
        catch (MalformedURLException | URISyntaxException e) {
            String msg = "There was an error while creating sauce url " + e.getMessage();
            LOG.error(msg);
            throw new FlyingPhoenixException(msg, e);
        }
    }

    /**
     * Creates one of:
     * <ul>
     * <li>{@link RemoteWebDriver}</li>
     * <li>{@link AndroidDriver}</li>
     * <li>{@link IOSDriver}</li>
     * </ul>
     * driver that will be communicating with SauceLabs
     * @param remoteAddress URL of the location to connect to
     * @return Instance of a driver
     * @throws FlyingPhoenixException
     */
    protected WebDriver createWebDriver(URL remoteAddress) throws FlyingPhoenixException {
        WebDriver retVal = null;

        boolean isAppium = (null != this.customCapabilities.getCapability("appiumVersion"));
        String platform = (String) this.customCapabilities.getCapability("platformName");
        boolean isAndroid = ("android".equalsIgnoreCase(platform));

        // upload files
        if (!filesToUpload.isEmpty()) {
            SauceResponse sauceResponse = uploadFilesToSauceStorage();
            LOG.info("FileName:UploadSuccess {}", sauceResponse.getResponseObject());
            if (!sauceResponse.isSuccess()) {
                LOG.error(sauceResponse.getFailureReason());
                throw new FlyingPhoenixException("Uploading file/s to sauce storage failed");
            }
        }

        if (isAppium && isAndroid) {
            retVal = new AndroidDriver(remoteAddress, this.customCapabilities);
        }
        else if (isAppium && (null != platform)) {
            retVal = new IOSDriver(remoteAddress, this.customCapabilities);
        }
        else {
            retVal = new RemoteWebDriver(remoteAddress, this.customCapabilities);
        }

        return retVal;
    }

    /**
     * Creates an instance of a {@link PhoenixDriver} that is representing {@link SaucePhoenixDriver}
     * @return Instance of a {@link SaucePhoenixDriver}
     * @throws FlyingPhoenixException If there was an error creating a driver
     */
    public PhoenixDriver buildDriver() throws FlyingPhoenixException {
        if (null == this.username) {
            throw new FlyingPhoenixException("Errors while validating builder parameters"
                    , new SauceException("Username must be provided"));
        }

        if (null == this.apiKey) {
            throw new FlyingPhoenixException("Errors while validating builder parameters"
                    , new SauceException("API Key must be provided"));
        }

        this.customCapabilities.setCapability("platform", this.desktopOS.toString());

        if (null == this.cookieHandler) {
            this.cookieHandler = new GeneralCookieHandler();
        }

        URL remoteAddress = generateSauceURL();

        try {
            SauceTunnelManager vpnManager = initSauceConnect();

            WebDriver driver = createWebDriver(remoteAddress);
            SaucePhoenixDriver spd = new SaucePhoenixDriver(driver, cookieHandler, vpnManager, this.driverCustomConfig);
            LOG.debug("Driver is: " + spd);
            return spd;
        }
        catch (UnreachableBrowserException e) {
            LOG.error("Unreachable Browser Exception was caught. " + e.getMessage());
            throw new FlyingPhoenixException("Errors while validating builder parameters"
                    , new SauceException("There was an error creating a sauce driver", e));
        }
    }

    /**
     * Sets up a tunnel via SauceConnect
     * @return
     * @throws FlyingPhoenixException
     */
    private SauceTunnelManager initSauceConnect() throws FlyingPhoenixException {
        if (false == this.useVpn) {
            LOG.warn("Driver is not configured to establish a VPN connection. "
                    + "Please make sure that '{}' property is set to 'true'", SauceProvider.VPN);
            return null;
        }

        Integer port = (Integer) this.driverCustomConfig.get(SauceProvider.VPN_PORT);
        Boolean qm = (Boolean) this.driverCustomConfig.get(SauceProvider.VPN_QUIET_MODE);
        Integer vv = (Integer) this.driverCustomConfig.get(SauceProvider.VPN_VERSION);

        int vpnVersion;
        if (null == vv) {
            vpnVersion = 4;
            LOG.warn("Sauce Connect version was not specified... Defaulting to '{}'", Integer.toString(vpnVersion));
        }
        else {
            vpnVersion = vv.intValue();
        }

        String vpnOptions = (String) this.driverCustomConfig.get(SauceProvider.VPN_OPTIONS);
        int vpnPort;
        if (null == port) {
            vpnPort = 4445;
            LOG.warn("Sauce Connect port was not specified... Defaulting to '{}'", Integer.toString(vpnPort));
        }
        else {
            vpnPort = port.intValue();
        }

        boolean vpnQm;
        if (null == qm) {
            vpnQm = true;
            LOG.warn("Sauce Connect Quiet Mode was not specified... Defaulting to '{}'", Boolean.toString(vpnQm));
        }
        else {
            vpnQm = qm.booleanValue();
        }

        LOG.debug(
                "Establishing a VPN connection using the following arguments: { port = {}; options = {}; quietMode = {} }",
                vpnPort, vpnOptions, !vpnQm);

        if (4 != vpnVersion) {
            String msg = String.format("Unsupported Sauce Connect version '%d'. Valid version is '4'", vpnVersion);
            LOG.error(msg);
            throw new FlyingPhoenixException(msg);
        }

        SauceTunnelManager vpnManager = new SauceConnectFourManager(vpnQm);

        try {
            vpnManager.openConnection(username, apiKey, vpnPort, null, vpnOptions, null, !useVpn, null);
        }
        catch (IOException e) {
            vpnManager = null;
            String msg = "There was an error while establishing a VPN connection to SauceLabs";
            LOG.error(msg, e);
            throw new FlyingPhoenixException(msg, e);
        }

        return vpnManager;
    }

    /**
     * Upload files to sauce storage and return response of file uploads as SauceResponse
     *
     * @return SauceResponse holding response of file uploads
     */
    public SauceResponse uploadFilesToSauceStorage() {
        SauceResponse sauceResponse = new SauceResponse();
        StringBuilder failureReason = new StringBuilder();
        Map<String, Boolean> fileUploadStatus = new HashMap<String, Boolean>();
        for (Object filePath : this.filesToUpload) {
            if (filePath instanceof String) {
                File file = new File((String) filePath);
                if (file.isFile() && file.exists()) {
                    // Reset upload status to false each time before upload
                    boolean uploadStatus = false;
                    try {
                        String authString = username + ":" + apiKey;
                        /* Encoding authentication string */
                        String authStringEnc = new Base64().encodeAsString(new String(authString).getBytes());
                        LOG.debug("Base64 encoded auth string: " + authStringEnc);

                        // Setting request headers
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Accept", "application/octet-stream");
                        headers.put("Authorization", "Basic " + authStringEnc);
                        headers.put("Content-type", "application/json");

                        // Converts file content to byte data
                        byte[] data = IOUtils.toByteArray(new FileInputStream(file));

                        // Uploading file to sauce storage by sending multi-part encoded HTTP POST request to SauceLabs
                        Builder builder = new HTTPRequestManager.Builder();
                        HTTPRequestManager helper = builder
                                .url(this.sauceStorageUrlStr + "/" + username + "/" + file.getName()
                                        + "?overwrite=true").method(METHOD.POST).contentType("multipart/form-data")
                                .headers(headers).data(data).build();
                        LOG.info("Uploading file {} to sauce storage", file.getName());

                        ResponseContainer response = doSendRequest(helper);
                        LOG.info("Response - {}", response.getResponseBody());
                        if (ServerStatusCodes.OK == response.getStatusCode()) {
                            String responseBody = response.getResponseBody();
                            if (null != responseBody) {
                                JSONObject jsonResponse = new JSONObject(responseBody);
                                int size = (Integer) jsonResponse.get("size");
                                if (ZERO_FILE_UPLOAD_SIZE < size) {
                                    uploadStatus = true;
                                }
                            }
                        }
                    }
                    catch (JSONException | IOException e) {
                        failureReason.append(String.format("Exception while uploading file %s to sauce storage",
                                file.getName()));
                        LOG.error("Exception while uploading file {} to sauce storage", file.getName());
                    }

                    fileUploadStatus.put(file.getName(), uploadStatus);
                    if (!uploadStatus) {
                        failureReason.append(String.format("Failed uploading file %s", file.getName()));
                    }

                }
                else {
                    failureReason.append(String.format(
                            "Either the given file is a directory or file does not exist %s \n", file.getName()));
                    LOG.error("Either the given file is a directory or file does not exist {}", file.getName());
                    fileUploadStatus.put(file.getName(), false);
                }
            }
            else {
                failureReason.append(String.format("Invalid data type: %s not a file name \n", filePath.getClass()
                        .getName()));
                LOG.error("Invalid data type: {} not a file name", filePath.getClass().getName());
            }
        }
        if (this.filesToUpload.size() > 0 && failureReason.length() <= 0) {
            sauceResponse.setSuccess(true);
        }
        sauceResponse.setResponseObject(fileUploadStatus);
        sauceResponse.setFailureReason(failureReason.toString());

        return sauceResponse;
    }

    /**
     * Sends HTTP request. Also, to support unit testing, http request-response handling refactored to separate API
     *
     * @param requestManager
     * @return HTTP Response container
     * @throws IOException
     */
    protected ResponseContainer doSendRequest(HTTPRequestManager requestManager) throws IOException {
        ResponseContainer response = requestManager.sendRequest();
        return response;
    }
}
