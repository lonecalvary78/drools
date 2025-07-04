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
package org.kie.maven.plugin.ittests;

import java.net.URL;

import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.junit.jupiter.api.Test;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.Assertions.assertThat;

public class ExecModelParameterTestIT {

    private static final String GAV_ARTIFACT_ID = "kie-maven-plugin-test-kjar-10-yes-generate";
    private static final String GAV_VERSION = "${org.kie.version}";
    private final static String KBASE_NAME = "SimpleKBase";
    private final static String RULE_NAME = "Hello";
    private static final String CANONICAL_KIE_MODULE = "org.drools.modelcompiler.CanonicalKieModule";

    @Test
    public void testWithoutDroolsModelCompilerOnClassPathDoNotRunExecModel() throws Exception {
        KieModule kieModule = fireRule();
        assertThat(kieModule).isNotNull();
        assertThat(kieModule.getClass().getCanonicalName()).isNotEqualTo(CANONICAL_KIE_MODULE);
    }

    private KieModule fireRule() throws Exception {
        final URL targetLocation = ExecModelParameterTestIT.class.getProtectionDomain().getCodeSource().getLocation();
        return ITTestsUtils.fireRule(targetLocation, GAV_ARTIFACT_ID, GAV_VERSION, KBASE_NAME, RULE_NAME);
    }
}