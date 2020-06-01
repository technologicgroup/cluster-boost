package com.technologicgroup.boost.core;

import java.util.Collection;

public interface Cluster {

  /**
   * Execute runnable on cluster group nodes
   * @param clusterGroup represents set of nodes
   * @param job is a runnable object
   */
  void execute(ClusterGroup clusterGroup, Runnable job);

  /**
   * Execute runnable on every cluster node
   * @param job is a runnable object
   */
  void execute(Runnable job);

  /**
   * Asynchronously execute runnable on cluster group nodes
   * @param clusterGroup represents set of nodes
   * @param job is a runnable object
   */
  void executeAsync(ClusterGroup clusterGroup, Runnable job);

  /**
   * Asynchronously execute runnable on every cluster node
   * @param job is a runnable object
   */
  void executeAsync(Runnable job);

  <T extends Runnable> void executeBeanAsync(Class<T> bean);
  <T extends Runnable> void executeBeanAsync(ClusterGroup clusterGroup, Class<T> bean);

  <T extends Runnable> void executeBean(Class<T> bean);
  <T extends Runnable> void executeBean(ClusterGroup clusterGroup, Class<T> bean);

  <R> Collection<R> run(ClusterGroup clusterGroup, ClusterJob<R> job);
  <R> Collection<R> run(ClusterJob<R> job);

  <R, T extends ClusterJob<R>> Collection<R> runBean(Class<T> bean);
  <R, T extends ClusterJob<R>> Collection<R> runBean(ClusterGroup clusterGroup, Class<T> bean);

  <A, R, T extends ClusterTask<A, R>> Collection<R> runBean(Class<T> bean, A arg);
  <A, R, T extends ClusterTask<A, R>> Collection<R> runBean(ClusterGroup clusterGroup, Class<T> bean, A arg);

  boolean isActivated();

  void setOnClusterReadyListener(OnClusterReadyListener listener);

  void waitForReady() throws InterruptedException;

  long getNodeOrder();
}
