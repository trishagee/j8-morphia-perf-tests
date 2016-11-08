package com.mechanitis;

import com.mechanitis.undertest.QueryImpl;
import com.mongodb.MongoClient;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Id;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO depends on number of fields in the entity
@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class QueryImplBenchmark {
    private final QueryImpl<String> query = new QueryImpl<>();
    private DatastoreImpl datastore;

    @Setup()
    public void setup() {
        datastore = (DatastoreImpl)new Morphia().createDatastore(new MongoClient(), "CollectionRefactoring6Benchmark");
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public String[] original() {
        return query.retrieveKnownFieldsOriginal(datastore, Entity.class);
        // 3280.312 ops/ms
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public String[] simplified() {
        return query.retrieveKnownFieldsSimplified(datastore, Entity.class);
        //2455.044 ops/ms
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public String[] refactored() {
        return query.retrieveKnownFieldsRefactored(datastore, Entity.class);
        //2392.828 ops/ms
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public String[] refactoredMore() {
        return query.retrieveKnownFieldsRefactoredMore(datastore, Entity.class);
        //2743 ops/ms
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
