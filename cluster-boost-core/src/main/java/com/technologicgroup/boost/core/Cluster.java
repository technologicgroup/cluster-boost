package com.technologicgroup.boost.core;

import java.util.Collection;

/**
 * Provides cluster operations
 */
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

  /**
   * Asynchronously execute runnable bean on every cluster node
   * @param bean is a Spring bean class implements Runnable
   */
  <T extends Runnable> void executeBeanAsync(Class<T> bean);

  /**
   * Asynchronously execute runnable bean on cluster group nodes
   * @param clusterGroup represents set of nodes
   * @param bean is a Spring bean class implements Runnable
   */
  <T extends Runnable> void executeBeanAsync(ClusterGroup clusterGroup, Class<T> bean);

  /**
   * Execute runnable bean on every cluster node
   * @param bean is a Spring bean class implements Runnable
   */
  <T extends Runnable> void executeBean(Class<T> bean);

  /**
   * Execute runnable bean on cluster group nodes
   * @param clusterGroup represents set of nodes
   * @param bean is a Spring bean class implements Runnable
   */
  <T extends Runnable> void executeBean(ClusterGroup clusterGroup, Class<T> bean);

  /**
   * Run cluster job on every cluster node for the result
   * @param job object to run
   * @param <R> result class
   * @return collection of results (one result per node)
   */
  <R> Collection<R> run(ClusterJob<R> job);

  /**
   * Run cluster job on cluster group nodes for the result
   * @param clusterGroup represents set of nodes
   * @param job object to run
   * @param <R> result class
   * @return collection of results (one result per node)
   */
  <R> Collection<R> run(ClusterGroup clusterGroup, ClusterJob<R> job);

  /**
   * Run bean on every cluster node for the result
   * @param bean is a Spring bean class implements ClusterJob
   * @param <R> result class
   * @return collection of results (one result per node)
   */
  <R, T extends ClusterJob<R>> Collection<R> runBean(Class<T> bean);

  /**
   * Run bean on cluster group nodes for the result
   * @param clusterGroup represents set of nodes
   * @param bean is a Spring bean class implements ClusterJob
   * @param <R> result class
   * @return collection of results (one result per node)
   */
  <R, T extends ClusterJob<R>> Collection<R> runBean(ClusterGroup clusterGroup, Class<T> bean);

  /**
   * Run bean on every cluster node for the result
   * @param bean is a Spring bean class implements ClusterJob
   * @param arg is an argument for a bean to run with
   * @param <R> result class
   * @return collection of results (one result per node)
   */
  <A, R, T extends ClusterTask<A, R>> Collection<R> runBean(Class<T> bean, A arg);

  /**
   * Run bean on cluster group nodes for the result
   * @param clusterGroup represents set of nodes
   * @param bean is a Spring bean class implements ClusterJob
   * @param arg is an argument for a bean to run with
   * @param <R> result class
   * @return collection of results (one result per node)
   */
  <A, R, T extends ClusterTask<A, R>> Collection<R> runBean(ClusterGroup clusterGroup, Class<T> bean, A arg);

  /**
   * Determine if the whole cluster with all listed hosts was activated successfully
   * @return #true if cluster was activated and #false otherwise
   */
  boolean isActivated();

  /**
   * Waits until cluster ready
   * @throws InterruptedException on interruption
   */
  void waitForReady() throws InterruptedException;

  /**
   * Returns the order of the current local node starting from 1
   * @return the order
   */
  long getNodeOrder();

  /**
   * Indicates if a current local node is the first cluster node
   * @return #true if a current node is the first and #false otherwise
   */
  boolean isFirstNode();

  /**
   * Returns a local node identifier
   * @return node identifier
   */
  String getLocalNode();
}
