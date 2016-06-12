package com.mechanitis;

import com.mechanitis.undertest.IterHelper;
import com.mongodb.BasicDBObject;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.Mapper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.HashMap;
import java.util.Map;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO depends on number of values in the DB Object
public class CollectionRefactoring7Benchmark {
    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Map<String, Object> original(final BenchmarkState state) {
        final Map<String, Object> values = new HashMap<>();
        new IterHelper<String, Object>().loopMap(state.source, (key, val) -> {
            final MappedField mf = state.mappedField;
            values.put(key, val != null ? state.mapper.getConverters().decode(mf.getSubClass(), val, mf) : null);
        });

        return values;
        //8.219 ops/ms

    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Map<String, Object> simplified(final BenchmarkState state) {
        final Map<String, Object> values = new HashMap<>();
        IterHelper.<String, Object>loopMapSimplified(state.source, (key, val) -> {
            final MappedField mf = state.mappedField;
            values.put(key, val != null ? state.mapper.getConverters().decode(mf.getSubClass(), val, mf) : null);
        });

        return values;
        //9.299 ops/ms
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Map<String, Object> refactored(final BenchmarkState state) {
        final Map<String, Object> values = new HashMap<>();
        state.source.forEach((key, val) -> {
            final MappedField mf = state.mappedField;
            values.put(key, val != null ? state.mapper.getConverters().decode(mf.getSubClass(), val, mf) : null);
        });

        return values;
        //9.331 ops/ms
    }

    // NOTES: we can get rid of the helper method entirely (but only if we know source is a BasicDBObject)

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private int numberOfValues = 1000;
        private final Mapper mapper = new Mapper();
        private final BasicDBObject source = new BasicDBObject(numberOfValues);
        private final MappedField mappedField;

        public BenchmarkState() {
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
