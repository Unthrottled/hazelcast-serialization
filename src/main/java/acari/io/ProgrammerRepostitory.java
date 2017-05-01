package acari.io;

import acari.io.pojo.Programmer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ProgrammerRepostitory {
    private static final int THREADS = 16;
    private final SecureRandom secureRandom = new SecureRandom();
    private final Random ranbo = new Random(9001);
    private final ComputerRepository computerRepository;
    private Collection<Programmer> programmerCollection;
    private ExecutorService executorService = Executors.newFixedThreadPool(THREADS);

    @Autowired
    public ProgrammerRepostitory(ComputerRepository computerRepository) {
        this.computerRepository = computerRepository;
    }

    @PostConstruct
    public void setUp() {
        Supplier<Integer> integerSupplier = () -> 1;
        List<Future<List<Programmer>>> futureProgrammers = Stream.generate(integerSupplier)
                .limit(THREADS)
                .map(i -> executorService.submit(() -> Stream.generate(integerSupplier)
                        .limit(100000)
                        .map(j -> new Programmer(new BigInteger(2048, secureRandom).toString(32),
                                ranbo.nextInt(64), computerRepository.randomComputer(), new ArrayList<>()))
                        .collect(Collectors.toList()))).collect(Collectors.toList());
    }

}
