package com.mechanitis;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class OptionalBenchmark {
    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean unchangingStringNullCheckNotNull(BenchmarkState state) {
        return state.finalStringNotNull == null;
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean unchangingStringNullCheckNotNullReversed(BenchmarkState state) {
        return state.finalStringNotNull != null;
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean unchangingStringNullCheckNull(BenchmarkState state) {
        return state.finalStringNull == null;
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean unchangingStringNullCheckNullReversed(BenchmarkState state) {
        return state.finalStringNull != null;
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean unchangingIntegerNullCheckNotNull(BenchmarkState state) {
        return state.finalIntegerNotNull == null;
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean unchangingIntegerNullCheckNull(BenchmarkState state) {
        return state.finalIntegerNull == null;
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean changingNullCheckNotNull(BenchmarkState state) {
        return state.variableNotNull == null;
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean changingNullCheckNull(BenchmarkState state) {
        return state.variableNull == null;
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean unchangingFieldOptionalNotNull(BenchmarkState state) {
        return state.notEmpty.isPresent();
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean unchangingFieldOptionalNull(BenchmarkState state) {
        return state.empty.isPresent();
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean changingOptionalCreatedNotNull(BenchmarkState state) {
        // like for like comparison
        state.mutateVariables();
        final Optional<Integer> optional = Optional.of(state.variableNotNull);
        return optional.isPresent();
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean changingOptionalCreatedNull(BenchmarkState state) {
        // like for like comparison
        state.mutateVariables();
        final Optional<Integer> optional = Optional.ofNullable(state.variableNull);
        return optional.isPresent();
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean unchangingOptionalCreatedNotNull(BenchmarkState state) {
        // like for like comparison
        state.mutateVariables();
        // not using the mutated values, just want to do the same number of ops for comparison
        final Optional<Integer> optional = Optional.of(state.finalIntegerNotNull);
        return optional.isPresent();
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean unchangingOptionalCreatedNull(BenchmarkState state) {
        // like for like comparison
        state.mutateVariables();
        // not using the mutated values, just want to do the same number of ops for comparison
        final Optional<Integer> optional = Optional.ofNullable(state.finalIntegerNull);
        return optional.isPresent();
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private final Integer finalIntegerNotNull = 1;
        private final Integer finalIntegerNull = null;
        private final String finalStringNotNull = "value";
        private final String finalStringNull = null;
        private final Optional<Integer> notEmpty = Optional.of(finalIntegerNotNull);
        private final Optional<Integer> empty = Optional.ofNullable(finalIntegerNull);
        // no noticeable different between perf of Integer and String Optionals.
        private Integer variableNotNull = 0;
        private Integer variableNull = null;

        private void mutateVariables() {
            variableNotNull++;
            variableNull = null;
        }
    }
}
