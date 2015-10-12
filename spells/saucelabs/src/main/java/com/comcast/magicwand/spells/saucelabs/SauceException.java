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

/**
 * Exception specific to Sauce Labs drivers. For more details, see {@link Exception}
 *
 * @author Dmitry Jerusalimsky
 */
public class SauceException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @see Exception#Exception()
     */
    public SauceException() {
        super();
    }

    /**
     * @see Exception#Exception(String)
     * @param message Exception message
     */
    public SauceException(String message) {
        super(message);
    }

    /**
     * @see Exception#Exception(Throwable)
     * @param cause Underlying cause for this exception
     */
    public SauceException(Throwable cause) {
        super(cause);
    }

    /**
     * @see Exception#Exception(String, Throwable)
     * @param message Exception message
     * @param cause Underlying cause for this exception
     */
    public SauceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see Exception#Exception(String, Throwable, boolean, boolean)
     * @param message Exception message
     * @param cause Underlying cause for this exception
     * @param enableSuppression Whether or not suppression is allowed
     * @param writableStackTrace whether or not the stack trace should be writable
     */
    public SauceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
