package wind57.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
public class JMH_6_FixtureLevel {

    double x;

    /*
     * Fixture methods have different levels to control when they should be run.
     * There are at least three Levels available to the user. These are, from
     * top to bottom:
     *
     * Level.Trial: before or after the entire benchmark run (the sequence of iterations)
     * Level.Iteration: before or after the benchmark iteration (the sequence of invocations)
     * Level.Invocation; before or after the benchmark method invocation (WARNING: read the Javadoc before using)
     *
     * Time spent in fixture methods does not count into the performance
     * metrics, so you can use this to do some heavy-lifting.
     */

    @TearDown(Level.Invocation)
    public void check() {
        assert x > Math.PI : "Nothing changed?";
    }

    @Benchmark
    public void measureRight() {
        x++;
    }

    @Benchmark
    public void measureWrong() {
        double x = 0;
        x++;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMH_6_FixtureLevel.class.getSimpleName())
                .forks(1)
                .jvmArgs("-ea")
                .shouldFailOnError(false) // switch to "true" to fail the complete run
                .build();

        new Runner(opt).run();
    }

}
