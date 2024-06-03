package wind57.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.infra.Control;

import java.util.concurrent.atomic.AtomicBoolean;

public class JMH_18_Control {

//    public final AtomicBoolean flag = new AtomicBoolean();
//
//    @Benchmark
//    @Group("pingpong")
//    public void ping(Control cnt) {
//        while (!cnt.stopMeasurement && !flag.compareAndSet(false, true)) {
//            // this body is intentionally left blank
//        }
//    }
//
//    @Benchmark
//    @Group("pingpong")
//    public void pong(Control cnt) {
//        while (!cnt.stopMeasurement && !flag.compareAndSet(true, false)) {
//            // this body is intentionally left blank
//        }
//    }

}
