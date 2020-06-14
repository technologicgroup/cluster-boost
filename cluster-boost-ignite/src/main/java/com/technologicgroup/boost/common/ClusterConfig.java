package com.technologicgroup.boost.common;

import com.technologicgroup.boost.chain.ChainConfig;
import com.technologicgroup.boost.common.providers.BeanProviderFactory;
import com.technologicgroup.boost.common.providers.ProvidersConfig;
import com.technologicgroup.boost.core.Cluster;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * Ignite cluster configuration
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackageClasses = {
    ContextHolder.class,
    ClusterProperties.class,
    ChainConfig.class,
    ProvidersConfig.class
})
public class ClusterConfig {

  private final ClusterProperties clusterProperties;

  /**
   * Configures Ignite bean
   * @param igniteConfiguration is the ignite configuration (can be null)
   * @return Ignite bean
   */
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

  /**
   * Creates Cluster bean for Ignite
   * @param ignite is Ignite bean
   * @param repositories is a list of available cluster repositories
   * @param beanProviderFactory is a bean provider factory bean, using for running beans on cluster
   * @return a Cluster bean
   */
  @Bean
  public Cluster igniteCluster(Ignite ignite,
                               @Autowired(required = false) CommonRepository<?, ?>[] repositories,
                               BeanProviderFactory beanProviderFactory) {
    if (repositories != null) {
      for (CommonRepository<?, ?> repository : repositories) {
        CacheConfiguration<?, ?> configuration = repository.getConfiguration();
        ignite.getOrCreateCache(configuration);
      }
    }

    IgniteCluster igniteCluster = new IgniteCluster(ignite,
            clusterProperties.getHosts(),
            clusterProperties.getStartupTimeout(),
            beanProviderFactory);

    CompletableFuture.runAsync(igniteCluster::activate);

    return igniteCluster;
  }
}
