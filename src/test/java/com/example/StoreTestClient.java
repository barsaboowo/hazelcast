package com.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Sam Barber on 3/15/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreTestClient {

    @Value("${map.name}")
    String mapName;
    private HazelcastInstance client;

    @Before
    public void setUp() throws Exception {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().addAddress("localhost:9072");

        client = HazelcastClient.newHazelcastClient(clientConfig);
    }

    @Test
    public void testMap() throws Exception {
        IMap<String, User> map = client.getMap(mapName);

        map.loadAll(true);
        map.put("id2", new User("id2", "Sam2", "email2"));
        map.flush();

    }
}