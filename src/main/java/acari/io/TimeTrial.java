package acari.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TimeTrial {
    private static final Logger logger = LoggerFactory.getLogger(TimeTrial.class);
    private final ApplicationContext applicationContext;
    private final ProgrammerRepository programmerRepository;

    @Autowired
    public TimeTrial(ApplicationContext applicationContext, ProgrammerRepository programmerRepository) {
        this.applicationContext = applicationContext;
        this.programmerRepository = programmerRepository;
    }

    @PostConstruct
    public void doWorkBruv(){
        logger.info("READY TO BLASTOFF");
//        SpringApplication.exit(applicationContext);
    }
}
