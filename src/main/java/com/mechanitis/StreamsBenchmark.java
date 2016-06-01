package com.mechanitis;

import com.mechanitis.undertest.IterHelper;
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

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class StreamsBenchmark {
    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Object decodeOriginal(final BenchmarkState state) {
        return decode(state.source, state.mappedField, state.mapper);
    }

    public Object decode(final Object fromDBObject, final MappedField mf, Mapper mapper) {
        if (fromDBObject == null) {
            return null;
        }

        final Map values = mapper.getOptions().getObjectFactory().createMap(mf);
        new IterHelper<>().loopMap(fromDBObject, (k, val) -> {
            final Object objKey = mapper.getConverters().decode(mf.getMapKeyClass(), k, mf);
            values.put(objKey, val != null ? mapper.getConverters().decode(mf.getSubClass(), val, mf) : null);
        });

        return values;
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
            mappedField = mapper.getMappedClass(new EntityContainingMap()).getMappedField("result");
            for (int i = 0; i < numberOfValues; i++) {
                source.put(String.valueOf(i), String.valueOf(i));
            }
        }
    }

    @SuppressWarnings("unused") // fields used by Morphia
    private static class EntityContainingMap {
        @Id
        private final int id = 0;
        private final Map<String, Object> result = new HashMap<>();
    }
}
