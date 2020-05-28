package com.technologicgroup.boost.common;

import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
class Activator {

  @Getter
  static IgniteCluster cluster;

  @Getter
  static ApplicationContext context;

  private static final Object locker = new Object();

  public Activator(IgniteCluster cluster, ApplicationContext context) {
    synchronized (locker) {
      Activator.cluster = cluster;
      Activator.context = context;
    }
  }

  public static boolean isReady() {
    synchronized (locker) {
      return cluster != null && context != null;
    }
  }
}
