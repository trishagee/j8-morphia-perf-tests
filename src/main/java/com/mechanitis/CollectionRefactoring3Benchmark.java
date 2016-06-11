package com.mechanitis;

import com.mechanitis.undertest.DuplicatedAttributeNames;
import com.mechanitis.undertest.EntityScanner;
import com.mongodb.BasicDBObject;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.mapping.validation.ConstraintViolation;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO depends on number of classes annotated with the Entity annotation
public class CollectionRefactoring3Benchmark {
    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Set original(final BenchmarkState state) {
//        new EntityScanner(new Predicate<String>() {
//
//            @Override
//            public boolean apply(final String input) {
//                return input.startsWith(EntityScannerTest.class.getPackage().getName());
//            }
//        });

        return null;
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private final int numberOfValues = 1000;
        private final DuplicatedAttributeNames duplicatedAttributeNames;
        private final Mapper mapper = new Mapper();
        private final BasicDBObject source = new BasicDBObject(numberOfValues);
        private final MappedClass mappedClass;

        public BenchmarkState() {
            duplicatedAttributeNames = new DuplicatedAttributeNames();
            mappedClass = mapper.getMappedClass(new Entity());
            for (int i = 0; i < numberOfValues; i++) {
                source.put(String.valueOf(i), String.valueOf(i));
            }
        }
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
