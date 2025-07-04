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
package org.drools.testcoverage.functional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.drools.template.parser.DecisionTableParseException;
import org.drools.testcoverage.common.listener.OrderListener;
import org.drools.testcoverage.common.listener.TrackingAgendaEventListener;
import org.drools.testcoverage.common.model.Person;
import org.drools.testcoverage.common.model.Sample;
import org.drools.testcoverage.common.model.Subject;
import org.drools.testcoverage.common.util.KieBaseTestConfiguration;
import org.drools.testcoverage.common.util.KieBaseUtil;
import org.drools.testcoverage.common.util.KieUtil;
import org.drools.testcoverage.common.util.ResourceUtil;
import org.drools.testcoverage.common.util.TestConstants;
import org.drools.testcoverage.common.util.TestParametersUtil2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.kie.api.KieBase;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.builder.DecisionTableInputType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Tests all features which can be used in decision tables.
 */
public class DecisionTableTest {

    public static Stream<KieBaseTestConfiguration> parameters() {
        return TestParametersUtil2.getKieBaseConfigurations().stream();
    }

    private static Resource sampleXlsDecisionTable;

    private static Resource sampleCsvDecisionTable;

    private static Resource multipleTablesDecisionTable;

    private static Resource evalDecisionTable;

    private static Resource advancedDecisionTable;

    private static Resource sequentialDecisionTable;

    private static Resource agendaGroupDecisionTable;

    private static Resource emptyConditionDecisionTable;

    private static Resource emptyActionDecisionTable;

    private static Resource queriesDecisionTable;

    private static Resource sampleDatesCsvDecisionTable;

    private static Resource sampleDatesXlsDecisionTable;

    private static Resource sampleDateXLSXDecisionTable;

    @BeforeAll
    public static void loadDecisionTablesToAvoidLoadingThemForEachKieBaseConfiguration() {
        sampleXlsDecisionTable = ResourceUtil.getDecisionTableResourceFromClasspath("sample.drl.xls",
                                                                                    DecisionTableTest.class,
                                                                                    DecisionTableInputType.XLS);

        sampleCsvDecisionTable = ResourceUtil.getDecisionTableResourceFromClasspath("sample.drl.csv",
                                                                                    DecisionTableTest.class,
                                                                                    DecisionTableInputType.CSV);

        multipleTablesDecisionTable = ResourceUtil.getDecisionTableResourceFromClasspath("multiple_tables.drl.xls",
                                                                                         DecisionTableTest.class,
                                                                                         DecisionTableInputType.XLS);

        evalDecisionTable = ResourceUtil.getDecisionTableResourceFromClasspath("eval_dt.drl.xls",
                                                                               DecisionTableTest.class,
                                                                               DecisionTableInputType.XLS);

        advancedDecisionTable = ResourceUtil.getDecisionTableResourceFromClasspath("advanced_dt.drl.xls",
                                                                                   DecisionTableTest.class,
                                                                                   DecisionTableInputType.XLS);

        agendaGroupDecisionTable = ResourceUtil.getDecisionTableResourceFromClasspath("agenda-group.drl.csv",
                                                                                      DecisionTableTest.class,
                                                                                      DecisionTableInputType.CSV);

        emptyConditionDecisionTable = ResourceUtil.getDecisionTableResourceFromClasspath("emptyCondition.drl.xls",
                                                                                         DecisionTableTest.class,
                                                                                         DecisionTableInputType.XLS);

        emptyActionDecisionTable = ResourceUtil.getDecisionTableResourceFromClasspath("emptyAction.drl.csv",
                                                                                      DecisionTableTest.class,
                                                                                      DecisionTableInputType.CSV);

        queriesDecisionTable = ResourceUtil.getDecisionTableResourceFromClasspath("queries.drl.xls",
                                                                                  DecisionTableTest.class,
                                                                                  DecisionTableInputType.XLS);

        sequentialDecisionTable = ResourceUtil.getDecisionTableResourceFromClasspath("sequential.drl.csv",
                                                                                     DecisionTableTest.class,
                                                                                     DecisionTableInputType.CSV);

        sampleDatesCsvDecisionTable = ResourceUtil.getDecisionTableResourceFromClasspath("sample_dates.drl.csv",
                                                                                         DecisionTableTest.class,
                                                                                         DecisionTableInputType.CSV);

        sampleDatesXlsDecisionTable = ResourceUtil.getDecisionTableResourceFromClasspath("sample_dates.drl.xls",
                                                                                         DecisionTableTest.class,
                                                                                         DecisionTableInputType.XLS);

        sampleDateXLSXDecisionTable = ResourceUtil.getDecisionTableResourceFromClasspath("inputObjectExample.drl.xlsx",
                                                                                         DecisionTableTest.class,
                                                                                         DecisionTableInputType.XLSX);

    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testSimpleXLS(KieBaseTestConfiguration kieBaseTestConfiguration) {
        testSimpleDecisionTable(kieBaseTestConfiguration, sampleXlsDecisionTable);
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testSimpleCSV(KieBaseTestConfiguration kieBaseTestConfiguration) {
        testSimpleDecisionTable(kieBaseTestConfiguration, sampleCsvDecisionTable);
    }

    private void testSimpleDecisionTable(KieBaseTestConfiguration kieBaseTestConfiguration, final Resource decisionTable) {
        final KieBase kbase = KieBaseUtil.getKieBaseFromResources(kieBaseTestConfiguration, decisionTable);

        final KieSession session = kbase.newKieSession();

        final Person person = new Person("Paul");
        person.setId(1);
        assertThat(person.getName()).isEqualTo("Paul");
        assertThat(person.getId()).isEqualTo(1);

        session.insert(person);
        session.fireAllRules();

        assertThat(person.getName()).isEqualTo("Paul");
        assertThat(person.getId()).isEqualTo(2);

        session.dispose();
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testMultipleTableXLS(KieBaseTestConfiguration kieBaseTestConfiguration) {
        final KieBase kbase = KieBaseUtil.getKieBaseFromResources(kieBaseTestConfiguration, multipleTablesDecisionTable);

        assertThat(2).isEqualTo(kbase.getKiePackages().size());

        final KieSession session = kbase.newKieSession();

        // testing person object from the first table
        final Person person = new Person("Paul");
        person.setId(1);
        assertThat(person.getName()).isEqualTo("Paul");
        assertThat(person.getId()).isEqualTo(1);

        // testing second person, he should be renamed by rules in the second
        // table
        final Person person2 = new Person("Helmut von Seireit");
        person2.setId(1000);
        assertThat(person2.getName()).isEqualTo("Helmut von Seireit");
        assertThat(person2.getId()).isEqualTo(1000);

        session.insert(person);
        session.insert(person2);
        session.fireAllRules();

        assertThat(person.getName()).isEqualTo("Paul");
        assertThat(person.getId()).isEqualTo(2);
        assertThat(person2.getName()).isEqualTo("Wilhelm von Seireit");
        assertThat(person2.getId()).isEqualTo(1000);

        session.dispose();
    }

    /**
     * test for various evaluations, file sample_eval_dt.xls need to rewrite xls
     * table and maybe add some classes to be able to do the test
     */
    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testEvalTable(KieBaseTestConfiguration kieBaseTestConfiguration) {
        final KieBase kbase = KieBaseUtil.getKieBaseFromResources(kieBaseTestConfiguration, evalDecisionTable);

        assertThat(2).isEqualTo(kbase.getKiePackages().size());

        KieSession session = kbase.newKieSession();

        final TrackingAgendaEventListener rulesFired = new TrackingAgendaEventListener();
        session.addEventListener(rulesFired);
        rulesFired.clear();

        // eval test 1
        final Subject mary = new Subject("Mary");
        mary.setDummy(1);
        session.insert(mary);
        session.fireAllRules();
        assertThat(rulesFired.isRuleFired("evalTest1")).isTrue();
        assertThat(rulesFired.isRuleFired("evalTest2")).isFalse();
        assertThat(rulesFired.isRuleFired("evalTest3")).isFalse();
        assertThat(rulesFired.isRuleFired("evalTest4")).isFalse();
        assertThat(rulesFired.isRuleFired("simpleBindingTest")).isFalse();
        session.dispose();

        // eval test 2
        session = kbase.newKieSession();
        session.addEventListener(rulesFired);
        rulesFired.clear();
        final Subject inge = new Subject("Inge");
        inge.setAge(7);
        inge.setSex("F");
        final Subject jochen = new Subject("Jochen");
        jochen.setAge(9);
        jochen.setSex("M");
        session.insert(inge);
        session.insert(jochen);
        session.fireAllRules();
        assertThat(rulesFired.isRuleFired("evalTest1")).isFalse();
        assertThat(rulesFired.isRuleFired("evalTest2")).isTrue();
        assertThat(rulesFired.isRuleFired("evalTest3")).isFalse();
        assertThat(rulesFired.isRuleFired("evalTest4")).isFalse();
        assertThat(rulesFired.isRuleFired("simpleBindingTest")).isFalse();
        session.dispose();

        // eval test 3, will run four times, there are four combinations
        session = kbase.newKieSession();
        session.addEventListener(rulesFired);
        rulesFired.clear();
        final Subject karl = new Subject("Karl");
        karl.setSex("male");
        final Subject egon = new Subject("Egon");
        egon.setSex("male");
        session.insert(karl);
        session.insert(egon);
        session.fireAllRules();
        assertThat(rulesFired.isRuleFired("evalTest1")).isFalse();
        assertThat(rulesFired.isRuleFired("evalTest2")).isFalse();
        assertThat(rulesFired.isRuleFired("evalTest3")).isTrue();
        assertThat(rulesFired.isRuleFired("evalTest4")).isFalse();
        assertThat(rulesFired.isRuleFired("simpleBindingTest")).isFalse();
        session.dispose();

        // eval test 4
        session = kbase.newKieSession();
        session.addEventListener(rulesFired);
        rulesFired.clear();
        final Subject gerda = new Subject("Gerda");
        gerda.setSex("female");
        gerda.setAge(9);
        gerda.setDummy(-10);
        session.insert(gerda);
        session.fireAllRules();
        assertThat(rulesFired.isRuleFired("evalTest1")).isFalse();
        assertThat(rulesFired.isRuleFired("evalTest2")).isFalse();
        assertThat(rulesFired.isRuleFired("evalTest3")).isFalse();
        assertThat(rulesFired.isRuleFired("evalTest4")).isTrue();
        assertThat(rulesFired.isRuleFired("simpleBindingTest")).isFalse();
        session.dispose();

        // eval test 5 - simple binding
        session = kbase.newKieSession();
        session.addEventListener(rulesFired);
        rulesFired.clear();
        final List<Sample> results = new ArrayList<>();
        session.setGlobal("results", results);
        final Sample sample = new Sample();
        session.insert(sample);
        session.fireAllRules();
        assertThat(rulesFired.isRuleFired("evalTest1")).isFalse();
        assertThat(rulesFired.isRuleFired("evalTest2")).isFalse();
        assertThat(rulesFired.isRuleFired("evalTest3")).isFalse();
        assertThat(rulesFired.isRuleFired("evalTest4")).isFalse();
        assertThat(rulesFired.isRuleFired("simpleBindingTest")).isTrue();
        session.dispose();
    }

    /**
     * test for advanced rule settings (no-loop, saliences, ...), file
     * sample_advanced_dt.xls
     * covers also bugfix for Bug724257 (agenda group not added from dtable to
     * .drl)
     */
    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testAdvancedTable(KieBaseTestConfiguration kieBaseTestConfiguration) {
        final KieBase kbase = KieBaseUtil.getKieBaseFromResources(kieBaseTestConfiguration, advancedDecisionTable);
        KieSession session = kbase.newKieSession();

        final OrderListener listener = new OrderListener();
        session.addEventListener(listener);

        final Subject lili = new Subject("Lili");
        lili.setAge(100);
        final Sample sample = new Sample();
        session.insert(lili);
        session.insert(sample);
        session.fireAllRules();

        // just 4 rules should fire
        assertThat(listener.size()).isEqualTo(4);

        // rules have to be fired in expected order
        final String[] expected = new String[]{"HelloWorld_11", "namedRule", "b1", "another rule"};
        for (int i = 0; i < 4; i++) {
            assertThat(listener.get(i)).isEqualTo(expected[i]);
        }

        session.dispose();
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testPushQueryWithFactDeclaration(KieBaseTestConfiguration kieBaseTestConfiguration) throws IllegalAccessException, InstantiationException {
        final KieBase kbase = KieBaseUtil.getKieBaseFromResources(kieBaseTestConfiguration, queriesDecisionTable);

        final FactType locationType = kbase.getFactType(TestConstants.PACKAGE_FUNCTIONAL, "Location");

        final KieSession ksession = kbase.newKieSession();
        final TrackingAgendaEventListener listener = new TrackingAgendaEventListener();
        ksession.addEventListener(listener);

        final Person peter = new Person("Peter");
        peter.setLikes("steak");
        final Object steakLocation = locationType.newInstance();
        locationType.set(steakLocation, "thing", "steak");
        locationType.set(steakLocation, "location", "table");
        final Object tableLocation = locationType.newInstance();
        locationType.set(tableLocation, "thing", "table");
        locationType.set(tableLocation, "location", "office");
        ksession.insert(peter);
        final FactHandle steakHandle = ksession.insert(steakLocation);
        final FactHandle tableHandle = ksession.insert(tableLocation);
        ksession.insert("push");
        ksession.fireAllRules();

        assertThat(listener.isRuleFired("testPushQueryRule")).isTrue();
        assertThat(listener.isRuleFired("testPullQueryRule")).isFalse();
        listener.clear();

        // when location is changed of what Peter likes, push query should fire
        // rule
        final Object steakLocation2 = locationType.newInstance();
        locationType.set(steakLocation2, "thing", "steak");
        locationType.set(steakLocation2, "location", "desk");
        final Object deskLocation = locationType.newInstance();
        locationType.set(deskLocation, "thing", "desk");
        locationType.set(deskLocation, "location", "office");
        ksession.insert(steakLocation2);
        ksession.insert(deskLocation);
        ksession.delete(steakHandle);
        ksession.delete(tableHandle);
        ksession.fireAllRules();

        assertThat(listener.isRuleFired("testPushQueryRule")).isTrue();
        assertThat(listener.isRuleFired("testPullQueryRule")).isFalse();
        listener.clear();

        final Person paul = new Person("Paul");
        paul.setLikes("steak");
        ksession.insert(paul);
        ksession.fireAllRules();

        assertThat(listener.isRuleFired("testPushQueryRule")).isTrue();
        assertThat(listener.isRuleFired("testPullQueryRule")).isFalse();
        listener.clear();

        ksession.dispose();
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testPullQueryWithFactDeclaration(KieBaseTestConfiguration kieBaseTestConfiguration) throws IllegalAccessException, InstantiationException {
        final KieBase kbase = KieBaseUtil.getKieBaseFromResources(kieBaseTestConfiguration, queriesDecisionTable);

        final FactType locationType = kbase.getFactType(TestConstants.PACKAGE_FUNCTIONAL, "Location");

        final KieSession ksession = kbase.newKieSession();
        final TrackingAgendaEventListener listener = new TrackingAgendaEventListener();
        ksession.addEventListener(listener);

        final Person peter = new Person("Peter");
        peter.setLikes("steak");
        final Object steakLocation = locationType.newInstance();
        locationType.set(steakLocation, "thing", "steak");
        locationType.set(steakLocation, "location", "table");
        final Object tableLocation = locationType.newInstance();
        locationType.set(tableLocation, "thing", "table");
        locationType.set(tableLocation, "location", "office");
        ksession.insert(peter);
        final FactHandle steakHandle = ksession.insert(steakLocation);
        final FactHandle tableHandle = ksession.insert(tableLocation);
        ksession.insert("pull");
        ksession.fireAllRules();

        assertThat(listener.isRuleFired("testPullQueryRule")).isTrue();
        assertThat(listener.isRuleFired("testPushQueryRule")).isFalse();
        listener.clear();

        // when location is changed of what Peter likes, pull query should
        // ignore it
        final Object steakLocation2 = locationType.newInstance();
        locationType.set(steakLocation2, "thing", "steak");
        locationType.set(steakLocation2, "location", "desk");
        final Object deskLocation = locationType.newInstance();
        locationType.set(deskLocation, "thing", "desk");
        locationType.set(deskLocation, "location", "office");
        ksession.insert(steakLocation2);
        ksession.insert(deskLocation);
        ksession.delete(steakHandle);
        ksession.delete(tableHandle);
        ksession.fireAllRules();

        assertThat(listener.isRuleFired("testPullQueryRule")).isFalse();
        assertThat(listener.isRuleFired("testPushQueryRule")).isFalse();
        listener.clear();

        final Person paul = new Person("Paul");
        paul.setLikes("steak");
        ksession.insert(paul);
        ksession.fireAllRules();

        assertThat(listener.isRuleFired("testPullQueryRule")).isTrue();
        assertThat(listener.isRuleFired("testPushQueryRule")).isFalse();
        listener.clear();

        ksession.dispose();
    }

    /**
     * Test sequential turned on, it overrides all user defined saliences.
     */
    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testSequential(KieBaseTestConfiguration kieBaseTestConfiguration) {
        final KieBase kbase = KieBaseUtil.getKieBaseFromResources(kieBaseTestConfiguration, sequentialDecisionTable);

        final KieSession ksession = kbase.newKieSession();
        final OrderListener listener = new OrderListener();
        ksession.addEventListener(listener);
        ksession.insert("something");
        ksession.fireAllRules();
        assertThat(listener.size()).as("Wrong number of rules fired").isEqualTo(3);
        final String[] expected = {"Rule1", "Rule2", "Rule3"};
        for (int i = 0; i < 3; i++) {
            assertThat(listener.get(i)).isEqualTo(expected[i]);
        }
        ksession.dispose();
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testLockOnActive(KieBaseTestConfiguration kieBaseTestConfiguration) {
        final KieBase kbase = KieBaseUtil.getKieBaseFromResources(kieBaseTestConfiguration, agendaGroupDecisionTable);
        final KieSession ksession = kbase.newKieSession();
        final OrderListener listener = new OrderListener();
        ksession.addEventListener(listener);
        ksession.insert("lockOnActive");
        ksession.fireAllRules();
        assertThat(listener.size()).isEqualTo(3);
        final String[] expected = {"a", "a2", "a3"};
        for (int i = 0; i < listener.size(); i++) {
            assertThat(listener.get(i)).isEqualTo(expected[i]);
        }
        ksession.dispose();
    }

    /**
     * Agenda group rule with auto focus can fire a give focus to agenda group
     * without focus set on whole agenda group.
     */
    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testAutoFocus(KieBaseTestConfiguration kieBaseTestConfiguration) {
        final KieBase kbase = KieBaseUtil.getKieBaseFromResources(kieBaseTestConfiguration, agendaGroupDecisionTable);
        final KieSession ksession = kbase.newKieSession();
        final OrderListener listener = new OrderListener();
        ksession.addEventListener(listener);

        // first test - we try to fire rule in agenda group which has auto focus
        // disable, we won't succeed
        final FactHandle withoutAutoFocus = ksession.insert("withoutAutoFocus");
        ksession.fireAllRules();
        assertThat(listener.size()).isEqualTo(0);

        // second test - we try to fire rule in agenda group with auto focus
        // enabled
        // it fires and it's defined consequence causes to fire second rule
        // which has no auto focus
        ksession.insert("autoFocus");
        ksession.delete(withoutAutoFocus);
        ksession.fireAllRules();
        assertThat(listener.size()).isEqualTo(2);
        final String[] expected = {"b2", "b1"};
        for (int i = 0; i < listener.size(); i++) {
            assertThat(listener.get(i)).isEqualTo(expected[i]);
        }
        ksession.dispose();
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testActivationGroup(KieBaseTestConfiguration kieBaseTestConfiguration) {
        final KieBase kbase = KieBaseUtil.getKieBaseFromResources(kieBaseTestConfiguration, agendaGroupDecisionTable);
        final KieSession ksession = kbase.newKieSession();
        final TrackingAgendaEventListener listener = new TrackingAgendaEventListener();
        ksession.addEventListener(listener);

        // only one rule from activation group may fire
        ksession.insert("activationGroup");
        ksession.fireAllRules();
        assertThat(listener.isRuleFired("c1")).isFalse();
        assertThat(listener.isRuleFired("c2")).isTrue();
        assertThat(listener.isRuleFired("c3")).isFalse();
        ksession.dispose();
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testEmptyConditionInXLS(KieBaseTestConfiguration kieBaseTestConfiguration) {
        assertThatExceptionOfType(DecisionTableParseException.class).isThrownBy(() -> 
        KieUtil.getKieBuilderFromResources(kieBaseTestConfiguration, true, emptyConditionDecisionTable));
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testEmptyActionInCSV(KieBaseTestConfiguration kieBaseTestConfiguration) {
        assertThatExceptionOfType(DecisionTableParseException.class).isThrownBy(() -> 
        KieUtil.getKieBuilderFromResources(kieBaseTestConfiguration, true, emptyActionDecisionTable));
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testCSVWithDateAttributes(KieBaseTestConfiguration kieBaseTestConfiguration) {
        testDecisionTableWithDateAttributes(kieBaseTestConfiguration, sampleDatesCsvDecisionTable);
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testXLSWithDateAttributes(KieBaseTestConfiguration kieBaseTestConfiguration) {
        testDecisionTableWithDateAttributes(kieBaseTestConfiguration, sampleDatesXlsDecisionTable);
    }

    private void testDecisionTableWithDateAttributes(KieBaseTestConfiguration kieBaseTestConfiguration, final Resource decisionTable) {
        final KieBase kbase = KieBaseUtil.getKieBaseFromResources(kieBaseTestConfiguration, decisionTable);

        final ArrayList<String> names = new ArrayList<>();
        final Collection<KiePackage> pkgs = kbase.getKiePackages();
        for (KiePackage kp : pkgs) {
            names.add(kp.getName());
        }

        assertThat(names.contains(TestConstants.PACKAGE_FUNCTIONAL)).isTrue();
        assertThat(names.contains(TestConstants.PACKAGE_TESTCOVERAGE_MODEL)).isTrue();

        final KiePackage kiePackage = (KiePackage) pkgs.toArray()[names.indexOf(TestConstants.PACKAGE_FUNCTIONAL)];

        assertThat(kiePackage.getRules().size()).isEqualTo(3);
    }

    public static class InputObject {

        private Date date;


        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }


    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testXLSXComparingDates(KieBaseTestConfiguration kieBaseTestConfiguration) {
        // DROOLS-6820
        KieBase kbase = KieBaseUtil.getKieBaseFromResources(kieBaseTestConfiguration, sampleDateXLSXDecisionTable);
        KieSession ksession = kbase.newKieSession();

        InputObject inputObject1 = new InputObject();
        inputObject1.setDate(new Date());
        ksession.insert(inputObject1);

        InputObject inputObject2 = new InputObject();
        inputObject2.setDate(new Date(0));
        ksession.insert(inputObject2);

        assertThat(ksession.fireAllRules()).isEqualTo(1);
    }
}
