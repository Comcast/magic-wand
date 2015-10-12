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
 * Holds response for sauce related operations
 *
 * @author Radhika
 *
 */
public class SauceResponse {

    private boolean success;
    private String failureReason;
    private Object responseObject;

    /**
     * Default constructor for SauceResponse
     */
    public SauceResponse() {
        this.success = false;
    }

    /**
     * Returns success status
     *
     * @return true if success; otherwise false
     */
    public boolean isSuccess() {
        return this.success;
    }

    /**
     * Sets the status of operation
     *
     * @param status success status
     */
    public void setSuccess(boolean status) {
        this.success = status;
    }

    /**
     * Gets the failure reason
     *
     * @return Failure reason
     */
    public String getFailureReason() {
        return this.failureReason;
    }

    /**
     * Sets the failure reason
     *
     * @param failReason Failure reason
     */
    public void setFailureReason(String failReason) {
        this.failureReason = failReason;
    }

    /**
     * Gets the response object as part of the operation
     *
     * @return Gets the response object
     */
    public Object getResponseObject() {
        return responseObject;
    }

    /**
     * Sets the response object. It can hold more details about response
     *
     * @param responseObject Object holding more details on response
     */
    public void setResponseObject(Object responseObject) {
        this.responseObject = responseObject;
    }
}
