package org.example;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3 ,time = 300, timeUnit = TimeUnit.MILLISECONDS)
//@Measurement(iterations = 3, time = 500, timeUnit = TimeUnit.MILLISECONDS)
public class CountingBloomFilterBenchmark {
    private CountingBloomFilter<String> bloomFilter;
    private static final String ELEMENT = "example";

    @Param({"10", "100", "1000"})
    private int totalCount;

    @Param({"0.01", "0.001"})
    private double errorProbability;

    private static final Logger log = LoggerFactory.getLogger(CountingBloomFilterBenchmark.class);

    public static void main(String[]args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(CountingBloomFilterBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void setUp() {
        bloomFilter = new CountingBloomFilterImpl<>(totalCount, errorProbability);
    }

    @Benchmark
    public void testAdd(Blackhole bh) {
        for (int i = 0; i < 100; i++) {
            bloomFilter.add(ELEMENT + i);
        }
        bh.consume(bloomFilter);
    }

    @Benchmark
    public void testContains(Blackhole bh) {
        for (int i = 0; i < 100; i++) {
            bh.consume(bloomFilter.contains(ELEMENT + i));
        }
    }

    @Benchmark
    public void testRemove(Blackhole bh) {
        for (int i = 0; i < 100; i++) {
            bloomFilter.add(ELEMENT + i);
        }
        for (int i = 0; i < 100; i++) {
            bloomFilter.remove(ELEMENT + i);
        }
        bh.consume(bloomFilter);
    }
}
