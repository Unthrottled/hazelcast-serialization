package io.acari;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.nio.serialization.DataSerializableFactory;
import io.acari.pojo.IdentifiedDataSerializableProgrammer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class HazelcastFactory {

    public static final DataSerializableFactory DATA_SERIALIZABLE_FACTORY = i -> {
        switch (i) {
            case IdentifiedDataSerializableProgrammer.OBJECT_ID:
                return new IdentifiedDataSerializableProgrammer();
            default:
                return null;
        }
    };

    /**
     * This creates a hazelcast config bean so that the auto-configured hazelcast instance
     * provided by Spring will have a data serializable factory.
     *
     * @return a hazelcast configuration bean with a data serializable factory for the programmer pojo.
     */
    @Bean
    public Config config() {
        Config serverConfig = new ClasspathXmlConfig("hazelcast.xml");
        serverConfig.getSerializationConfig().addDataSerializableFactory(IdentifiedDataSerializableProgrammer.FACTORY_ID,
                DATA_SERIALIZABLE_FACTORY);
        return serverConfig;
    }

    /**
     * Not taking advantage of hazelcast auto configure, and creating my own, because for the need for mutiple beans of the same type.
     *
     * @param config should be provided by the method above.
     * @return one hazelcast instance with a data serializable factory for the programmer pojo.
     */
    @Bean
    public HazelcastInstance serializationServer(Config config) {
        return Hazelcast.newHazelcastInstance(config);
    }

    /**
     * Creates a plain vanilla hazelcast server bean with no
     * data serializable factory. So that an example with a Hazelcast lite client
     * can provide one.
     *
     * @return a non-multicast enable default hazelcast instance
     */
    @Bean
    public HazelcastInstance hazelcastServer() {
        return Hazelcast.newHazelcastInstance(new ClasspathXmlConfig("hazelcast-server.xml"));
    }

    /**
     * Creates a client which will attach to the full fledged hazelcast instance
     * provided by the hazelcastServer bean.
     *
     * @param hazelcastServer needed so the server is created before the client.
     * @return a hazelcast instance with a DataSerializableFactory for our programmer.
     * @throws IOException
     */
    @Bean
    public HazelcastInstance hazelcastClient(@Qualifier("hazelcastServer") HazelcastInstance hazelcastServer) throws IOException {
        ClientConfig clientConfig = new XmlClientConfigBuilder("hazelcast-client.xml").build();
        clientConfig.getSerializationConfig().addDataSerializableFactory(IdentifiedDataSerializableProgrammer.FACTORY_ID,
                DATA_SERIALIZABLE_FACTORY);
        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
