package com.mechanitis;

import com.mechanitis.undertest.IterHelper;
import com.mongodb.BasicDBObject;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.Mapper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class StreamsBenchmark {
    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Map<String, Object> decodeOriginal(final BenchmarkState state) {
        final Map<String, Object> values = new HashMap<>();
        IterHelper.<String, Object>loopMapSimplified(state.source, (key, val) -> {
            final MappedField mf = state.mappedField;
            values.put(key, val != null ? state.mapper.getConverters().decode(mf.getSubClass(), val, mf) : null);
        });

        return values;
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Map<String, Object> decodeWithStreams(final BenchmarkState state) {
        final Map<String, Object> values = new HashMap<>();
        state.source.forEach((key, val) -> {
            final MappedField mf = state.mappedField;
            values.put(key, val != null ? state.mapper.getConverters().decode(mf.getSubClass(), val, mf) : null);
        });

        return values;
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
