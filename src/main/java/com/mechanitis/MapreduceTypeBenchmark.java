package com.mechanitis;

import com.mechanitis.undertest.MapreduceType;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1000, timeUnit = MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = MILLISECONDS)
public class MapreduceTypeBenchmark {

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public MapreduceType original() {
        return MapreduceType.original("MERGE");
        //27721
        //
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public MapreduceType refactored() {
        return MapreduceType.refactored("MERGE");
        //8168
        //
    }

}
