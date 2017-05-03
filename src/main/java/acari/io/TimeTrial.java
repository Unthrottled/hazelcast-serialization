package acari.io;

import acari.io.pojo.DataSerializableProgrammer;
import acari.io.pojo.ExternalizableProgrammer;
import acari.io.pojo.IdentifiedDataSerializableProgrammer;
import acari.io.pojo.Programmer;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static acari.io.ProgrammerRepository.NUM_PROGRAMMERS;

@Component
public class TimeTrial {
    public static final int NUMBER_TRIALS = 10;
    private static final Logger logger = LoggerFactory.getLogger(TimeTrial.class);
    private final ProgrammerRepository programmerRepository;

    //Fun fact, Spring will auto-create a Hazelcast instance
    //If it detects Hazelcast on the classpath! It can be created
    //using a custom config, in the form of a bean! Neat!!
    private final HazelcastInstance hazelcastInstance;

    @Autowired
    public TimeTrial(ProgrammerRepository programmerRepository, HazelcastInstance hazelcastInstance) {
        this.programmerRepository = programmerRepository;
        this.hazelcastInstance = hazelcastInstance;
    }

    @PreDestroy
    public void onlyDreams() {
        logger.info("Shutting Down Hazelcast");
        hazelcastInstance.shutdown();
        logger.info("Hazelcast shutdown.");
    }

    @PostConstruct
    public void doWorkBruv() {
        logger.info("Time trials ready to start!");
        doTimeTrial(hazelcastInstance.getMap("programmer"),
                programmerRepository::getProgrammers, Programmer::getName, "Regular Serializable");

        doTimeTrial(hazelcastInstance.getMap("programmer-ext"),
                () -> programmerRepository.getProgrammers().map(ExternalizableProgrammer::new),
                ExternalizableProgrammer::getName, "Externalizable");

        doTimeTrial(hazelcastInstance.getMap("programmer-ds"),
                () -> programmerRepository.getProgrammers().map(DataSerializableProgrammer::new),
                DataSerializableProgrammer::getName, "Data Serializable");

        doTimeTrial(hazelcastInstance.getMap("programmer-ids"),
                () -> programmerRepository.getProgrammers().map(IdentifiedDataSerializableProgrammer::new),
                IdentifiedDataSerializableProgrammer::getName, "Identified Data Serializable");

        logger.info("Time trials finished!");
    }

    private <T> void doTimeTrial(IMap<String, T> programmer, Supplier<Stream<T>> programmers, Function<T, String> idFunct, String methodName) {
        LongStream.Builder writeTimes = LongStream.builder();
        LongStream.Builder readTimes = LongStream.builder();
        for (int i = 0; i < NUMBER_TRIALS; i++) {
            Instant before = Instant.now();
            programmers.get().forEach(programmer1 -> programmer.set(idFunct.apply(programmer1), programmer1));
            Instant after = Instant.now();
            writeTimes.accept(getMillisBetween(before, after));

            before = Instant.now();
            programmer.entrySet().parallelStream().collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue));
            after = Instant.now();
            readTimes.accept(getMillisBetween(before, after));
            programmer.clear();
        }
        logger.info("Writing {} {}  arguments {} times took an average of {} milliseconds.", NUM_PROGRAMMERS, methodName, NUMBER_TRIALS, writeTimes.build().average().orElseThrow(IllegalStateException::new));
        logger.info("Reading {} {}  arguments {} times took an average of {} milliseconds.", NUM_PROGRAMMERS, methodName, NUMBER_TRIALS, readTimes.build().average().orElseThrow(IllegalStateException::new));
    }

    private long getMillisBetween(Instant before, Instant after) {
        return Duration.between(before, after).toMillis();
    }
}
