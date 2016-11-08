package com.mechanitis;

import com.mechanitis.undertest.ContainsEmbeddedWithId;
import com.mechanitis.undertest.ContainsEmbeddedWithIdRewritten;
import com.mechanitis.undertest.DatastoreImpl;
import com.mongodb.DBCollection;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.Mapper;
import org.openjdk.jmh.annotations.*;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class ContainsEmbeddedWithIdBenchmark {
//    private final Mapper mapper = new Mapper();
//    private DatastoreImpl datastore;
//    private DBCollection collection;
//    private ContainsEmbeddedWithId containsEmbeddedWithId;
//    private ContainsEmbeddedWithIdRewritten containsEmbeddedWithIdRewritten;
//
//    private MappedClass mappedClassWith10BasicFields;
//    private MappedClass mappedClassWith10FieldsWithoutIds;
//    private MappedClass mappedClassWith10FieldsWithIds;
//    private MappedClass mappedClassWith10FieldsWith10Fields;
//
//    @Setup()
//    public void setup() {
//        mappedClassWith10BasicFields = mapper.getMappedClass(new ContainsEmbeddedWithIdBenchmark.EntityWith10BasicFields());
//        mappedClassWith10FieldsWithoutIds = mapper.getMappedClass(new ContainsEmbeddedWithIdBenchmark.EntityWith10FieldsWithoutIds());
//        mappedClassWith10FieldsWithIds = mapper.getMappedClass(new ContainsEmbeddedWithIdBenchmark.EntityWith10FieldsWithIds());
//        mappedClassWith10FieldsWith10Fields = mapper.getMappedClass(new ContainsEmbeddedWithIdBenchmark.EntityWith10FieldsWith10Fields());
//        containsEmbeddedWithId = new ContainsEmbeddedWithId();
//        containsEmbeddedWithIdRewritten = new ContainsEmbeddedWithIdRewritten();
//    }
//
//    @Benchmark
//    @OutputTimeUnit(MILLISECONDS)
//    public void originalWith10BasicFields() {
//        containsEmbeddedWithId.checkOriginal(mapper, mappedClassWith10BasicFields, new HashSet<>());
//        //450ish
//        //
//    }
//
//    @Benchmark
//    @OutputTimeUnit(MILLISECONDS)
//    public void originalWith10FieldsWithIds() {
//        containsEmbeddedWithId.checkOriginal(mapper, mappedClassWith10FieldsWithIds, new HashSet<>());
//        //350ish
//    }
//
//    @Benchmark
//    @OutputTimeUnit(MILLISECONDS)
//    public void originalWith10FieldsWithoutIds() {
//        containsEmbeddedWithId.checkOriginal(mapper, mappedClassWith10FieldsWithoutIds, new HashSet<>());
//        //799
//    }
//
//    @Benchmark
//    @OutputTimeUnit(MILLISECONDS)
//    public void originalWith10FieldsWith10Fields() {
//        containsEmbeddedWithId.checkOriginal(mapper, mappedClassWith10FieldsWith10Fields, new HashSet<>());
//        //270
//    }
//
//    @Benchmark
//    @OutputTimeUnit(MILLISECONDS)
//    public void refactoredWith10BasicFields() {
//        containsEmbeddedWithId.checkRefactored(mapper, mappedClassWith10BasicFields, new HashSet<>());
//        //450
//    }
//
//    @Benchmark
//    @OutputTimeUnit(MILLISECONDS)
//    public void refactoredWith10FieldsWithIds() {
//        containsEmbeddedWithId.checkRefactored(mapper, mappedClassWith10FieldsWithIds, new HashSet<>());
//        //350
//    }
//
//    @Benchmark
//    @OutputTimeUnit(MILLISECONDS)
//    public void refactoredWith10FieldsWithoutIds() {
//        containsEmbeddedWithId.checkRefactored(mapper, mappedClassWith10FieldsWithoutIds, new HashSet<>());
//        //720
//    }
//
//    @Benchmark
//    @OutputTimeUnit(MILLISECONDS)
//    public void refactoredWith10FieldsWith10Fields() {
//        containsEmbeddedWithId.checkRefactored(mapper, mappedClassWith10FieldsWith10Fields, new HashSet<>());
//        //250
//    }
//
//    @Benchmark
//    @OutputTimeUnit(MILLISECONDS)
//    public void rewrittenWith10BasicFields() {
//        containsEmbeddedWithIdRewritten.check(mapper, mappedClassWith10BasicFields, new HashSet<>());
//        //250
//    }
//
//    @Benchmark
//    @OutputTimeUnit(MILLISECONDS)
//    public void rewrittenWith10FieldsWithIds() {
//        containsEmbeddedWithIdRewritten.check(mapper, mappedClassWith10FieldsWithIds, new HashSet<>());
//        //181
//    }
//
//    @Benchmark
//    @OutputTimeUnit(MILLISECONDS)
//    public void rewrittenWith10FieldsWithoutIds() {
//        containsEmbeddedWithIdRewritten.check(mapper, mappedClassWith10FieldsWithoutIds, new HashSet<>());
//        //502
//    }
//
//    @Benchmark
//    @OutputTimeUnit(MILLISECONDS)
//    public void rewrittenWith10FieldsWith10Fields() {
//        containsEmbeddedWithIdRewritten.check(mapper, mappedClassWith10FieldsWith10Fields, new HashSet<>());
//        //150
//    }
//
//    private static class EntityWith10FieldsWithIds {
//        private EmbeddedWithId field1;
//        private EmbeddedWithId field2;
//        private EmbeddedWithId field3;
//        private EmbeddedWithId field4;
//        private EmbeddedWithId field5;
//        private EmbeddedWithId field6;
//        private EmbeddedWithId field7;
//        private EmbeddedWithId field8;
//        private EmbeddedWithId field9;
//        private EmbeddedWithId field10;
//    }
//
//    private static class EntityWith10FieldsWithoutIds {
//        private EmbeddedWithoutFields field1;
//        private EmbeddedWithoutFields field2;
//        private EmbeddedWithoutFields field3;
//        private EmbeddedWithoutFields field4;
//        private EmbeddedWithoutFields field5;
//        private EmbeddedWithoutFields field6;
//        private EmbeddedWithoutFields field7;
//        private EmbeddedWithoutFields field8;
//        private EmbeddedWithoutFields field9;
//        private EmbeddedWithoutFields field10;
//    }
//
//    private static class EntityWith10FieldsWith10Fields {
//        private EntityWith10BasicFields field1;
//        private EntityWith10BasicFields field2;
//        private EntityWith10BasicFields field3;
//        private EntityWith10BasicFields field4;
//        private EntityWith10BasicFields field5;
//        private EntityWith10BasicFields field6;
//        private EntityWith10BasicFields field7;
//        private EntityWith10BasicFields field8;
//        private EntityWith10BasicFields field9;
//        private EntityWith10BasicFields field10;
//    }
//
//    private static class EntityWith10BasicFields {
//        private String field1;
//        private String field2;
//        private String field3;
//        private String field4;
//        private String field5;
//        private String field6;
//        private String field7;
//        private String field8;
//        private String field9;
//        private String field10;
//    }
//
//    private static class EmbeddedWithId {
//        @Id
//        private String id;
//    }
//
//    private static class EmbeddedWithoutFields {
//    }
}
