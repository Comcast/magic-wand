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
package com.comcast.cookie.utils;

import java.io.IOException;
import java.util.List;

/**
 * An interface that outlines process creation and execution
 *
 * @author Trent Schmidt
 *
 */
public interface IProcessBuilder {

    /**
     * Set the command to be run
     *
     * @param command Command parts to be executed
     * @return this process builder
     */
    IProcessBuilder command(String... command);

    /**
     * Starts the process
     *
     * @see java.lang.ProcessBuilder#start()
     *
     * @return running process
     * @throws IOException If an I/O error occurs
     */
    Process start() throws IOException;

    /**
     * Returns this process builder's operating system program and arguments. The returned list is not a copy.
     * Subsequent updates to the list will be reflected in the state of this process builder
     *
     * @see java.lang.ProcessBuilder#command()
     *
     * @return this process builder's program and its arguments
     */
    List<String> command();

    /**
     * Sets this process builder's operating system program and arguments. This method does not make a copy of the
     * command list. Subsequent updates to the list will be reflected in the state of the process builder. It is not
     * checked whether command corresponds to a valid operating system command.
     *
     * @see java.lang.ProcessBuilder#command(List)
     *
     * @param command the list containing the program and its arguments
     * @return this process builder
     */
    IProcessBuilder command(List<String> command);
}
