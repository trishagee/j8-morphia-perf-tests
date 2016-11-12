package com.mechanitis;

import com.mechanitis.undertest.TypeConverter;
import com.mechanitis.undertest.entities.EntityWith10BasicFields;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class TypeConverterBenchmark {
    @Param({"1", "10", "100", "1000", "10000", "100000"})
    public int numberOfItems;

    public Class[] source;

    @Setup()
    public void setup(BenchmarkParams params) {
        source = new Class[numberOfItems];

        for (int i = 0; i < numberOfItems - 1; i++) {
            source[i] = EntityWith10BasicFields.class;
        }
        source[numberOfItems - 1] = String.class;
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean original() {
        return TypeConverter.original(String.class, source);
        // 242613
        //
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean refactored() {
        return TypeConverter.refactored(String.class, source);
        // 21245
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public boolean refactoredParallel() {
        return TypeConverter.refactoredParallel(String.class, source);
        // 21245
    }

}
