/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.api.builder;

/**
 * A Message generated by the building process of a KieModule
 */
public interface Message {

    /**
     * Returns the unique identifier of this message
     */
    long getId();

    /**
     * Returns the message level
     */
    Level getLevel();

    /***
     * Returns the path of the resource that caused the creation of this Message
     */
    String getPath();

    /**
     * Returns the line number in the resource of the issue that caused the creation of this Message
     */
    int getLine();

    /**
     * Returns the column number in the resource of the issue that caused the creation of this Message
     */
    int getColumn();

    /**
     * Return the text of this Message
     */
    String getText();

    public static enum Level {
        ERROR, WARNING, INFO;
    }
}
