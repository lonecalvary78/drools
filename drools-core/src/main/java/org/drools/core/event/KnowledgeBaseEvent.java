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
package org.drools.core.event;

import java.util.EventObject;

import org.drools.base.definitions.InternalKnowledgePackage;
import org.drools.base.definitions.rule.impl.RuleImpl;
import org.drools.core.impl.InternalRuleBase;

public class KnowledgeBaseEvent extends EventObject {

    private static final long serialVersionUID = 510l;
    private final InternalRuleBase kBase;
    private final InternalKnowledgePackage pkg;
    private final RuleImpl rule;
    private final String function;

    public KnowledgeBaseEvent(final InternalRuleBase kBase) {
        super( kBase );
        this.kBase = kBase;
        this.pkg = null;
        this.rule = null;
        this.function = null;
    }

    public KnowledgeBaseEvent(final InternalRuleBase kBase,
                              final InternalKnowledgePackage pkg) {
        super( kBase );
        this.kBase = kBase;
        this.pkg = pkg;
        this.rule = null;
        this.function = null;
    }

    public KnowledgeBaseEvent(final InternalRuleBase kBase,
                              final InternalKnowledgePackage pkg,
                              final RuleImpl rule) {
        super( kBase );
        this.kBase = kBase;
        this.pkg = pkg;
        this.rule = rule;
        this.function = null;
    }

    public KnowledgeBaseEvent(final InternalRuleBase kBase,
                              final InternalKnowledgePackage pkg,
                              final String function) {
        super( kBase );
        this.kBase = kBase;
        this.pkg = pkg;
        this.rule = null;
        this.function = function;
    }

    public InternalRuleBase getKnowledgeBase() {
        return this.kBase;
    }

    public InternalKnowledgePackage getPackage() {
        return this.pkg;
    }

    public RuleImpl getRule() {
        return this.rule;
    }

    public String getFunction() {
        return this.function;
    }

}
