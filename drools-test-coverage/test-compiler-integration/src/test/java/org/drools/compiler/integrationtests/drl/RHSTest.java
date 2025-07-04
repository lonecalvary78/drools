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
package org.drools.compiler.integrationtests.drl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.drools.testcoverage.common.util.KieBaseTestConfiguration;
import org.drools.testcoverage.common.util.KieBaseUtil;
import org.drools.testcoverage.common.util.TestParametersUtil2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.Assertions.assertThat;

public class RHSTest {

    public static Stream<KieBaseTestConfiguration> parameters() {
        return TestParametersUtil2.getKieBaseCloudConfigurations(true).stream();
    }

    @ParameterizedTest(name = "KieBase type={0}")
	@MethodSource("parameters")
    public void testGenericsInRHS(KieBaseTestConfiguration kieBaseTestConfiguration) {

        final String drl =
            "package org.drools.compiler.integrationtests.drl;\n" +
            "import java.util.Map;\n" +
            "import java.util.HashMap;\n" +
            "rule \"Test Rule\"\n" +
            "  when\n" +
            "  then\n" +
            "    Map<String,String> map = new HashMap<String,String>();\n" +
            "end";

        final KieBase kbase = KieBaseUtil.getKieBaseFromKieModuleFromDrl("rhs-test", kieBaseTestConfiguration, drl);
        final KieSession ksession = kbase.newKieSession();
        ksession.dispose();
    }

    @ParameterizedTest(name = "KieBase type={0}")
	@MethodSource("parameters")
    public void testIncrementOperator(KieBaseTestConfiguration kieBaseTestConfiguration) {
        final String drl =
            "package org.drools.compiler.integrationtest.drl \n" +
            "global java.util.List list \n" +
            "rule rule1 \n" +
            "    dialect \"java\" \n" +
            "when \n" +
            "    $I : Integer() \n" +
            "then \n" +
            "    int i = $I.intValue(); \n" +
            "    i += 5; \n" +
            "    list.add( i ); \n" +
            "end \n";

        final KieBase kbase = KieBaseUtil.getKieBaseFromKieModuleFromDrl("rhs-test", kieBaseTestConfiguration, drl);
        final KieSession ksession = kbase.newKieSession();
        try {
            final List list = new ArrayList();
            ksession.setGlobal("list", list);

            ksession.insert(5);
            ksession.fireAllRules();

            assertThat(list.size()).isEqualTo(1);
            assertThat(list.get(0)).isEqualTo(10);
        } finally {
            ksession.dispose();
        }
    }

    @ParameterizedTest(name = "KieBase type={0}")
	@MethodSource("parameters")
    public void testKnowledgeHelperFixerInStrings(KieBaseTestConfiguration kieBaseTestConfiguration) {
        final String drl =
            "package org.drools.compiler.integrationtests.drl; \n" +
            "global java.util.List list \n" +
            "rule xxx \n" +
            "  no-loop true " +
            "when \n" +
            "  $fact : String() \n" +
            "then \n" +
            "  list.add(\"This is an update()\"); \n" +
            "  list.add(\"This is an update($fact)\"); \n" +
            "  update($fact); \n" +
            "end  \n";

        final KieBase kbase = KieBaseUtil.getKieBaseFromKieModuleFromDrl("rhs-test", kieBaseTestConfiguration, drl);
        final KieSession ksession = kbase.newKieSession();
        try {
            final List list = new ArrayList();
            ksession.setGlobal("list", list);

            ksession.insert("hello");
            ksession.fireAllRules();
            assertThat(list.size()).isEqualTo(2);
            assertThat(list.get(0)).isEqualTo("This is an update()");
            assertThat(list.get(1)).isEqualTo("This is an update($fact)");
        } finally {
            ksession.dispose();
        }
    }
}
