package com.mechanitis;

import com.mechanitis.undertest.MappingValidator;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.Mapper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO depends on number of validation errors
@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class MappingValidatorBenchmark {
    private final Mapper mapper = new Mapper();
    private MappedClass mappedClass;

    @Setup()
    public void setup() {
        mappedClass = mapper.getMappedClass(new Entity());
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void original() {
        //hmm, no return type, are we going to be optimised away?
        MappingValidator.validateOriginal(mapper, asList(mappedClass));
        //911.498 ops/ms (without the log)
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void refactored() {
        //hmm, no return type, are we going to be optimised away?
        MappingValidator.validateRefactored(mapper, asList(mappedClass));
        //833.777 ops/ms (without the log)
    }

    @SuppressWarnings("unused") // fields used by Morphia
    private static class Entity {
        @Id
        private final int id = 0;

        // Map with Object key will cause appropriate warning
        private Map<Object, Object> map;
    }

}
