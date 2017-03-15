package com.example;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MapStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
public class HazelcastCachingMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HazelcastCachingMicroserviceApplication.class, args);
	}

    @Value("${hazelcast.port}")
    int port;

    @Value("${map.name}")
    String mapName;

    @Configuration
    @EnableCassandraRepositories(basePackages = {"com.example.repositories"})
    static class CassandraConfiguration extends AbstractCassandraConfiguration {

        @Value("${cassandra.port}")
        private int port;

        @Value("${cassandra.keyspace}")
        private String keyspace;

        @Value("${cassandra.hostname}")
        private String hostName;

        @Override
        protected String getKeyspaceName() {
            return keyspace;
        }

        @Override
        protected int getPort() {
            return port;
        }

        @Override
        protected String getContactPoints() {
            return hostName;
        }

		@Bean
		public CassandraClusterFactoryBean cluster() {
			CassandraClusterFactoryBean cluster =
					new CassandraClusterFactoryBean();
			cluster.setContactPoints(hostName);
			cluster.setPort(port);
			return cluster;
		}

		@Bean
		public CassandraMappingContext cassandraMapping()
				throws ClassNotFoundException {
			return new BasicCassandraMappingContext();
		}
    }

	@Bean
	public HazelcastInstance getHazelcastInstance(MapStore mapStore) {
		final Config config = new Config();

		final NetworkConfig networkConfig = new NetworkConfig();
		networkConfig.setPort(port);
		networkConfig.setPortAutoIncrement(true);
		config.setNetworkConfig(networkConfig);

		final JoinConfig joinConfig = new JoinConfig();
		networkConfig.setJoin(joinConfig);

		final MulticastConfig multicastConfig = new MulticastConfig();
		multicastConfig.setEnabled(false);
		joinConfig.setMulticastConfig(multicastConfig);

		final TcpIpConfig tcpIpConfig = new TcpIpConfig();
		tcpIpConfig.setEnabled(false);
		joinConfig.setTcpIpConfig(tcpIpConfig);

		final AwsConfig awsConfig = new AwsConfig();
		awsConfig.setEnabled(false);
		joinConfig.setAwsConfig(awsConfig);

		final SSLConfig sslConfig = new SSLConfig();
		sslConfig.setEnabled(false);
		networkConfig.setSSLConfig(sslConfig);

		final MapConfig mapConfig = new MapConfig();
		mapConfig.setName(mapName);

		final MapStoreConfig mapStoreConfig = new MapStoreConfig();
		mapStoreConfig.setEnabled(true);
		mapStoreConfig.setWriteDelaySeconds(60);
		mapStoreConfig.setInitialLoadMode(MapStoreConfig.InitialLoadMode.EAGER);
		mapStoreConfig.setImplementation(mapName);
		mapConfig.setMapStoreConfig(mapStoreConfig);
		config.addMapConfig(mapConfig);

		return Hazelcast.newHazelcastInstance(config);
	}
}
