package acari.io;

import acari.io.pojo.IdentifiedDataSerializableProgrammer;
import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Component
@Scope(value = SCOPE_SINGLETON)
public class HazelcastServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HazelcastServer.class);
    private HazelcastInstance hazelcastInstance;

    @PostConstruct
    public void setUp() {
        LOGGER.info("Starting up Hazelcast Server!");
        Config serverConfig = new ClasspathXmlConfig("hazelcast.xml");
        serverConfig.getSerializationConfig().addDataSerializableFactory(IdentifiedDataSerializableProgrammer.FACTORY_ID,
                i -> {
                    switch (i) {
                        case IdentifiedDataSerializableProgrammer.OBJECT_ID:
                            return new IdentifiedDataSerializableProgrammer();
                        default:
                            return null;
                    }});
        hazelcastInstance = Hazelcast.newHazelcastInstance(serverConfig);
        LOGGER.info("Hazlecast server started!");
    }

    @PreDestroy
    public void tearDown() {
        LOGGER.info("Shutting Down Hazelcast Server!");
        hazelcastInstance.shutdown();
        LOGGER.info("Hazelcast Server Shut Down!");
    }

    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }
}
