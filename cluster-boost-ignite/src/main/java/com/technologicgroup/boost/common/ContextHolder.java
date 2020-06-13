package com.technologicgroup.boost.common;

import lombok.Getter;
import org.apache.ignite.Ignite;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * Static object that hold a link to Spring Application Context and to the Cluster object
 */
@Configuration
public class ContextHolder {

  @Getter
  static IgniteCluster cluster;

  @Getter
  static ApplicationContext context;

  private static final Object locker = new Object();

  public ContextHolder(IgniteCluster cluster, ApplicationContext context) {
    synchronized (locker) {
      ContextHolder.cluster = cluster;
      ContextHolder.context = context;
    }
  }

  /**
   * Indicates if application context and Cluster is not null and can be used
   * This does not mean that all nodes from the cluster are ready and Cluster is ready for tasks
   * @return true if cluster and application context are not null and false otherwise
   */
  public static boolean isReady() {
    synchronized (locker) {
      return cluster != null && context != null &&
          Optional.of(cluster)
              .map(c -> c.ignite)
              .map(Ignite::cluster)
              .map(org.apache.ignite.IgniteCluster::active)
              .orElse(false);
    }
  }
}
