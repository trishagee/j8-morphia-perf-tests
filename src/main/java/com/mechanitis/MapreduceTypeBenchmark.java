package com.mechanitis;

import com.mechanitis.undertest.Mapper;
import com.mechanitis.undertest.MapreduceType;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.MappedField;
import org.openjdk.jmh.annotations.*;

import java.lang.annotation.Annotation;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1000, timeUnit = MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = MILLISECONDS)
public class MapreduceTypeBenchmark {

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public MapreduceType original() {
        return MapreduceType.original("MERGE");
        //27721
        //
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public MapreduceType refactored() {
        return MapreduceType.refactored("MERGE");
        //8168
        //
    }

}
