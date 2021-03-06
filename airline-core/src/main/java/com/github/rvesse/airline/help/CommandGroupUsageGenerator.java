/**
 * Copyright (C) 2010-16 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rvesse.airline.help;

import java.io.IOException;
import java.io.OutputStream;

import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;

/**
 * Interface implemented by classes that can generate usage documentation for a
 * command group
 */
public interface CommandGroupUsageGenerator<T> {

    /**
     * Generate the help and output it on standard out
     * 
     * @param global
     *            Global Metadata
     * @param groups
     *            Group path to the command
     * @throws IOException
     */
    public abstract void usage(GlobalMetadata<T> global, CommandGroupMetadata[] groups) throws IOException;

    /**
     * Generate the help and output it to the stream
     * 
     * @param global
     *            Global metadata
     * @param groups
     *            Group path to the command
     * @param out
     *            Stream to output to
     * @throws IOException
     */
    public abstract void usage(GlobalMetadata<T> global, CommandGroupMetadata[] groups, OutputStream output)
            throws IOException;
}