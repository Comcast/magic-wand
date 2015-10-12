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
/**
 * @author Bobby Jap
 */

package com.comcast.magicwand.utils;

public class SystemDetail
{
    /**
     * Determine if the system is running on Windows.
     * @author Bobby Jap
     * @return The confirmation or denial
     */
    public static boolean deviceIsRunningWindows()
    {
        return (SystemDetail.getOsName().toLowerCase().indexOf("win") >= 0);
    }

    /**
     * Determine if the system is running on OS X.
     * @author Bobby Jap
     * @return The confirmation or denial
     */
    public static boolean deviceIsRunningMac()
    {
        return (SystemDetail.getOsName().toLowerCase().indexOf("mac") >= 0);
    }

    /**
     * Determine if the system is running on Unix.
     * @author Bobby Jap
     * @return The confirmation or denial
     */
    public static boolean deviceIsUnix()
    {
        return (SystemDetail.getOsName().toLowerCase().indexOf("nix") >= 0 || SystemDetail.getOsName().toLowerCase().indexOf("aix") > 0);
    }

    /**
     * Determine if the system is running on Linux.
     * @author Bobby Jap
     * @return The confirmation or denial
     */
    public static boolean deviceIsLinux()
    {
        return (SystemDetail.getOsName().toLowerCase().indexOf("nux") >= 0);
    }

    /**
     * Determine if the system is running on Solaris.
     * @author Bobby Jap
     * @return The confirmation or denial
     */
    public static boolean deviceIsSolaris()
    {
        return (SystemDetail.getOsName().toLowerCase().indexOf("sunos") >= 0);
    }

    /**
     * Determine the OS the system is running on.
     * @author Bobby Jap
     * @return The OS name
     */
    public static String getOsName()
    {
        return System.getProperty("os.name");
    }
}
