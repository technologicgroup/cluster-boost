package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.Cluster;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = {Activator.class})
public class RepositoryConfig {

  @Value("${cluster.hosts}")
  private String[] hosts;

  @Bean
  public Cluster igniteCluster(Ignite ignite, CommonRepository<?, ?>[] repositories) {
    for (CommonRepository<?, ?> repository : repositories) {
      CacheConfiguration<?, ?> configuration = repository.getConfiguration();
      ignite.getOrCreateCache(configuration);
    }

    TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
    ipFinder.setAddresses(Arrays.asList(hosts));

    TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
    discoverySpi.setIpFinder(ipFinder);

    ignite.configuration().setDiscoverySpi(discoverySpi);
    ignite.cluster().restartNodes();

    IgniteCluster igniteCluster = new IgniteCluster(ignite, hosts);
    CompletableFuture.runAsync(igniteCluster::startActivation);

    return igniteCluster;
  }
}
