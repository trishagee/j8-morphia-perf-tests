/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.mechanitis;

import com.mechanitis.undertest.IterHelper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LambdaBenchmarks {

    @SuppressWarnings("Convert2Lambda")
    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public String[] decodeWithAnonymousInnerClass(final BenchmarkState state) {
        IterHelper.loopMap(state.values, new IterHelper.MapIterCallback<Integer, String>() {
            @Override
            public void eval(final Integer key, final String value) {
                state.arrayOfResults[key] = value;
            }
        });
        return state.arrayOfResults;
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void decodeWithLambda(final BenchmarkState state) {
        IterHelper.<Integer, String>loopMap(state.values, (key, value) -> state.arrayOfResults[key] = value) ;
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private int numberOfValues = 1000;
        private Map<Integer, String> values;
        private String[] arrayOfResults = new String[numberOfValues];

        public BenchmarkState() {
            this.values = initialiseMap(numberOfValues);
        }

        private Map<Integer, String> initialiseMap(final int numberOfValues) {
            final Map<Integer, String> result = new HashMap<>(numberOfValues);
            for (int i = 0; i < numberOfValues; i++) {
                result.put(i, String.valueOf(i));
            }
            return result;
        }
    }

}
