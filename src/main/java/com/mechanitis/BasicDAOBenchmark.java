package com.mechanitis;

import com.mechanitis.undertest.BasicDAO;
import org.mongodb.morphia.Key;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class BasicDAOBenchmark {
    @Param({"1", "10", "100", "1000", "10000", "100000"})
    public int numberOfItems;

    private final List<Key> keys = new ArrayList<>(numberOfItems);
    private final BasicDAO basicDAO = new BasicDAO();

    @Setup()
    public void setup() {
        for (int i = 0; i < numberOfItems; i++) {
            keys.add(new Key(String.class, "Test Collection", i));
        }
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public List originalIterationCode() {
        return basicDAO.keysToIdsOriginal(keys);

//        131.242 ops/ms
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public List simplifiedIterationCode() {
        return basicDAO.keysToIdsSimplified(keys);
//85.574 ops/ms

    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public List refactoredCode() {
        return basicDAO.keysToIdsRefactored(keys);
// 85.045 ops/ms

    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public List parallelCode() {
        return basicDAO.keysToIdsParallel(keys);
//

    }
}
