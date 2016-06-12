package com.mechanitis;

import com.mechanitis.undertest.DatastoreImpl;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.Mapper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.ArrayList;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO depends on number of Index values in Indexes annotation
public class CollectionRefactoring4Benchmark {
    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void original(final BenchmarkState state) {
        //hmm, no return type, are we going to be optimised away?
        state.datastore.processClassAnnotationsOriginal(state.collection, state.mappedClass, true,
                new ArrayList<>(), new ArrayList<>());
        //0.867 ops/ms
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void refactored(final BenchmarkState state) {
        //hmm, no return type, are we going to be optimised away?
        state.datastore.processClassAnnotationsRefactored(state.collection, state.mappedClass, true,
                new ArrayList<>(), new ArrayList<>());
        //0.855 ops/ms
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private final DatastoreImpl datastore;
        private final Mapper mapper = new Mapper();
        private final MappedClass mappedClass;
        private final DBCollection collection;

        public BenchmarkState() {
            MongoClient mongoClient = new MongoClient();
            collection = mongoClient.getDB("PerformanceTest").getCollection("PerformanceTestCollection");
            mappedClass = mapper.getMappedClass(new Entity());
            datastore = new DatastoreImpl(new Morphia(), mapper, mongoClient, "PerformanceTest");
        }
    }

    @SuppressWarnings("unused") // fields used by Morphia
    @Indexes({@Index(fields = @Field("field1")),
            @Index(fields = @Field("field2")),
            @Index(fields = @Field("field3")),
            @Index(fields = @Field("field4")),
            @Index(fields = @Field("field5")),
            @Index(fields = @Field("field6")),
            @Index(fields = @Field("field7")),
            @Index(fields = @Field("field8")),
            @Index(fields = @Field("field9")),
            @Index(fields = @Field("field10"))
    })
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
