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
package org.drools.mvel.evaluators;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.drools.base.base.ValueType;
import org.drools.compiler.rule.builder.EvaluatorDefinition;
import org.drools.drl.parser.impl.Operator;
import org.drools.base.util.TimeIntervalParser;
import org.drools.core.common.DefaultEventHandle;
import org.drools.base.rule.accessor.Evaluator;
import org.drools.base.time.Interval;
import org.kie.api.runtime.rule.FactHandle;

/**
 * <p>The implementation of the 'before' evaluator definition.</p>
 * 
 * <p>The <b><code>before</code></b> evaluator correlates two events and matches when the temporal 
 * distance from the event being correlated to the current correlated belongs to the distance range declared 
 * for the operator.</p> 
 * 
 * <p>Lets look at an example:</p>
 * 
 * <pre>$eventA : EventA( this before[ 3m30s, 4m ] $eventB )</pre>
 *
 * <p>The previous pattern will match if and only if the temporal distance between the 
 * time when $eventA finished and the time when $eventB started is between ( 3 minutes 
 * and 30 seconds ) and ( 4 minutes ). In other words:</p>
 * 
 * <pre> 3m30s <= $eventB.startTimestamp - $eventA.endTimeStamp <= 4m </pre>
 * 
 * <p>The temporal distance interval for the <b><code>before</code></b> operator is optional:</p>
 * 
 * <ul><li>If two values are defined (like in the example below), the interval starts on the
 * first value and finishes on the second.</li>
 * <li>If only one value is defined, then the interval starts on the value and finishes on 
 * the positive infinity.</li>
 * <li>If no value is defined, it is assumed that the initial value is 1ms and the final value
 * is the positive infinity.</li></ul>
 * 
 * <p><b>NOTE:</b> it is allowed to define negative distances for this operator. Example:</p>
 * 
 * <pre>$eventA : EventA( this before[ -3m30s, -2m ] $eventB )</pre>
 *
 * <p><b>NOTE:</b> if the initial value is greater than the finish value, the engine automatically
 * reverse them, as there is no reason to have the initial value greater than the finish value. Example: 
 * the following two patterns are considered to have the same semantics:</p>
 * 
 * <pre>
 * $eventA : EventA( this before[ -3m30s, -2m ] $eventB )
 * $eventA : EventA( this before[ -2m, -3m30s ] $eventB )
 * </pre>
 */
public class BeforeEvaluatorDefinition
    implements
        EvaluatorDefinition {

    public static final String beforeOp = Operator.BuiltInOperator.BEFORE.getSymbol();

    public static final Operator BEFORE = Operator.determineOperator( beforeOp, false );

    public static final Operator NOT_BEFORE = Operator.determineOperator( beforeOp, true );

    private static final String[] SUPPORTED_IDS = new String[] { beforeOp };

    private Map<String, BeforeEvaluator> cache         = Collections.emptyMap();

    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        cache = (Map<String, BeforeEvaluator>) in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject( cache );
    }

    /**
     * @inheridDoc
     */
    public Evaluator getEvaluator(ValueType type,
                                  Operator operator) {
        return this.getEvaluator( type,
                                  operator.getOperatorString(),
                                  operator.isNegated(),
                                  null );
    }

    /**
     * @inheridDoc
     */
    public Evaluator getEvaluator(ValueType type,
                                  Operator operator,
                                  String parameterText) {
        return this.getEvaluator( type,
                                  operator.getOperatorString(),
                                  operator.isNegated(),
                                  parameterText );
    }

    /**
     * @inheritDoc
     */
    public Evaluator getEvaluator(final ValueType type,
                                  final String operatorId,
                                  final boolean isNegated,
                                  final String parameterText) {
        return this.getEvaluator( type,
                                  operatorId,
                                  isNegated,
                                  parameterText,
                                  Target.HANDLE,
                                  Target.HANDLE );

    }

    /**
     * @inheritDoc
     */
    public Evaluator getEvaluator(final ValueType type,
                                  final String operatorId,
                                  final boolean isNegated,
                                  final String parameterText,
                                  final Target left,
                                  final Target right) {
        if ( this.cache == Collections.EMPTY_MAP ) {
            this.cache = new HashMap<>();
        }
        String key = left+":"+right+":"+isNegated + ":" + parameterText;
        BeforeEvaluator eval = this.cache.get( key );
        if ( eval == null ) {
            long[] params = TimeIntervalParser.parse( parameterText );
            eval = new BeforeEvaluator( type,
                                        isNegated,
                                        params,
                                        parameterText,
                                        left == Target.FACT,
                                        right == Target.FACT );
            this.cache.put( key,
                            eval );
        }
        return eval;
    }

    /**
     * @inheritDoc
     */
    public String[] getEvaluatorIds() {
        return SUPPORTED_IDS;
    }

    /**
     * @inheritDoc
     */
    public boolean isNegatable() {
        return true;
    }

    /**
     * @inheritDoc
     */
    public Target getTarget() {
        return Target.BOTH;
    }

    /**
     * @inheritDoc
     */
    public boolean supportsType(ValueType type) {
        // supports all types, since it operates over fact handles
        // Note: should we change this interface to allow checking of event classes only?
        return true;
    }

    /**
     * Implements the 'before' evaluator itself
     */
    public static class BeforeEvaluator extends PointInTimeEvaluator {
        private static final long serialVersionUID = 510l;

        public BeforeEvaluator() {
        }

        public BeforeEvaluator(final ValueType type,
                               final boolean isNegated,
                               final long[] parameters,
                               final String paramText,
                               final boolean unwrapLeft,
                               final boolean unwrapRight) {
            super( type,
                   isNegated ? NOT_BEFORE : BEFORE,
                   parameters,
                   paramText,
                   unwrapLeft,
                   unwrapRight );
        }

        @Override
        public Interval getInterval() {
            long init = (this.finalRange == Interval.MAX) ? Interval.MIN : -this.finalRange;
            long end = (this.initRange == Interval.MIN) ? Interval.MAX : -this.initRange;
            if ( this.getOperator().isNegated() ) {
                if ( init == Interval.MIN && end != Interval.MAX ) {
                    init = finalRange + 1;
                    end = Interval.MAX;
                } else if ( init != Interval.MIN && end == Interval.MAX ) {
                    init = Interval.MIN;
                    end = initRange - 1;
                } else if ( init == Interval.MIN ) {
                    init = 0;
                    end = -1;
                } else {
                    init = Interval.MIN;
                    end = Interval.MAX;
                }
            }
            return new Interval( init, end );
        }

        @Override
        protected boolean evaluate(long rightTS, long leftTS) {
            long dist = leftTS - rightTS;
            return this.getOperator().isNegated() ^ (dist >= this.initRange && dist <= this.finalRange);
        }

        @Override
        protected long getLeftTimestamp( FactHandle handle) {
            return ( (DefaultEventHandle) handle ).getStartTimestamp();
        }

        @Override
        protected long getRightTimestamp( FactHandle handle ) {
            return ( (DefaultEventHandle) handle ).getEndTimestamp();
        }
    }
}
