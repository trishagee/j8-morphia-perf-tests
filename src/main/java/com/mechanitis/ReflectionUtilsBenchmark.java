package com.mechanitis;

import com.mechanitis.undertest.ReflectionUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;

import java.lang.reflect.Field;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1000, timeUnit = MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = MILLISECONDS)
public class ReflectionUtilsBenchmark {
    @Param({"1", "10", "100", "1000", "10000", "100000"})
    public int numberOfItems;

    public Field[] source;

    @Setup()
    public void setup(BenchmarkParams params) throws NoSuchFieldException {
        source = new Field[numberOfItems];

        for (int i = 0; i < numberOfItems; i++) {
            source[i] = EntityWith10Fields.class.getField("simpleField");
        }
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public List<Field> original() {
        return ReflectionUtils.original(source, true);
        //24905
        //
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public List<Field> refactored() {
        return ReflectionUtils.refactored(source, true);
        //9168
        //
    }

    public class EntityWith10Fields {
        public String simpleField;
        private String staticField;
        private String finalField;
    }

}

