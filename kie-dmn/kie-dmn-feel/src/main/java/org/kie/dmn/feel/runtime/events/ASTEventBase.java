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
package org.kie.dmn.feel.runtime.events;

import org.kie.dmn.api.feel.runtime.events.FEELEvent;
import org.kie.dmn.feel.lang.ast.ASTNode;

/**
 * A base class with common functionality to all events
 */
public class ASTEventBase implements FEELEvent {

    protected final Severity severity;
    protected final String message;
    protected final ASTNode astNode;
    protected Throwable sourceException;
    
    public ASTEventBase(Severity severity, String message, ASTNode astNode, Throwable sourceException) {
        this(severity, message, astNode);
        this.sourceException = sourceException;
    }

    public ASTEventBase(Severity severity, String message, ASTNode astNode) {
        this.severity = severity;
        this.message = message;
        this.astNode = astNode;
    }

    @Override
    public Severity getSeverity() {
        return severity;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Throwable getSourceException() {
        return sourceException;
    }

    @Override
    public int getLine() {
        return -1;
    }

    @Override
    public int getColumn() {
        return -1;
    }

    @Override
    public Object getOffendingSymbol() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ASTEventBase [severity=").append(severity)
        .append(", message=").append(message)
        .append(", sourceException=").append(sourceException)
        .append(", astNode=").append(astNode)
        .append("]");
        return builder.toString();
    }

    
}
