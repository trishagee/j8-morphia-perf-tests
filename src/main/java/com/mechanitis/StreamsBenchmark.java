package com.mechanitis;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.converters.MapOfValuesConverter;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.Mapper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StreamsBenchmark {
    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Object decodeOriginal(final BenchmarkState state) {
        return state.mapOfValuesConverter.decode(null, state.source, state.mappedField);
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private int numberOfValues = 1000;
        private final MapOfValuesConverter mapOfValuesConverter = new MapOfValuesConverter();
        private final Mapper mapper = new Mapper();
        private final DBObject source = new BasicDBObject(numberOfValues);
        private final MappedField mappedField;

        public BenchmarkState() {
            mapOfValuesConverter.setMapper(mapper);
            mappedField = mapper.getMappedClass(new EntityToMap()).getMappedField("result");
            for (int i = 0; i < numberOfValues; i++) {
                source.put(String.valueOf(i), String.valueOf(i));
            }
        }
    }

    private static class EntityToMap {
        @Id
        private final int id = 0;
        private final Map<String, Object> result = new HashMap<>();
    }

//    public static void main(String[] args) throws RunnerException {
//        Options opt = new OptionsBuilder()
//                .include(StreamsBenchmark.class.getSimpleName())
//                .timeUnit(TimeUnit.MILLISECONDS)
//                .build();
//
//        new Runner(opt).run();
//
//    }
}
