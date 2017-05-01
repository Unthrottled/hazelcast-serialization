package acari.io;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HazelcastSerializationApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(HazelcastSerializationApplication.class, args);
		SpringApplication.exit(run);
	}
}
