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
package org.drools.core.event.rule.impl;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.drools.core.common.PropagationContext;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.RuleRuntimeEvent;
import org.kie.api.runtime.KieRuntime;

public class RuleRuntimeEventImpl implements RuleRuntimeEvent, Externalizable {
    private KieRuntime kruntime;
    
    private PropagationContext propagationContext;
    
    public RuleRuntimeEventImpl() {
        
    }
                                      
    public RuleRuntimeEventImpl(KieRuntime kruntime, PropagationContext propagationContext) {
        this.kruntime = kruntime;
        this.propagationContext = propagationContext;
    }
    
    public KieRuntime getKieRuntime() {
        return this.kruntime;
    }
    
    public PropagationContext getPropagationContext() {
        return this.propagationContext;
    }

    public Rule getRule() {
        return this.propagationContext != null ? this.getPropagationContext().getRuleOrigin() : null;
    }
    
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject( propagationContext );
    }
    
    public void readExternal(ObjectInput in) throws IOException,
                                            ClassNotFoundException {
        this.propagationContext = (PropagationContext) in.readObject();
    }

    @Override
    public String toString() {
        return "==>[WorkingMemoryEventImpl: getKnowledgeRuntime()=" + getKieRuntime()
                + ", getPropagationContext()=" + getPropagationContext() + "]";
    }
}
