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
package org.kie.dmn.feel.runtime.functions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.runtime.FEELNumberFunction;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;

public class CountFunction
        extends BaseFEELFunction implements FEELNumberFunction {

    public static final CountFunction INSTANCE = new CountFunction();

    private CountFunction() {
        super( "count" );
    }

    public FEELFnResult<BigDecimal> invoke(@ParameterName( "list" ) List list) {
        if ( list == null ) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "list", "cannot be null"));
        }
        
        return FEELFnResult.ofResult( BigDecimal.valueOf( list.size() ) );
    }

    public FEELFnResult<BigDecimal> invoke(@ParameterName( "c" ) Object[] list) {
        if ( list == null ) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "c", "cannot be null"));
        }
        
        return invoke( Arrays.asList( list ) );
    }

    @Override
    public List feelDialectAdaptedInputList(List toAdapt) {
        return toAdapt;
    }

}
