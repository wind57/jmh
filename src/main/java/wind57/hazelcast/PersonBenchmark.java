package wind57.hazelcast;

import com.hazelcast.config.SerializationConfig;
import com.hazelcast.internal.serialization.Data;
import com.hazelcast.internal.serialization.impl.DefaultSerializationServiceBuilder;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import com.hazelcast.internal.serialization.SerializationService;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;

public class PersonBenchmark {

    @State(Scope.Thread)
    public static class PersonCreator {

        private static final RandomGenerator RANDOM = RandomGenerator.getDefault();

        public Person person;

        @Setup
        public void person() {
            String name = RANDOM.nextInt(1, 100) + "";
            String surname = RANDOM.nextInt(1, 100) + "";
            int age = RANDOM.nextInt(1, 50);
            person = new Person(name, surname, age);
        }

        @TearDown
        public void tearDown() {
            if (person.getAge() == 0 || person.getName() == null || person.getSurname() == null) {
                throw new RuntimeException("ha?");
            }
        }

    }

    @State(Scope.Benchmark)
    public static class CompactSerializationServiceCreator {

        SerializationService serializationService;

        @Setup
        public void serializationService() {

            SerializationConfig serializationConfig = new SerializationConfig();
            serializationConfig.getCompactSerializationConfig()
                    .addSerializer(new PersonCompactSerializer());

            serializationService = new DefaultSerializationServiceBuilder()
                    .setSchemaService(new InMemorySchemaService())
                    .setConfig(serializationConfig).build();
        }

    }

    @State(Scope.Benchmark)
    public static class ZeroConfigSerializationServiceCreator {

        public SerializationService serializationService;

        @Setup
        public void serializationService() {
            SerializationConfig serializationConfig = new SerializationConfig();
            serializationService = new DefaultSerializationServiceBuilder()
                    .setSchemaService(new InMemorySchemaService())
                    .setConfig(serializationConfig).build();
        }

    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Warmup(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
    public Data compactSerialize(PersonCreator personCreator,
            CompactSerializationServiceCreator compactSerializationServiceCreator) {

        return compactSerializationServiceCreator.serializationService.toData(personCreator.person);

    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Warmup(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
    public Data zeroConfigSerializer(PersonCreator personCreator,
            ZeroConfigSerializationServiceCreator zeroConfigSerializationServiceCreator) {

        return zeroConfigSerializationServiceCreator.serializationService.toData(personCreator.person);

    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Warmup(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
    public Object compactDeserialize(PersonCreator personCreator,
            CompactSerializationServiceCreator compactSerializationServiceCreator) {

        Data data = compactSerializationServiceCreator.serializationService.toData(personCreator.person);
        return compactSerializationServiceCreator.serializationService.toObject(data);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Warmup(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
    public Object zeroConfigDeserializer(PersonCreator personCreator,
            ZeroConfigSerializationServiceCreator zeroConfigSerializationServiceCreator) {

        Data data = zeroConfigSerializationServiceCreator.serializationService.toData(personCreator.person);
        return zeroConfigSerializationServiceCreator.serializationService.toObject(data);

    }

}
