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
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO depends on number of Index values in Indexes annotation
@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class DatastoreImplBenchmark {
    private final Mapper mapper = new Mapper();
    private DatastoreImpl datastore;
    private MappedClass mappedClass;
    private DBCollection collection;

    @Setup()
    public void setup() {
        MongoClient mongoClient = new MongoClient();
        collection = mongoClient.getDB("PerformanceTest").getCollection("PerformanceTestCollection");
        mappedClass = mapper.getMappedClass(new Entity());
        datastore = new DatastoreImpl(new Morphia(), mapper, mongoClient, "PerformanceTest");
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void original() {
        //hmm, no return type, are we going to be optimised away?
        datastore.processClassAnnotationsOriginal(collection, mappedClass, true,
                new ArrayList<>(), new ArrayList<>());
        // 0.867 ops/ms (10 indexes)
        // 1.305 ops/ms (5 indexes)
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void refactored() {
        //hmm, no return type, are we going to be optimised away?
        datastore.processClassAnnotationsRefactored(collection, mappedClass, true,
                new ArrayList<>(), new ArrayList<>());
        // 0.855 ops/ms (10)
        // 1.278 ops/ms (5)
    }

    @SuppressWarnings("unused") // fields used by Morphia
    @Indexes({@Index(fields = @Field("field1")),
            @Index(fields = @Field("field2")),
            @Index(fields = @Field("field3")),
            @Index(fields = @Field("field4")),
            @Index(fields = @Field("field5"))
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
