package io.acari;

import io.acari.pojo.Programmer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ProgrammerRepository {
    private static final Logger logger = LoggerFactory.getLogger(ProgrammerRepository.class);
    public static final int PROGRAMMER_PER_THREAD = 10000;
    private static final int THREADS = 4;
    public static final int NUM_PROGRAMMERS = PROGRAMMER_PER_THREAD * THREADS;
    private final SecureRandom secureRandom = new SecureRandom();
    private final Random ranbo = new Random(9001);
    private final ComputerRepository computerRepository;
    private final LanguageRepository languageRepository;
    private final LinkedList<Programmer> programmerCollection = new LinkedList<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(THREADS);

    @Autowired
    public ProgrammerRepository(ComputerRepository computerRepository, LanguageRepository languageRepository) {
        this.computerRepository = computerRepository;
        this.languageRepository = languageRepository;
    }

    @PostConstruct
    public void setUp() {
        Supplier<Integer> integerSupplier = () -> 1;
        List<Future<List<Programmer>>> futureProgrammers = Stream.generate(integerSupplier)
                .limit(THREADS)
                .map(i -> executorService.submit(() -> Stream.generate(integerSupplier)
                        .limit(PROGRAMMER_PER_THREAD)
                        .map(j -> new Programmer(new BigInteger(128, secureRandom).toString(32),
                                ranbo.nextInt(64), computerRepository.randomComputer(), languageRepository.randomLanguages()))
                        .collect(Collectors.toList()))).collect(Collectors.toCollection(LinkedList::new));
        for (Future<List<Programmer>> futureProgrammer : futureProgrammers) {
            try {
                programmerCollection.addAll(futureProgrammer.get());
            } catch (InterruptedException | ExecutionException e) {
                logger.warn("Problem creating programmers.", e);
            }
        }
    }

    @PreDestroy
    public void shutDown(){
        executorService.shutdown();
    }

    public Stream<Programmer> getProgrammers() {
        return programmerCollection.parallelStream();
    }

}
