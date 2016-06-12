package com.mechanitis;

import com.mechanitis.undertest.IterHelper;
import com.mongodb.BasicDBObject;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.Mapper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.BenchmarkParams;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO depends on number of values in the DB Object
@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class IterHelperBenchmark {
    @Param({"1", "10", "100", "1000", "10000", "100000"})
    public int numberOfItems;

    public BasicDBObject source;
    public final Mapper mapper = new Mapper();
    public MappedField mappedField;

    @Setup()
    public void setup(BenchmarkParams params) {
        source = new BasicDBObject(numberOfItems);
        mappedField = mapper.getMappedClass(new EntityContainingMap()).getMappedField("result");
        for (int i = 0; i < numberOfItems; i++) {
            source.put(String.valueOf(i), String.valueOf(i));
        }
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Map<String, Object> original() {
        final Map<String, Object> values = new HashMap<>();
        new IterHelper<String, Object>().loopMap(source, (key, val) -> {
            final MappedField mf = mappedField;
            values.put(key, val != null ? mapper.getConverters().decode(mf.getSubClass(), val, mf) : null);
        });

        return values;
        //8.219 ops/ms

    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Map<String, Object> simplified() {
        final Map<String, Object> values = new HashMap<>();
        IterHelper.<String, Object>loopMapSimplified(source, (key, val) -> {
            final MappedField mf = mappedField;
            values.put(key, val != null ? mapper.getConverters().decode(mf.getSubClass(), val, mf) : null);
        });

        return values;
        //9.299 ops/ms
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public Map<String, Object> refactored() {
        final Map<String, Object> values = new HashMap<>();
        source.forEach((key, val) -> {
            final MappedField mf = mappedField;
            values.put(key, val != null ? mapper.getConverters().decode(mf.getSubClass(), val, mf) : null);
        });

        return values;
        //9.331 ops/ms
    }

    // NOTES: we can get rid of the helper method entirely (but only if we know source is a BasicDBObject)

    @SuppressWarnings("unused") // fields used by Morphia
    private static class EntityContainingMap {
        @Id
        private final int id = 0;
        private final Map<String, Object> result = new HashMap<>();
    }

}
