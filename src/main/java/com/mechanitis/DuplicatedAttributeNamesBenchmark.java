package com.mechanitis;

import com.mechanitis.undertest.DuplicatedAttributeNames;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.mapping.validation.ConstraintViolation;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO depends on number of fields in the entity and number of "load names" (whcih I think shouldn't be high
@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class DuplicatedAttributeNamesBenchmark {
    private final Mapper mapper = new Mapper();
    private DuplicatedAttributeNames duplicatedAttributeNames;
    private MappedClass mappedClass;

    @Setup()
    public void setup() {
        duplicatedAttributeNames = new DuplicatedAttributeNames();
        mappedClass = mapper.getMappedClass(new Entity());
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Set originalIterationCode() {
        HashSet<ConstraintViolation> violations = new HashSet<>();
        duplicatedAttributeNames.originalIteration(mapper, mappedClass, violations);
        return violations;

//12176.275 ops/ms
        //1546.271 ops/ms - 10 additional fields

    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Set refactoredStreamCode() {
        HashSet<ConstraintViolation> violations = new HashSet<>();
        duplicatedAttributeNames.refactoredIteration(mapper, mappedClass, violations);
        return violations;

// 5638.329 ops/ms - only ID field
//        613.994 ops/ms - 10 additional fields


    }

    @SuppressWarnings("unused") // fields used by Morphia
    private static class Entity {
        @Id
        private final int id = 0;
        private String field1;
        private String field2;
        private String field3;
        private String field4;
        private String field5;
        private String field6;
        private String field7;
        private String field8;
        private String field9;
        private String field10;

    }

}
