package acari.io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TimeTrial {
    private final ApplicationContext applicationContext;
    private final ProgrammerRepository programmerRepository;

    @Autowired
    public TimeTrial(ApplicationContext applicationContext, ProgrammerRepository programmerRepository) {
        this.applicationContext = applicationContext;
        this.programmerRepository = programmerRepository;
    }

    @PostConstruct
    public void doWorkBruv(){
//        SpringApplication.exit(applicationContext);
    }
}
