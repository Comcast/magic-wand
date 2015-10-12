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
package com.comcast.cookie.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.comcast.cookie.CookieHandler;
import com.comcast.cookie.utils.IProcessBuilder;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of CookieHandler that executes a system command for clearing cookies
 *
 * @author Trent Schmidt
 *
 */
public class ProcessCookieHandler implements CookieHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessCookieHandler.class);
    private List<IProcessBuilder> cmds;

    /**
     * Creates an instance of cookie handler
     * @param cmds Command to execute
     */
    public ProcessCookieHandler(IProcessBuilder[] cmds) {
        if (null != cmds) {
            this.cmds = new ArrayList<>(cmds.length);

            for (IProcessBuilder i : cmds) {
                this.cmds.add(i);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean clearAllCookies(WebDriver wd) {
        boolean rv = true;
        for (IProcessBuilder cmd : this.cmds) {
            if (false == runCommand(cmd)) {
                rv = false;
                break;
            }
        }

        return rv;
    }

    /**
     * Executes a command
     * @param cmd Command to execute
     * @return True if command was executed, False otherwise
     */
    private boolean runCommand(IProcessBuilder cmd) {
        boolean rv = false;
        Process p = null;

        try {
            p = cmd.start();
            p.waitFor();
            if (0 == p.exitValue()) {
                rv = true;
            } else {
                LOG.warn("exit value (" + p.exitValue() + ")");
            }
        } catch (IOException | InterruptedException e) {
            LOG.warn("There was an exception while running command", e);
        }

        return rv;
    }
}
