package com.technologicgroup.gridgain.core;

public interface Cluster {

  <R> R run(ClusterGroup clusterGroup, ClusterJob<R> job);
  void run(ClusterGroup clusterGroup, ClusterCall job);

  <R> R run(ClusterJob<R> job);
  void run(ClusterCall job);

  void runAsync(ClusterGroup clusterGroup, ClusterCall job);
  void runAsync(ClusterCall job);

  void runBean(ClusterGroup clusterGroup, Class<Runnable> bean);
  void runBean(Class<Runnable> bean);

  boolean isActivated();

  void setOnClusterReadyListener(OnClusterReadyListener listener);

  void waitForReady() throws InterruptedException;
}