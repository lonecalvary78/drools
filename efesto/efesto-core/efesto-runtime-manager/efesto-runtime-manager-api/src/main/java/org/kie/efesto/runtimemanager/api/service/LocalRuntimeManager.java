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
package org.kie.efesto.runtimemanager.api.service;

import java.util.Collection;

import org.kie.efesto.common.api.model.EfestoRuntimeContext;
import org.kie.efesto.runtimemanager.api.model.EfestoInput;
import org.kie.efesto.runtimemanager.api.model.EfestoOutput;

/**
 * This is the type that run on local JVM
 */
public interface LocalRuntimeManager {

    /**
     * Produce a <code>Collection&lt;EfestoOutput&gt;</code> from the given <code>EfestoInput</code>
     *
     * @param context
     * @param toEvaluate
     * @return
     */
    @SuppressWarnings("rawtypes")
    Collection<EfestoOutput> evaluateInput(EfestoRuntimeContext context,
                                           EfestoInput... toEvaluate);

}
