package com.mechanitis;

import com.google.common.base.Predicate;
import com.mechanitis.undertest.EntityScanner;
import org.mongodb.morphia.Morphia;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO depends on number of classes annotated with the Entity annotation
@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class EntityScannerBenchmark {
    private final Morphia morphia = new Morphia();
    private EntityScanner entityScanner;

    @Setup()
    public void setup() {
        entityScanner = new EntityScanner(new Predicate<String>() {

            @Override
            public boolean apply(final String input) {
                return input.startsWith("com.mechanitis.undertest.entityscanner");
            }
        });
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void original() {
        entityScanner.mapAllClassesAnnotatedWithEntityOriginal(morphia);
        //0.061 ops/ms
        //0.036 ops/ms (busy system)
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void refactored() {
        entityScanner.mapAllClassesAnnotatedWithEntityRefactored(morphia);
        //0.069 ops/ms
        //0.042 ops/ms (busy)
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Set<URL> removingOriginal() {
        return entityScanner.identifyURLsOriginal();
        //
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Set<URL> removingRefactored() {
        return entityScanner.identifyURLsRefactored();
        //
    }

}
