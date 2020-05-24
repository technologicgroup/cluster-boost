package com.technologicgroup.core.ignite;

import com.technologicgroup.gridgain.core.Cluster;

import org.apache.ignite.Ignite;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.concurrent.CompletableFuture;

@Configuration
@ComponentScan(basePackageClasses = {Activator.class})
public class IgniteRepositoryConfig {

  @Bean
  public Cluster igniteCluster(@Lazy Ignite ignite, IgniteRepository<?, ?>[] repositories) {
    for (IgniteRepository<?, ?> repository : repositories) {
      CacheConfiguration<?, ?> configuration = repository.getConfiguration();
      ignite.getOrCreateCache(configuration);
      repository.setIgnite(ignite);
    }

    IgniteCluster igniteCluster = new IgniteCluster(ignite);
    CompletableFuture.runAsync(() -> startActivation(igniteCluster));

    return igniteCluster;
  }

  private void startActivation(IgniteCluster cluster) {
    while (!cluster.isActivated()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    cluster.setIsReady();
  }


}
