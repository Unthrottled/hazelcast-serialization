package acari.io;

import acari.io.pojo.DataSerializableProgrammer;
import acari.io.pojo.ExternalizableProgrammer;
import acari.io.pojo.IdentifiedDataSerializableProgrammer;
import acari.io.pojo.Programmer;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.serialization.DataSerializable;
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
        IMap<String, Programmer> programmer = hazelcastServer.getHazelcastInstance().getMap("programmer");
        Instant before = Instant.now();
        programmerRepository.getProgrammers().forEach(programmer1 -> programmer.set(programmer1.getName(), programmer1));
        Instant after = Instant.now();
        logger.info("Writing " + programmer.size() + " " + "Regular Serializable" + " arguments took " + Duration.between(before, after).toMillis() + " milliseconds.");
        before = Instant.now();
        programmer.entrySet().parallelStream().collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue));
        after = Instant.now();
        logger.info("Reading " + programmer.size() + " " + "Regular Serializable" +" arguments took " + Duration.between(before, after).toMillis() + " milliseconds.");
        programmer.clear();

        IMap<String, ExternalizableProgrammer> programmerExt = hazelcastServer.getHazelcastInstance().getMap("programmer-ext");
        before = Instant.now();
        programmerRepository.getProgrammers().map(ExternalizableProgrammer::new).forEach(programmer1 -> programmerExt.set(programmer1.getName(), programmer1));
        after = Instant.now();
        logger.info("Writing " + programmerExt.size() + " " + "Externalizable" + " arguments took " + Duration.between(before, after).toMillis() + " milliseconds.");
        before = Instant.now();
        programmerExt.entrySet().parallelStream().collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue));
        after = Instant.now();
        logger.info("Reading " + programmerExt.size() + " " + "Externalizable" +" arguments took " + Duration.between(before, after).toMillis() + " milliseconds.");
        programmerExt.clear();

        IMap<String, DataSerializableProgrammer> programmerDS = hazelcastServer.getHazelcastInstance().getMap("programmer-ds");
        before = Instant.now();
        programmerRepository.getProgrammers().map(DataSerializableProgrammer::new).forEach(programmer1 -> programmerDS.set(programmer1.getName(), programmer1));
        after = Instant.now();
        logger.info("Writing " + programmerDS.size() + " " + "Data Serializable" + " arguments took " + Duration.between(before, after).toMillis() + " milliseconds.");
        before = Instant.now();
        programmerDS.entrySet().parallelStream().collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue));
        after = Instant.now();
        logger.info("Reading " + programmerDS.size() + " " + "Data Serializable" +" arguments took " + Duration.between(before, after).toMillis() + " milliseconds.");
        programmerDS.clear();

        IMap<String, IdentifiedDataSerializableProgrammer> programmerIDS = hazelcastServer.getHazelcastInstance().getMap("programmer-ids");
        before = Instant.now();
        programmerRepository.getProgrammers().map(IdentifiedDataSerializableProgrammer::new).forEach(programmer1 -> programmerIDS.set(programmer1.getName(), programmer1));
        after = Instant.now();
        logger.info("Writing " + programmerIDS.size() + " " + "Identified Data Serializable" + " arguments took " + Duration.between(before, after).toMillis() + " milliseconds.");
        before = Instant.now();
        programmerIDS.entrySet().parallelStream().collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue));
        after = Instant.now();
        logger.info("Reading " + programmerIDS.size() + " " + "Identified Data Serializable" +" arguments took " + Duration.between(before, after).toMillis() + " milliseconds.");
        programmerIDS.clear();
    }

}
