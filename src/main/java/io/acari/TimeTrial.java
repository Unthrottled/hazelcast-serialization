package io.acari;

import io.acari.pojo.DataSerializableProgrammer;
import io.acari.pojo.ExternalizableProgrammer;
import io.acari.pojo.IdentifiedDataSerializableProgrammer;
import io.acari.pojo.Programmer;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

@Component
public class TimeTrial {
    public static final int NUMBER_TRIALS = 10;
    private static final Logger logger = LoggerFactory.getLogger(TimeTrial.class);
    private final ProgrammerRepository programmerRepository;

    //Fun fact, Spring will auto-create a Hazelcast instance
    //If it detects Hazelcast on the classpath! It can be created
    //using a custom config, in the form of a bean! Neat!!
    private final HazelcastInstance serializationServer;
    private final HazelcastInstance hazelcastClient;

    @Autowired
    public TimeTrial(ProgrammerRepository programmerRepository,
                     @Qualifier("serializationServer")
                     HazelcastInstance hazelcastInstance,
                     @Qualifier("hazelcastClient")
                     HazelcastInstance hazelcastClient) {
        this.programmerRepository = programmerRepository;
        this.serializationServer = hazelcastInstance;
        this.hazelcastClient = hazelcastClient;
    }

    @PreDestroy
    public void onlyDreams() {
        logger.info("Shutting Down Hazelcast");
        serializationServer.shutdown();
        logger.info("Hazelcast shutdown.");
    }

    @PostConstruct
    public void doWorkBruv() {
        logger.info("Time trials ready for Hazelcast server ready to start!");
        runTimeTrialsForInstance(serializationServer);
        logger.info("Time trials for Hazelcast server finished!");

        logger.info("Time trials ready for Hazelcast client ready to start!");
        runTimeTrialsForInstance(serializationServer);
        logger.info("Time trials for Hazelcast client finished!");
    }

    private void runTimeTrialsForInstance(HazelcastInstance aHazelcastInstance) {
        doTimeTrial(aHazelcastInstance.getMap("programmer"),
                programmerRepository::getProgrammers, Programmer::getName, "Regular Serializable");

        doTimeTrial(aHazelcastInstance.getMap("programmer-ext"),
                () -> programmerRepository.getProgrammers().map(ExternalizableProgrammer::new),
                ExternalizableProgrammer::getName, "Externalizable");

        doTimeTrial(aHazelcastInstance.getMap("programmer-ds"),
                () -> programmerRepository.getProgrammers().map(DataSerializableProgrammer::new),
                DataSerializableProgrammer::getName, "Data Serializable");

        doTimeTrial(aHazelcastInstance.getMap("programmer-ids"),
                () -> programmerRepository.getProgrammers().map(IdentifiedDataSerializableProgrammer::new),
                IdentifiedDataSerializableProgrammer::getName, "Identified Data Serializable");
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
        logger.info("Writing {} {}  arguments {} times took an average of {} milliseconds.", ProgrammerRepository.NUM_PROGRAMMERS, methodName, NUMBER_TRIALS, writeTimes.build().average().orElseThrow(IllegalStateException::new));
        logger.info("Reading {} {}  arguments {} times took an average of {} milliseconds.", ProgrammerRepository.NUM_PROGRAMMERS, methodName, NUMBER_TRIALS, readTimes.build().average().orElseThrow(IllegalStateException::new));
    }

    private long getMillisBetween(Instant before, Instant after) {
        return Duration.between(before, after).toMillis();
    }
}
