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

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.PhoenixDriver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * Integration test for SauceProvider
 *
 * @author Radhika
 */

public class SauceProviderIT {

    private static final Logger LOG = LoggerFactory.getLogger(SauceProviderIT.class);

    PhoenixDriver driver;

    /**
     * Verifies if file upload success
     *
     *
     * @throws URISyntaxException
     */
    @Test
    public void testSauceAppiumWithFileUpload() throws URISyntaxException {
        PhoenixDriverIngredients pid = buildSauceDrvierIngredients();
        List<String> files = new ArrayList<String>();
        URL path = getClass().getResource("/testFileUpload.txt");
        files.add(path.toURI().getPath());
        pid.addCustomDriverConfiguration(SauceProvider.SAUCE_FILES_UPLOAD, files);

        SauceProvider sauceProvider = new SauceProvider(pid, pid.getDriverCapabilities());
        SauceResponse sauceResponse = sauceProvider.uploadFilesToSauceStorage();
        Assert.assertTrue(sauceResponse.isSuccess(), "File upload should be success");
        Assert.assertEquals(sauceResponse.getFailureReason().length(), 0, "Failure reason should be empty");
        Map<String, Boolean> responseObj = (Map<String, Boolean>) sauceResponse.getResponseObject();
        Assert.assertTrue(responseObj.get("testFileUpload.txt"), "upload should be success");
    }

    /**
     * Verifies if file upload response
     *
     *
     * @throws URISyntaxException
     */
    @Test
    public void testSauceAppiumWithMultipleFileUpload() throws URISyntaxException {
        PhoenixDriverIngredients pid = buildSauceDrvierIngredients();
        List<String> files = new ArrayList<String>();
        URL path = getClass().getResource("/testFileUpload.txt");
        files.add(path.toURI().getPath());
        files.add("test.txt");
        pid.addCustomDriverConfiguration(SauceProvider.SAUCE_FILES_UPLOAD, files);

        SauceProvider sauceProvider = new SauceProvider(pid, pid.getDriverCapabilities());
        SauceResponse sauceResponse = sauceProvider.uploadFilesToSauceStorage();
        Assert.assertFalse(sauceResponse.isSuccess(), "File upload should be success");
        Assert.assertTrue(
                sauceResponse.getFailureReason().contains(
                        "Either the given file is a directory or file does not exist test.txt"),
                "Failure reason should not be empty");
        Map<String, Boolean> responseObj = (Map<String, Boolean>) sauceResponse.getResponseObject();
        Assert.assertTrue(responseObj.get("testFileUpload.txt"), "upload should be success");
        Assert.assertFalse(responseObj.get("test.txt"), "upload should be failure");
    }

    /**
     * Verifies if file upload return false when no files are given for upload
     *
     * @throws URISyntaxException
     *
     */
    @Test
    public void testSauceAppiumWithNoFileUpload() throws URISyntaxException {
        PhoenixDriverIngredients pid = buildSauceDrvierIngredients();
        SauceProvider sauceProvider = new SauceProvider(pid, pid.getDriverCapabilities());
        SauceResponse sauceResponse = sauceProvider.uploadFilesToSauceStorage();
        Assert.assertFalse(sauceResponse.isSuccess(), "File upload should not be success");
        Assert.assertEquals(sauceResponse.getFailureReason().length(), 0, "Failure reason should be empty");

    }

    /**
     * Verifies if file upload fails for non-existing file
     *
     */
    @Test
    public void testSauceAppiumWithInvalidFileUpload() {
        PhoenixDriverIngredients pid = buildSauceDrvierIngredients();
        // non-existing file
        List<String> files = new ArrayList<String>();
        files.add("test.txt");
        pid.addCustomDriverConfiguration(SauceProvider.SAUCE_FILES_UPLOAD, files);
        SauceProvider sauceProvider = new SauceProvider(pid, pid.getDriverCapabilities());
        SauceResponse sauceResponse = sauceProvider.uploadFilesToSauceStorage();
        Assert.assertFalse(sauceResponse.isSuccess(), "File upload should not be success");
        Assert.assertTrue(
                sauceResponse.getFailureReason().contains(
                        "Either the given file is a directory or file does not exist test.txt"),
                "Failure reason should not be empty");
    }

    /**
     * Verifies if file upload fails for non String data type in List
     */
    @Test
    public void testSauceAppiumInvalidData() {
        PhoenixDriverIngredients pid = buildSauceDrvierIngredients();
        // invalid data
        List files = new ArrayList();
        files.add(new Object());
        pid.addCustomDriverConfiguration(SauceProvider.SAUCE_FILES_UPLOAD, files);
        SauceProvider sauceProvider = new SauceProvider(pid, pid.getDriverCapabilities());
        SauceResponse sauceResponse = sauceProvider.uploadFilesToSauceStorage();
        Assert.assertFalse(sauceResponse.isSuccess(), "File upload should not be success");
        Assert.assertTrue(
                sauceResponse.getFailureReason().contains("Invalid data type: java.lang.Object not a file name"),
                "Failure reason not matching");
    }

    /**
     * Verifies if file uploads returns true and uploads no files when SAUCE_FILES_UPLOAD holds non List data
     */
    @Test
    public void testSauceAppiumInvalidDataType() {
        PhoenixDriverIngredients pid = buildSauceDrvierIngredients();
        // string input
        pid.addCustomDriverConfiguration(SauceProvider.SAUCE_FILES_UPLOAD, "test.txt");
        SauceProvider sauceProvider = new SauceProvider(pid, pid.getDriverCapabilities());
        SauceResponse sauceResponse = sauceProvider.uploadFilesToSauceStorage();
        Assert.assertFalse(sauceResponse.isSuccess(), "File upload should not be success");
        Assert.assertEquals(sauceResponse.getFailureReason().length(), 0, "Failure reason should not be empty");
    }

    /**
     * Creates driver ingrdeients
     *
     * @return PhoenixDriverIngredients
     */
    private PhoenixDriverIngredients buildSauceDrvierIngredients() {
        String sauceUser = System.getProperty("sauce.user");
        String sauceKey = System.getProperty("sauce.key");
        String sauceUrl = System.getProperty("sauce.url");

        LOG.debug("Using the following config values: { username={}; key={}; url={} }", sauceUrl, sauceKey, sauceUrl);

        assertNotNull(sauceUser, "Sauce username has to be specified");
        assertNotNull(sauceKey, "Sauce API key has to be specified");
        assertNotNull(sauceUrl, "Sauce URL has to be specified");

        // @formatter:off
        PhoenixDriverIngredients pid = new PhoenixDriverIngredients()
            .addCustomDriverConfiguration(SauceProvider.USERNAME, sauceUser)
            .addCustomDriverConfiguration(SauceProvider.API_KEY, sauceKey)
            .addCustomDriverConfiguration(SauceProvider.URL, sauceUrl);
        // @formatter:on

        return pid;
    }
}
