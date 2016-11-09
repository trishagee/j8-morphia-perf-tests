package com.mechanitis;

import com.mechanitis.undertest.ReflectionUtils;
import com.mechanitis.undertest.entities.EntityWith10BasicFields;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class ReflectionUtilsBenchmark {
    @Param({"1", "10", "100", "1000", "10000", "100000"})
    public int numberOfItems;

    private Field[] fields;

    @Setup()
    public void setup() throws NoSuchFieldException {
        fields = new Field[numberOfItems];
        for (int i = 0; i < numberOfItems; i++) {
            fields[i] = EntityWith10BasicFields.class.getField("field1");
        }
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public List original() {
        return ReflectionUtils.original(fields, false);
        //
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public List refactored() {
        return ReflectionUtils.refactored(fields, false);
        //
    }
}
