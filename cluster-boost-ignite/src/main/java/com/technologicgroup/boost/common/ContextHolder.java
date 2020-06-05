package com.technologicgroup.boost.common;

import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

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

  public static boolean isReady() {
    synchronized (locker) {
      return cluster != null && context != null;
    }
  }
}
