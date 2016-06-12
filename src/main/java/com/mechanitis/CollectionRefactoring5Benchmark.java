package com.mechanitis;

import com.mechanitis.undertest.MappingValidator;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.Mapper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO depends on number of validation errors
public class CollectionRefactoring5Benchmark {
    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void original(final BenchmarkState state) {
        //hmm, no return type, are we going to be optimised away?
        MappingValidator.validateOriginal(state.mapper, asList(state.mappedClass));
        //911.498 ops/ms (without the log)
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void refactored(final BenchmarkState state) {
        //hmm, no return type, are we going to be optimised away?
        MappingValidator.validateRefactored(state.mapper, asList(state.mappedClass));
        //833.777 ops/ms (without the log)
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private final Mapper mapper = new Mapper();
        private final MappedClass mappedClass;

        public BenchmarkState() {
            mappedClass = mapper.getMappedClass(new Entity());
        }
    }

    @SuppressWarnings("unused") // fields used by Morphia
    private static class Entity {
        @Id
        private final int id = 0;

        // Map with Object key will cause appropriate warning
        private Map<Object, Object> map;
    }

}
