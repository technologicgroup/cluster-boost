package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.Cluster;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackageClasses = { Activator.class, ClusterProperties.class })
public class RepositoryConfig {

  private final ClusterProperties clusterProperties;

  @Bean
  public Ignite ignite(IgniteConfiguration igniteConfiguration) {

    TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
    ipFinder.setAddresses(Arrays.asList(clusterProperties.getHosts()));

    TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
    discoverySpi.setIpFinder(ipFinder);

    if (igniteConfiguration == null) {
      igniteConfiguration = new IgniteConfiguration();
    }

    igniteConfiguration.setDiscoverySpi(discoverySpi);
    return Ignition.start(igniteConfiguration);
  }

  @Bean
  public Cluster igniteCluster(Ignite ignite, CommonRepository<?, ?>[] repositories) {
    for (CommonRepository<?, ?> repository : repositories) {
      CacheConfiguration<?, ?> configuration = repository.getConfiguration();
      ignite.getOrCreateCache(configuration);
    }

    IgniteCluster igniteCluster = new IgniteCluster(ignite,
            clusterProperties.getHosts(),
            clusterProperties.getStartupTimeout());

    CompletableFuture.runAsync(igniteCluster::activate);

    return igniteCluster;
  }
}
