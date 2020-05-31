package com.technologicgroup.boost.core;

import java.util.Collection;

public interface Cluster {

  <R> Collection<R> run(ClusterGroup clusterGroup, ClusterJob<R> job);
  <R> Collection<R> run(ClusterJob<R> job);

  void execute(ClusterGroup clusterGroup, ClusterCall job);
  void execute(ClusterCall job);

  void executeAsync(ClusterGroup clusterGroup, ClusterCall job);
  void executeAsync(ClusterCall job);

  <T extends Runnable> void executeBeanAsync(Class<T> bean);
  <T extends Runnable> void executeBeanAsync(ClusterGroup clusterGroup, Class<T> bean);

  <T extends Runnable> void executeBean(Class<T> bean);
  <T extends Runnable> void executeBean(ClusterGroup clusterGroup, Class<T> bean);

  <R, T extends ClusterJob<R>> Collection<R> runBean(Class<T> bean);
  <R, T extends ClusterJob<R>> Collection<R> runBean(ClusterGroup clusterGroup, Class<T> bean);

  <A, R, T extends ClusterArgJob<A, R>> Collection<R> runBean(Class<T> bean, A arg);
  <A, R, T extends ClusterArgJob<A, R>> Collection<R> runBean(ClusterGroup clusterGroup, Class<T> bean, A arg);

  boolean isActivated();

  void setOnClusterReadyListener(OnClusterReadyListener listener);

  void waitForReady() throws InterruptedException;

  long getNodeOrder();
}
