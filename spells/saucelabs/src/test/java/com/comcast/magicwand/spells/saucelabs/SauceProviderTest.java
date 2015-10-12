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

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.comcast.cookie.CookieHandler;
import com.comcast.cookie.handlers.GeneralCookieHandler;
import com.comcast.csv.drivethru.api.HTTPRequestManager;
import com.comcast.csv.drivethru.model.ResponseContainer;
import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.PhoenixDriver;
import com.comcast.magicwand.exceptions.FlyingPhoenixException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;



public class SauceProviderTest {

    private class TestData {
        private SauceResponse fileUploadResponse;
        private String exceptionMsg;
        private ResponseContainer mockResponse;
        private PhoenixDriverIngredients ingridients = new PhoenixDriverIngredients();
        private DesiredCapabilities capabilities = new DesiredCapabilities();

        public TestData username(String username) {
            addToIngridients(SauceProvider.USERNAME, username);
            return this;
        }
        public TestData apiKey(String apiKey) {
            addToIngridients(SauceProvider.API_KEY, apiKey);
            return this;
        }
        public TestData url(String url) {
            addToIngridients(SauceProvider.URL, url);
            return this;
        }
        public TestData vpn(Boolean vpn) {
            addToIngridients(SauceProvider.VPN, vpn);
            return this;
        }
        public TestData cookieHandler(CookieHandler cookieHandler) {
            addToIngridients(SauceProvider.COOKIE_HANDLER, cookieHandler);
            return this;
        }
        public TestData expectedExceptionMsg(String exceptionMsg) {
            this.exceptionMsg = exceptionMsg;
            return this;
        }

//        public TestData uploadToSauce(boolean uploadtoSauce) {
//            addToIngridients(SauceProvider.UPLOAD_TO_SAUCE, uploadtoSauce);
//            return this;
//        }

        public TestData fileUploadResponse(SauceResponse expectedResponse) {
            this.fileUploadResponse = expectedResponse;
            return this;
        }

        public TestData uploadFiles(Object filesToUpload) {
            addToIngridients(SauceProvider.SAUCE_FILES_UPLOAD, filesToUpload);
            return this;
        }

        public TestData mockResponse(ResponseContainer mockResponse) {
            this.mockResponse = mockResponse;
            return this;
        }

        private void addToIngridients(String key, Object value) {
            this.ingridients.addCustomDriverConfiguration(key, value);
        }
    }


    @DataProvider(name = "builderData")
    public Iterator<Object[]> generateBuilderTestData () {
        // fields are:
        // username, api_key, url, vpn, platform, cookieHandler, expected
        List<Object[]> rv = new ArrayList<Object[]>();
        // @formatter:off
        rv.add(new Object[] {new TestData().expectedExceptionMsg("Username must be provided")});
        rv.add(new Object[] {new TestData().username("test").expectedExceptionMsg("API Key must be provided")});
        rv.add(new Object[] {new TestData().username("test").apiKey("api-key").vpn(false)});
        rv.add(new Object[] {new TestData().username("test").apiKey("api-key").vpn(false).cookieHandler(new GeneralCookieHandler())});
        rv.add(new Object[] {new TestData().username("test").apiKey("api-key").vpn(false).url("some://url.something").expectedExceptionMsg("unknown protocol: some")});
        rv.add(new Object[] {new TestData().username("test").apiKey("api-key").vpn(false).url("http://url.something.com")});
        // @formatter:on


        return rv.iterator();
    }

    @Test(dataProvider = "builderData")
    public void testBuilder(TestData testData) {
        try {
            SauceProvider spUnderTest = new SauceProvider(testData.ingridients, testData.capabilities) {
                @Override
                protected WebDriver createWebDriver(java.net.URL remoteAddress) {
                    return mock(RemoteWebDriver.class);
                }
            };
            PhoenixDriver driver = spUnderTest.buildDriver();
            assertNotNull(driver, "Driver should've been created");

            if (null != testData.exceptionMsg) {
                fail("There should've been an exception here");
            }
        }
        catch (FlyingPhoenixException e) {
            if (null != testData.exceptionMsg) {
                assertEquals(e.getCause().getMessage(), testData.exceptionMsg, "Exception messages didn't match");
            }
            else {
                fail("There shouldn't have been any exceptions");
            }
        }
    }

    @DataProvider(name = "userInfo")
    public Iterator<Object[]> genUserInfoArgs() {
        List<Object[]> rv = new ArrayList<Object[]>();

        // fields are: user, api, url, expected, exception msg
        rv.add(new Object[] { null, null, "http://ondeman.sauceconnect.com:80/wd/hub", null, "Credentials are missing. Please include them using 'sauceUser' and 'sauceApiKey' properties or as part of a URL." });
        rv.add(new Object[] { "user", null, "http://ondeman.sauceconnect.com:80/wd/hub", null, "Either set 'sauceUser' and 'sauceApiKey' properties or define userInfo in the url" });
        rv.add(new Object[] { null, "pass", "http://ondeman.sauceconnect.com:80/wd/hub", null, "Either set 'sauceUser' and 'sauceApiKey' properties or define userInfo in the url" });
        rv.add(new Object[] { "usernull", "pass", "http://ondeman.sauceconnect.com:80/wd/hub", "usernull:pass", null });
        rv.add(new Object[] { null, null, "http://user:@ondeman.sauceconnect.com:80/wd/hub", null, "Either set 'sauceUser' and 'sauceApiKey' properties or define userInfo in the url" });
        rv.add(new Object[] { null, null, "http://:pass@ondeman.sauceconnect.com:80/wd/hub", null, "Either set 'sauceUser' and 'sauceApiKey' properties or define userInfo in the url" });
        rv.add(new Object[] { null, null, "http://urluser:urlpass@ondeman.sauceconnect.com:80/wd/hub", "urluser:urlpass", null });
        rv.add(new Object[] { "user", "pass", "http://urluser:urlpass@ondeman.sauceconnect.com:80/wd/hub", null, "URL userInfo 'urluser:urlpass' cannot be specified if 'sauceUser' or 'sauceApiKey' is defined" });
        return rv.iterator();
    }

    @Test(dataProvider = "userInfo")
    public void testUserInfo(String user, String api, String url, String expected, String exceptionMsg) throws MalformedURLException {
        // @formatter:off
        PhoenixDriverIngredients pdi = new PhoenixDriverIngredients()
            .addCustomDriverConfiguration(SauceProvider.USERNAME, user)
            .addCustomDriverConfiguration(SauceProvider.API_KEY, api)
            .addCustomDriverConfiguration(SauceProvider.URL, url)
            .verify();
        // @formatter:on


        SauceProvider sp = new SauceProvider(pdi, new DesiredCapabilities());
        try {
            URL urlObj = new URL(url);
            String res = sp.getUserInfo(urlObj);
            if (null == expected) {
                fail("Should have failed, but got: " + urlObj);
            }
            assertEquals(res, expected);
        }
        catch (FlyingPhoenixException e) {
            if (null != expected) {
                fail("Should have passed with: " + expected + ". Got an exception instead: " + e);
            }
            assertEquals(e.getMessage(), exceptionMsg, "Exception messages didn't match");
        }
    }

    /**
     * Sets input data for various scenarios of fileupload
     *
     * @return TestData for fileUpload scenarios
     * @throws URISyntaxException
     */
    @DataProvider(name = "sauceUpload")
    public Iterator<Object[]> genFileUploadArgs() throws URISyntaxException {
        List<Object[]> inputParams = new ArrayList<Object[]>();
        // fields are: uploadToSauce, expectedResponse, exception msg
        // Case:1 Valid file
        List files = new ArrayList();
        Map<String, Boolean> statusMap = new HashMap<String, Boolean>();
        statusMap.put("testFileUpload.txt", true);
        URL path = getClass().getResource("/testFileUpload.txt");
        files.add(path.toURI().getPath());
        ResponseContainer mockResponse = mock(ResponseContainer.class);
        when(mockResponse.getStatusCode()).thenReturn(200);
        when(mockResponse.getResponseBody()).thenReturn(
                "{size: 58, md5: 7a9e9f1c502191311a5b2f25f5e3567e, filename: testFileUpload.txt}");
        SauceResponse sauceResponse = new SauceResponse();
        sauceResponse.setSuccess(true);
        sauceResponse.setFailureReason("");
        sauceResponse.setResponseObject(statusMap);
        inputParams.add(new Object[] {
            new TestData().uploadFiles(files).mockResponse(mockResponse).fileUploadResponse(sauceResponse)
        });

        // Case:2 Non-existing file
        files = new ArrayList<String>();
        files.add("test.txt");
        statusMap = new HashMap<String, Boolean>();
        statusMap.put("test.txt", false);
        sauceResponse = new SauceResponse();
        sauceResponse.setFailureReason("Either the given file is a directory or file does not exist test.txt");
        sauceResponse.setResponseObject(statusMap);
        inputParams.add(new Object[] {
            new TestData().uploadFiles(files).fileUploadResponse(sauceResponse)
        });

        // Case:3 Valid file; but file upload failed
        files = new ArrayList<String>();
        files.add(path.toURI().getPath());
        statusMap = new HashMap<String, Boolean>();
        statusMap.put("testFileUpload.txt", false);
        mockResponse = mock(ResponseContainer.class);
        when(mockResponse.getStatusCode()).thenReturn(500);
        when(mockResponse.getResponseBody()).thenReturn(null);
        sauceResponse = new SauceResponse();
        sauceResponse.setFailureReason("Failed uploading file testFileUpload.txt");
        sauceResponse.setResponseObject(statusMap);
        inputParams.add(new Object[] {
            new TestData().uploadFiles(files).mockResponse(mockResponse).fileUploadResponse(sauceResponse)
        });

        // Case:4 Valid file; but file upload failed with response contains param size 0
        files = new ArrayList<String>();
        files.add(path.toURI().getPath());
        statusMap = new HashMap<String, Boolean>();
        statusMap.put("testFileUpload.txt", false);
        mockResponse = mock(ResponseContainer.class);
        when(mockResponse.getStatusCode()).thenReturn(200);
        when(mockResponse.getResponseBody()).thenReturn(
                "{size: 0, md5: 7a9e9f1c502191311a5b2f25f5e3567e, filename: testFileUpload.txt}");
        sauceResponse = new SauceResponse();
        sauceResponse.setFailureReason("Failed uploading file testFileUpload.txt");
        sauceResponse.setResponseObject(statusMap);
        inputParams.add(new Object[] {
            new TestData().uploadFiles(files).mockResponse(mockResponse).fileUploadResponse(sauceResponse)
        });

        // Case:5 Driver ingredient sauceFilesUpload holds invalid data type String; instead of List
        // Verifying handling of class cast exception
        statusMap = new HashMap<String, Boolean>();
        sauceResponse = new SauceResponse();
        sauceResponse.setFailureReason("");
        sauceResponse.setResponseObject(statusMap);
        inputParams.add(new Object[] {
            new TestData().uploadFiles("test.txt").fileUploadResponse(sauceResponse)
        });

        // Case:6 Driver ingredient sauceFilesUpload contains List with data type not String
        // Verifying individual contents in List
        files = new ArrayList<Object>();
        files.add(path.toURI().getPath());
        files.add(new Object());
        statusMap = new HashMap<String, Boolean>();
        statusMap.put("testFileUpload.txt", true);
        mockResponse = mock(ResponseContainer.class);
        when(mockResponse.getStatusCode()).thenReturn(200);
        when(mockResponse.getResponseBody()).thenReturn(
                "{size: 58, md5: 7a9e9f1c502191311a5b2f25f5e3567e, filename: testFileUpload.txt}");
        sauceResponse = new SauceResponse();
        sauceResponse.setFailureReason("Invalid data type: java.lang.Object not a file name");
        sauceResponse.setResponseObject(statusMap);
        inputParams.add(new Object[] {
            new TestData().uploadFiles(files).mockResponse(mockResponse).fileUploadResponse(sauceResponse)
        });

        // Case:7 Empty file list
        files = new ArrayList<String>();
        statusMap = new HashMap<String, Boolean>();
        sauceResponse = new SauceResponse();
        sauceResponse.setFailureReason("");
        sauceResponse.setResponseObject(statusMap);
        inputParams.add(new Object[] {
            new TestData().uploadFiles(files).fileUploadResponse(sauceResponse)
        });
        return inputParams.iterator();
    }

    /**
     * Verifies different scenarios of file upload
     *
     * @param testData TestData holds data for test
     */
    @Test(dataProvider = "sauceUpload")
    public void testFileUpload(final TestData testData) {
        SauceProvider sauceProvider = new SauceProvider(testData.ingridients, testData.capabilities) {
            @Override
            protected ResponseContainer doSendRequest(HTTPRequestManager requestManager) throws IOException {
                return testData.mockResponse;
            }
        };
        SauceResponse actualResponse = sauceProvider.uploadFilesToSauceStorage();
        Assert.assertEquals(actualResponse.isSuccess(), testData.fileUploadResponse.isSuccess(), "Unexpected status");
        Assert.assertTrue(actualResponse.getFailureReason().contains(testData.fileUploadResponse.getFailureReason()));
        Map<String, Boolean> responseMap = (Map<String, Boolean>) actualResponse.getResponseObject();
        Assert.assertEquals(responseMap.size(),
                ((Map<String, Boolean>) testData.fileUploadResponse.getResponseObject()).size());
        Set<String> keys = responseMap.keySet();
        for (String key : keys) {
            Assert.assertEquals(responseMap.get(key),
                    ((Map<String, Boolean>) testData.fileUploadResponse.getResponseObject()).get(key));
        }
    }
}
