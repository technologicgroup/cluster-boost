package com.technologicgroup.boost.core;

public interface Cluster {

  <R> R run(ClusterGroup clusterGroup, ClusterJob<R> job);
  void run(ClusterGroup clusterGroup, ClusterCall job);

  <R> R run(ClusterJob<R> job);
  void run(ClusterCall job);

  void runAsync(ClusterGroup clusterGroup, ClusterCall job);
  void runAsync(ClusterCall job);

  <R extends Runnable> void runBean(ClusterGroup clusterGroup, Class<R> bean);
  <R extends Runnable> void runBean(Class<R> bean);

  boolean isActivated();

  void setOnClusterReadyListener(OnClusterReadyListener listener);

  void waitForReady() throws InterruptedException;
}
