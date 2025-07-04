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
package org.drools.compiler.integrationtests;

import java.util.List;
import java.util.stream.Stream;

import org.drools.compiler.integrationtests.model.CalcFact;
import org.drools.compiler.integrationtests.model.Item;
import org.drools.compiler.integrationtests.model.RecordFact;
import org.drools.testcoverage.common.util.KieBaseTestConfiguration;
import org.drools.testcoverage.common.util.KieBaseUtil;
import org.drools.testcoverage.common.util.TestParametersUtil2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.Assertions.assertThat;

public class UnexpectedLoopTest {


    public static Stream<KieBaseTestConfiguration> parameters() {
        return TestParametersUtil2.getKieBaseCloudConfigurations(true).stream();
    }

    @ParameterizedTest(name = "KieBase type={0}")
	@MethodSource("parameters")
    public void joinFromFromPeerUpdate_shouldNotLoop(KieBaseTestConfiguration kieBaseTestConfiguration) {

        final KieBase kieBase = KieBaseUtil.getKieBaseFromClasspathResources(getClass(), kieBaseTestConfiguration,
                                                                             "org/drools/compiler/integrationtests/joinFromFrom.drl");
        final KieSession ksession = kieBase.newKieSession();
        try {
            RecordFact record1 = new RecordFact(null, 0);
            Item item1 = new Item(null);
            CalcFact calcFact = new CalcFact(List.of(item1), 4144);

            ksession.insert("test");
            ksession.insert(record1);
            ksession.insert(item1);
            ksession.insert(calcFact);

            int fired = ksession.fireAllRules(10);

            assertThat(fired).as("Unexpected loop detected. Expects firing R2 once").isEqualTo(1);
        } finally {
            ksession.dispose();
        }
    }
}
