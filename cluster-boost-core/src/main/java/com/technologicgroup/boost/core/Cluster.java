package com.technologicgroup.boost.core;

import java.util.Collection;

public interface Cluster {

  <R> Collection<R> run(ClusterGroup clusterGroup, ClusterJob<R> job);
  <R> Collection<R> run(ClusterJob<R> job);

  void run(ClusterGroup clusterGroup, ClusterCall job);
  void run(ClusterCall job);

  void runAsync(ClusterGroup clusterGroup, ClusterCall job);
  void runAsync(ClusterCall job);

  <R extends Runnable> void runBeanAsync(ClusterGroup clusterGroup, Class<R> bean);
  <R extends Runnable> void runBeanAsync(Class<R> bean);

  <R extends Runnable> Collection<R> runBean(ClusterGroup clusterGroup, Class<R> bean);
  <R extends Runnable> Collection<R> runBean(Class<R> bean);

  boolean isActivated();

  void setOnClusterReadyListener(OnClusterReadyListener listener);

  void waitForReady() throws InterruptedException;
}
