package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.Cluster;

import org.apache.ignite.Ignite;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;

@Configuration
@ComponentScan(basePackageClasses = {Activator.class})
public class IgniteRepositoryConfig {

  @Bean
  public Cluster igniteCluster(Ignite ignite, CommonRepository<?, ?>[] repositories) {
    for (CommonRepository<?, ?> repository : repositories) {
      CacheConfiguration<?, ?> configuration = repository.getConfiguration();
      ignite.getOrCreateCache(configuration);
    }

    IgniteCluster igniteCluster = new IgniteCluster(ignite);
    CompletableFuture.runAsync(() -> startActivation(igniteCluster));

    return igniteCluster;
  }

  @SuppressWarnings("BusyWait")
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
