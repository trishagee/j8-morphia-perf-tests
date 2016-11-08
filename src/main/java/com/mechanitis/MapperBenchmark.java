package com.mechanitis;

import com.mechanitis.undertest.Mapper;
import com.mongodb.DBCollection;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.MappedField;
import org.openjdk.jmh.annotations.*;

import java.lang.annotation.Annotation;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1000, timeUnit = MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = MILLISECONDS)
public class MapperBenchmark {
    private MappedClass mappedClass;
    private MappedField mappedField;

    @Setup()
    public void setup() {
        mappedClass = new org.mongodb.morphia.mapping.Mapper().getMappedClass(new EntityWith10FieldsWithIds());
        mappedField = mappedClass.getMappedField("field1");

    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Class<? extends Annotation> original() {
        return Mapper.original(mappedField);
        //42432
        //
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Class<? extends Annotation> refactored() {
        return Mapper.refactored(mappedField);
        //9242
        //
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Class<? extends Annotation> refactoredMore() {
        return Mapper.refactoredMore(mappedField);
        //8765
        //
    }

    private static class EntityWith10FieldsWithIds {
        private EmbeddedWithAnnotation field1;
        private EmbeddedWithAnnotation field2;
        private EmbeddedWithAnnotation field3;
        private EmbeddedWithAnnotation field4;
        private EmbeddedWithAnnotation field5;
        private EmbeddedWithAnnotation field6;
        private EmbeddedWithAnnotation field7;
        private EmbeddedWithAnnotation field8;
        private EmbeddedWithAnnotation field9;
        private EmbeddedWithAnnotation field10;
    }

    private static class EmbeddedWithAnnotation {
        @Reference
        private String id;
    }

}
