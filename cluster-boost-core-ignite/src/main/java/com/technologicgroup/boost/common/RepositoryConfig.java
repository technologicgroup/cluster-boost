package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.Cluster;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = { Activator.class, IgniteConfig.class })
public class RepositoryConfig {

  @Value("${cluster.hosts}")
  private String[] hosts;

  @Bean
  public Cluster igniteCluster(Ignite ignite, CommonRepository<?, ?>[] repositories) {
    for (CommonRepository<?, ?> repository : repositories) {
      CacheConfiguration<?, ?> configuration = repository.getConfiguration();
      ignite.getOrCreateCache(configuration);
    }

    IgniteCluster igniteCluster = new IgniteCluster(ignite, hosts);
    CompletableFuture.runAsync(igniteCluster::activate);

    return igniteCluster;
  }
}
