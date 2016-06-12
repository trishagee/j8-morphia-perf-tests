package com.mechanitis;

import com.mechanitis.undertest.DatastoreImpl;
import com.mechanitis.undertest.DuplicatedAttributeNames;
import com.mongodb.BasicDBObject;
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

import java.util.Set;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO depends on number of Index values in Indexes annotation
public class CollectionRefactoring4Benchmark {
    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void original(final BenchmarkState state) {
        //hmm, no return type, are we going to be optimised away?
        state.datastore.processClassAnnotationsOriginal("Collection", state.mappedClass);
        //362 065.853
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public void refactored(final BenchmarkState state) {
        //hmm, no return type, are we going to be optimised away?
        state.datastore.processClassAnnotationsRefactored("Collection", state.mappedClass);
        //434 488.631
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private final DatastoreImpl datastore = new DatastoreImpl();
        private final Mapper mapper = new Mapper();
        private final MappedClass mappedClass;

        public BenchmarkState() {
            mappedClass = mapper.getMappedClass(new Entity());
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
