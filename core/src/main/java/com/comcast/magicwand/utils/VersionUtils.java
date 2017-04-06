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
package com.comcast.magicwand.utils;

/**
 * Provides common utilities for handling version comparision.
 *
 * @author Andrew Benton
 */
public class VersionUtils {

    /**
     * Compare two versions provided in string form, attempting to first using an integer comparison, and then using a string comparison.
     *
     * Any null string will be treated as having a lower value than a non-null string, even if the non-null string is empty.
     *
     * @param lhs The left hand side of the comparator
     * @param rhs The right hand side of the comparator
     *
     * @return -1, 0, or 1 based on the {@link Comparable} interface
     */
    public static int compare(String lhs, String rhs) {
        if(lhs == rhs)
            return  0;
        if(null == lhs)
            return -1;
        if(null == rhs)
            return  1;

        String[] lhsTokens = lhs.split("\\.");
        String[] rhsTokens = rhs.split("\\.");

        for(int i = 0; i < lhsTokens.length && i < rhsTokens.length; i++) {
            try {
                int lhsi = Integer.parseInt(lhsTokens[i]);
                int rhsi = Integer.parseInt(rhsTokens[i]);

                if(lhsi < rhsi)
                    return -1;
                if(lhsi > rhsi)
                    return  1;
            }
            catch(NumberFormatException nfe) {
                int res = lhsTokens[i].compareTo(rhsTokens[i]);
                //normalize for odd string-compare results
                if(0 != res)
                    return res / Math.abs(res);
            }
        }

        if(lhsTokens.length < rhsTokens.length)
            return -1;
        if(lhsTokens.length > rhsTokens.length)
            return  1;

        return 0;
    }
}
