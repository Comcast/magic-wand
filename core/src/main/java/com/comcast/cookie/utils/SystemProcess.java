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
 * A class that creates and executes a process
 *
 * @author Trent Schmidt
 *
 */
public class SystemProcess implements IProcessBuilder {
    private ProcessBuilder pb = new ProcessBuilder();

    /**
     * {@inheritDoc}
     */
    @Override
    public IProcessBuilder command(String... command) {
        pb.command(command);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Process start() throws IOException {
        return pb.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> command() {
        return pb.command();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IProcessBuilder command(List<String> command) {
        pb.command(command);
        return this;
    }
}
