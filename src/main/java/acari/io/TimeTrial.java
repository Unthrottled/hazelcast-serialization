package acari.io;

import acari.io.pojo.DataSerializableProgrammer;
import acari.io.pojo.ExternalizableProgrammer;
import acari.io.pojo.IdentifiedDataSerializableProgrammer;
import acari.io.pojo.Programmer;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TimeTrial {
    private static final Logger logger = LoggerFactory.getLogger(TimeTrial.class);
    private final ProgrammerRepository programmerRepository;
    private final HazelcastServer hazelcastServer;

    @Autowired
    public TimeTrial(ProgrammerRepository programmerRepository, HazelcastServer hazelcastServer) {
        this.programmerRepository = programmerRepository;
        this.hazelcastServer = hazelcastServer;
    }

    @PostConstruct
    public void doWorkBruv() {
        logger.info("Time trials ready to start!");
        doTimeTrial(hazelcastServer.getHazelcastInstance().getMap("programmer"),
                programmerRepository.getProgrammers(), Programmer::getName, "Regular Serializable");

        doTimeTrial(hazelcastServer.getHazelcastInstance().getMap("programmer-ext"),
                programmerRepository.getProgrammers().map(ExternalizableProgrammer::new),
                ExternalizableProgrammer::getName, "Externalizable");

        doTimeTrial(hazelcastServer.getHazelcastInstance().getMap("programmer-ds"),
                programmerRepository.getProgrammers().map(DataSerializableProgrammer::new),
                DataSerializableProgrammer::getName, "Data Serializable");

        doTimeTrial(hazelcastServer.getHazelcastInstance().getMap("programmer-ids"),
                programmerRepository.getProgrammers().map(IdentifiedDataSerializableProgrammer::new),
                IdentifiedDataSerializableProgrammer::getName, "Identified Data Serializable");

        logger.info("Time trials finished!");
    }

    private <T> void doTimeTrial(IMap<String, T> programmer, Stream<T> programmers, Function<T, String> idFunct, String methodName) {
        Instant before = Instant.now();
        programmers.forEach(programmer1 -> programmer.set(idFunct.apply(programmer1), programmer1));
        Instant after = Instant.now();
        logger.info("Writing " + programmer.size() + " " + methodName + " arguments took " + getMillisBetween(before, after) + " milliseconds.");
        before = Instant.now();
        programmer.entrySet().parallelStream().collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue));
        after = Instant.now();
        logger.info("Reading " + programmer.size() + " " + methodName + " arguments took " + getMillisBetween(before, after) + " milliseconds.");
        programmer.clear();
    }

    private long getMillisBetween(Instant before, Instant after) {
        return Duration.between(before, after).toMillis();
    }
}
