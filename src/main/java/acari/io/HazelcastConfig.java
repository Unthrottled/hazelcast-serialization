package acari.io;

import acari.io.pojo.IdentifiedDataSerializableProgrammer;
import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    public Config config() {
        Config serverConfig = new ClasspathXmlConfig("hazelcast.xml");
        serverConfig.getSerializationConfig().addDataSerializableFactory(IdentifiedDataSerializableProgrammer.FACTORY_ID,
                i -> {
                    switch (i) {
                        case IdentifiedDataSerializableProgrammer.OBJECT_ID:
                            return new IdentifiedDataSerializableProgrammer();
                        default:
                            return null;
                    }
                });
        return serverConfig;
    }
}
