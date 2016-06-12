package com.mechanitis;

import com.google.common.base.Predicate;
import com.mechanitis.undertest.EntityScanner;
import org.mongodb.morphia.Morphia;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO depends on number of classes annotated with the Entity annotation
public class CollectionRefactoring3Benchmark {
    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void original(final BenchmarkState state) {
        state.entityScanner.mapAllClassesAnnotatedWithEntityOriginal(state.morphia);
        //0.061 ops/ms
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void refactored(final BenchmarkState state) {
        state.entityScanner.mapAllClassesAnnotatedWithEntityOriginal(state.morphia);
        //0.069 ops/ms
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private final EntityScanner entityScanner;
        private final Morphia morphia = new Morphia();

        public BenchmarkState() {
            entityScanner = new EntityScanner(new Predicate<String>() {

                @Override
                public boolean apply(final String input) {
                    return input.startsWith("com.mechanitis.undertest.entityscanner");
                }
            });
        }
    }

}
