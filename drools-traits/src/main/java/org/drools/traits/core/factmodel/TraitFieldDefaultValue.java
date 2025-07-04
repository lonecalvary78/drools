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
package org.drools.traits.core.factmodel;

import java.io.Serializable;
import java.util.BitSet;


public class TraitFieldDefaultValue implements LatticeElement<Object>, Serializable {

    private Object value;
    private BitSet bitMask;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public BitSet getBitMask() {
        return bitMask;
    }

    public void setBitMask(BitSet bitMask) {
        this.bitMask = bitMask;
    }

    public TraitFieldDefaultValue(Object value, BitSet bitMask) {
        this.value = value;
        this.bitMask = bitMask;
    }

    @Override
    public String toString() {
        return "TraitFieldDefaultValue{" +
               "value=" + value +
               ", bitMask=" + bitMask +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TraitFieldDefaultValue that = (TraitFieldDefaultValue) o;

        if (!bitMask.equals(that.bitMask)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return bitMask.hashCode();
    }
}
