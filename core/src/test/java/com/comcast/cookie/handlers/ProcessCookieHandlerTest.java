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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.cookie.utils.IProcessBuilder;
import com.comcast.cookie.utils.SystemProcess;

public class ProcessCookieHandlerTest {
    private class testCommands {
        private List<IProcessBuilder> cmds = new ArrayList<IProcessBuilder>();
        private boolean expected_result;

        public testCommands addCmd(IProcessBuilder cmd) {
            cmds.add(cmd);
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
    public Object[][] createData() throws IOException, InterruptedException {
        IProcessBuilder cmd1 = mock(SystemProcess.class);
        Process cmd_process1 = mock(Process.class);
        when(cmd1.start()).thenReturn(cmd_process1);
        when(cmd_process1.exitValue()).thenReturn(0);


        IProcessBuilder cmd2 = mock(SystemProcess.class);
        Process cmd_process2 = mock(Process.class);
        when(cmd2.start()).thenReturn(cmd_process2);
        when(cmd_process2.exitValue()).thenReturn(1);

        IProcessBuilder cmd3 = mock(SystemProcess.class);
        when(cmd3.start()).thenThrow(new IOException());

        IProcessBuilder cmd4 = mock(SystemProcess.class);
        Process cmd_process4 = mock(Process.class);
        when(cmd4.start()).thenReturn(cmd_process4);
        when(cmd_process4.waitFor()).thenThrow(new InterruptedException());


        return new Object[][] {
                {new testCommands().addCmd(cmd1).setExpected(true)},
                {new testCommands().addCmd(cmd2).setExpected(false)},
                {new testCommands().addCmd(cmd3).setExpected(false)},
                {new testCommands().addCmd(cmd4).setExpected(false)}
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
