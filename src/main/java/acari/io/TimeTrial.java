package acari.io;

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
import java.util.stream.Collectors;

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
        logger.info("READY TO BLASTOFF");
        IMap<String, Programmer> programmerIMap = hazelcastServer.getHazelcastInstance().getMap("programmer");
        Instant before = Instant.now();
        programmerRepository.getProgrammers().forEach(programmer -> programmerIMap.set(programmer.getName(), programmer));
        Instant after = Instant.now();
        logger.info("Writing " + programmerIMap.size() + " arguments took " + Duration.between(before, after).toMillis() + " milliseconds.");
        before = Instant.now();
        Map<String, Programmer> values = programmerIMap.entrySet().parallelStream().collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue));
        after = Instant.now();
        logger.info("Reading " + programmerIMap.size() + " arguments took " + Duration.between(before, after).toMillis() + " milliseconds.");
        programmerIMap.clear();
    }
}
