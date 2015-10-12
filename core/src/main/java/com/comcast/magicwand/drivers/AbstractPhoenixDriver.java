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
package com.comcast.magicwand.drivers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.comcast.cookie.CookieHandler;

public abstract class AbstractPhoenixDriver implements PhoenixDriver {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPhoenixDriver.class);

    /**
     * @return Reference to a {@link CookieHandler}
     */
    protected abstract CookieHandler getCookieHandler();

    /**
     * Wrapper for {@link FileUtils#copyFile(File, File)}
     *
     * @param src
     * @param dst
     * @throws IOException
     */
    protected void fileUtilsCopyFile(File src, File dst) throws IOException {
        FileUtils.copyFile(src, dst);
    }

    /**
     * {@inheritDoc}
     */
    public void clearAllCookies() {
        this.getCookieHandler().clearAllCookies(this.getDriver());
    }

    /**
     * {@inheritDoc}
     */
    public boolean takeScreenshot(String filePath) {
        WebDriver driver = this.getDriver();

        if (null != driver) {
            try {
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                fileUtilsCopyFile(srcFile, new File(filePath));

                return true;
            }
            catch (IOException | WebDriverException e) {
                LOG.error("Could not take screenshot", e);

                return false;
            }
        }

        return false;
    }

    public void close() {
        WebDriver driver = this.getDriver();

        if (null != driver) {
            driver.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public WebElement findElement(By arg0) {
        WebDriver driver = this.getDriver();
        WebElement element = null;

        if (null != driver) {
            element = driver.findElement(arg0);
        }

        return element;
    }

    /**
     * {@inheritDoc}
     */
    public List<WebElement> findElements(By arg0) {
        WebDriver driver = this.getDriver();
        List<WebElement> elements = null;

        if (null != driver) {
            elements = driver.findElements(arg0);
        }

        return elements;
    }

    /**
     * {@inheritDoc}
     */
    public void get(String arg0) {
        WebDriver driver = this.getDriver();

        if (null != driver) {
            driver.get(arg0);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getCurrentUrl() {
        WebDriver driver = this.getDriver();
        String currentUrl = null;

        if (null != driver) {
            currentUrl = driver.getCurrentUrl();
        }

        return currentUrl;
    }

    /**
     * {@inheritDoc}
     */
    public String getPageSource() {
        WebDriver driver = this.getDriver();
        String pageSource = null;

        if (null != driver) {
            pageSource = driver.getPageSource();
        }

        return pageSource;
    }

    /**
     * {@inheritDoc}
     */
    public String getTitle() {
        WebDriver driver = this.getDriver();
        String title = null;

        if (null != driver) {
            title = driver.getTitle();
        }

        return title;
    }

    /**
     * {@inheritDoc}
     */
    public String getWindowHandle() {
        WebDriver driver = this.getDriver();
        String windowHandle = null;

        if (null != driver) {
            windowHandle = driver.getWindowHandle();
        }

        return windowHandle;
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> getWindowHandles() {
        WebDriver driver = this.getDriver();
        Set<String> windowHandles = null;

        if (null != driver) {
            windowHandles = driver.getWindowHandles();
        }

        return windowHandles;
    }

    /**
     * {@inheritDoc}
     */
    public Options manage() {
        WebDriver driver = this.getDriver();
        Options manage = null;

        if (null != driver) {
            manage = driver.manage();
        }

        return manage;
    }

    /**
     * {@inheritDoc}
     */
    public Navigation navigate() {
        WebDriver driver = this.getDriver();
        Navigation navigate = null;

        if (null != driver) {
            navigate = driver.navigate();
        }

        return navigate;
    }

    /**
     * {@inheritDoc}
     */
    public void quit() {
        WebDriver driver = this.getDriver();
        if (null != driver) {
            driver.quit();
        }
    }

    /**
     * {@inheritDoc}
     */
    public TargetLocator switchTo() {
        WebDriver driver = this.getDriver();
        TargetLocator switchTo = null;

        if (null != driver) {
            switchTo = driver.switchTo();
        }

        return switchTo;
    }
}
