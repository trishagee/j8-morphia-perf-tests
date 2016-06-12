package com.mechanitis;

import com.mechanitis.undertest.MappedClass;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.Mapper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.BenchmarkParams;

import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO depends on number of fields in the entity
@State(Scope.Benchmark)
public class MappedClassBenchmark {
    private final Mapper mapper = new Mapper();
    private MappedClass mappedClass;

    @Setup()
    public void setup(BenchmarkParams params) {
        mappedClass = new MappedClass();
        mappedClass.addField(mapper.getMappedClass(new Entity()).getMappedField("field1"));
        mappedClass.addField(mapper.getMappedClass(new Entity()).getMappedField("field2"));
        mappedClass.addField(mapper.getMappedClass(new Entity()).getMappedField("field3"));
        mappedClass.addField(mapper.getMappedClass(new Entity()).getMappedField("field4"));
        mappedClass.addField(mapper.getMappedClass(new Entity()).getMappedField("field5"));
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public List<MappedField> original() {
        return mappedClass.getFieldsAnnotatedWithOriginal(Id.class);
        //24764.287 ops/ms
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public List<MappedField> refactored() {
        return mappedClass.getFieldsAnnotatedWithRefactored(Id.class);
        // 9640.649 ops/ms
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
    }

}
