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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.cookie.utils.IProcessBuilder;
import com.comcast.cookie.utils.SystemProcess;

public class ProcessCookieHandlerIT {

    private class testCommands {
        private List<IProcessBuilder> cmds = new ArrayList<IProcessBuilder>();
        private boolean expected_result;

        public testCommands addCmd(String cmd) {
            cmds.add(new SystemProcess().command(cmd));
            return this;
        }

        public testCommands addCmd(List<String> cmd) {
            cmds.add(new SystemProcess().command(cmd));
            return this;
        }

        public testCommands setExpected(boolean rv) {
            expected_result = rv;
            return this;
        }

        public boolean getExpected() {
            return expected_result;
        }

        public IProcessBuilder[] getCommandsArray() {
            IProcessBuilder[] pb = new IProcessBuilder[cmds.size()];

            {
                int count = 0;
                for (IProcessBuilder i : cmds) {
                    pb[count] = i;
                    count++;
                }
            }
            return pb;
        }
    }

    @DataProvider(name = "provider")
    public Object[][] createData() {

        return new Object[][] {
                {new testCommands().addCmd("ls").addCmd("pwd").setExpected(true)},
                {new testCommands().addCmd("ls").addCmd("pwd").addCmd("fake_command_which_should_fail").setExpected(false)},
                {new testCommands().addCmd(Arrays.asList("ls", "non_existant_file")).setExpected(false)}
        };
    }

    @Test(dataProvider = "provider", groups={"util", "cookie"})
    public void testCookie(testCommands commands) {
        ProcessCookieHandler c;
        boolean rv = commands.getExpected();

        c = new ProcessCookieHandler(commands.getCommandsArray());

        Assert.assertEquals(rv, c.clearAllCookies(null));

    }
}
