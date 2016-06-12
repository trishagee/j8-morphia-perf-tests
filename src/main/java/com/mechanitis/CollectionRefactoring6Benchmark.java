package com.mechanitis;

import com.mechanitis.undertest.QueryImpl;
import com.mongodb.MongoClient;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Id;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO depends on ...?
public class CollectionRefactoring6Benchmark {
    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public String[] original(final BenchmarkState state) {
        return state.query.retrieveKnownFieldsOriginal(state.datastore, Entity.class);
        // 3280.312 ops/ms
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public String[] simplified(final BenchmarkState state) {
        return state.query.retrieveKnownFieldsSimplified(state.datastore, Entity.class);
        //2455.044 ops/ms
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public String[] refactored(final BenchmarkState state) {
        return state.query.retrieveKnownFieldsRefactored(state.datastore, Entity.class);
        //2392.828 ops/ms
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private final QueryImpl<String> query = new QueryImpl<>();
        private final DatastoreImpl datastore;

        public BenchmarkState() {
            datastore = (DatastoreImpl)new Morphia().createDatastore(new MongoClient(), "CollectionRefactoring6Benchmark");
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
