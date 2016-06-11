package com.mechanitis;

import com.mechanitis.undertest.BasicDAO;
import org.mongodb.morphia.Key;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

//TODO: is there a way to run the same test for 10, 100, 1000, 10_000, 100_000 entries?
public class CollectionRefactoring1Benchmark {
    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public List originalIterationCode(final BenchmarkState state) {
        return state.basicDAO.keysToIdsOriginal(state.keys);

//        131.242 ops/ms
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public List simplifiedIterationCode(final BenchmarkState state) {
        return state.basicDAO.keysToIdsSimplified(state.keys);
//85.574 ops/ms

    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public List refactoredCode(final BenchmarkState state) {
        return state.basicDAO.keysToIdsRefactored(state.keys);
// 85.045 ops/ms

    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private final int numberOfValues = 1000;
        private final BasicDAO basicDAO;
        private final List<Key> keys = new ArrayList<>(numberOfValues);

        public BenchmarkState() {
            basicDAO = new BasicDAO();
            for (int i = 0; i < numberOfValues; i++) {
                keys.add(new Key(String.class, "Test Collection", i));
            }
        }
    }

}
