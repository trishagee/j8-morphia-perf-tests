package com.mechanitis;

import org.mongodb.morphia.logging.Logger;
import org.mongodb.morphia.logging.MorphiaLoggerFactory;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class LoggingBenchmark {
    private static final Logger LOG = MorphiaLoggerFactory.get(LoggingBenchmark.class);

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void loggingWithoutProtection(BenchmarkState state) {
        log("Logging: " + state.i);
        state.i++;
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void loggingSameMessageWithoutProtection(BenchmarkState state) {
        log("Logging");
        state.i++; // like for like comparison
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void loggingWithLambda(BenchmarkState state) {
        log(() -> "Logging: " + state.i);
        state.i++;
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void loggingSameMessageWithLambda(BenchmarkState state) {
        log(() -> "Logging");
        state.i++; // like for like comparison
    }

    private void log(String message) {
        LOG.debug(message);
    }

    private void log(Supplier<String> messageSupplier) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(messageSupplier.get());
        }
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private int i = 0;
    }
}
