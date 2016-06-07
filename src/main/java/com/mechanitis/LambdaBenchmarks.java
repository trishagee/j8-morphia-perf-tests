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
