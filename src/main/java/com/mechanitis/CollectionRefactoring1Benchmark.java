package com.mechanitis;

import com.mechanitis.undertest.BasicDAO;
import org.mongodb.morphia.Key;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.BenchmarkParams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

//TODO: is there a way to run the same test for 10, 100, 1000, 10_000, 100_000 entries?
@State(Scope.Benchmark)
public class CollectionRefactoring1Benchmark {
    @Param({"1", "10", "100", "1000", "10000", "100000"})
    public int numberOfItems;

    private final List<Key> keys = new ArrayList<>(numberOfItems);
    private final BasicDAO basicDAO = new BasicDAO();

    @Setup()
    public void setup(BenchmarkParams params) {
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
}
