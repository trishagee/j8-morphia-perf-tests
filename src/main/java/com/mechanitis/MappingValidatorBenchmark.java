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
        mappedClass = mapper.getMappedClass(new EntityWithOneError());
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void original() {
        //hmm, no return type, are we going to be optimised away?
        MappingValidator.validateOriginal(mapper, asList(mappedClass));
        //911.498 ops/ms (without the log) - 1 validation error
        //820.869 ops/ms
        //238.669 ops/ms (5)
        //120.471 ops/ms (10)
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void refactored() {
        //hmm, no return type, are we going to be optimised away?
        MappingValidator.validateRefactored(mapper, asList(mappedClass));
        //833.777 ops/ms (without the log) - 1 validation error
        //787.706 ops/ms
        //262.831 ops/ms (5)
        //111.197 ops/ms (10)
    }

    @SuppressWarnings("unused") // fields used by Morphia
    private static class EntityWithOneError {
        @Id
        private final int id = 0;

        // Map with Object key will cause appropriate warning
        private Map<Object, Object> map1;
    }

    @SuppressWarnings("unused") // fields used by Morphia
    private static class EntityWith10Errors {
        @Id
        private final int id = 0;

        // Map with Object key will cause appropriate warning
        private Map<Object, Object> map1;
        private Map<Object, Object> map2;
        private Map<Object, Object> map3;
        private Map<Object, Object> map4;
        private Map<Object, Object> map5;
        private Map<Object, Object> map6;
        private Map<Object, Object> map7;
        private Map<Object, Object> map8;
        private Map<Object, Object> map9;
        private Map<Object, Object> map10;
    }

    @SuppressWarnings("unused") // fields used by Morphia
    private static class EntityWith20Errors {
        @Id
        private final int id = 0;

        // Map with Object key will cause appropriate warning
        private Map<Object, Object> map1;
        private Map<Object, Object> map2;
        private Map<Object, Object> map3;
        private Map<Object, Object> map4;
        private Map<Object, Object> map5;
        private Map<Object, Object> map6;
        private Map<Object, Object> map7;
        private Map<Object, Object> map8;
        private Map<Object, Object> map9;
        private Map<Object, Object> map10;
        private Map<Object, Object> map11;
        private Map<Object, Object> map12;
        private Map<Object, Object> map13;
        private Map<Object, Object> map14;
        private Map<Object, Object> map15;
        private Map<Object, Object> map16;
        private Map<Object, Object> map17;
        private Map<Object, Object> map18;
        private Map<Object, Object> map19;
        private Map<Object, Object> map20;
    }

}
