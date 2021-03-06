package com.technologicgroup.boost.common;

import com.technologicgroup.boost.common.providers.BeanProviderFactory;
import com.technologicgroup.boost.core.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

/**
 * Ignite implementation for Cluster interface
 */
@Slf4j
@Service
@RequiredArgsConstructor
class IgniteCluster implements Cluster {
  final Ignite ignite;
  private final String[] hosts;
  private final int startupTimeout;
  private final BeanProviderFactory beanProviderFactory;

  private static final int DEFAULT_SLEEP_INTERVAL = 100;

  /**
   * Run cluster job on cluster group nodes for the result
   * @param clusterGroup represents set of nodes
   * @param job object to run
   * @param <R> result class
   * @return collection of results (one result per node)
   */
  @Override
  public <R> Collection<R> run(ClusterGroup clusterGroup, ClusterJob<R> job) {
    return ignite.compute(mapClusterGroup(clusterGroup)).broadcast(job::run);
  }

  /**
   * Run cluster job on every cluster node for the result
   * @param job object to run
   * @param <R> result class
   * @return collection of results (one result per node)
   */
  @Override
  public <R> Collection<R> run(ClusterJob<R> job) {
    return ignite.compute().broadcast(job::run);
  }

  /**
   * Execute runnable on cluster group nodes
   * @param clusterGroup represents set of nodes
   * @param job is a runnable object
   */
  @Override
  public void execute(ClusterGroup clusterGroup, Runnable job) {
    ignite.compute(mapClusterGroup(clusterGroup)).broadcast(job::run);
  }

  /**
   * Execute runnable on every cluster node
   * @param job is a runnable object
   */
  @Override
  public void execute(Runnable job) {
    ignite.compute().broadcast(job::run);
  }

  /**
   * Asynchronously execute runnable on cluster group nodes
   * @param clusterGroup represents set of nodes
   * @param job is a runnable object
   */
  @Override
  public synchronized void executeAsync(ClusterGroup clusterGroup, Runnable job) {
    ignite.compute(mapClusterGroup(clusterGroup)).broadcastAsync(job::run);
  }

  /**
   * Asynchronously execute runnable on every cluster node
   * @param job is a runnable object
   */
  @Override
  public synchronized void executeAsync(Runnable job) {
    ignite.compute().broadcastAsync(job::run);
  }

  /**
   * Asynchronously execute runnable bean on cluster group nodes
   * @param clusterGroup represents set of nodes
   * @param bean is a Spring bean class implements Runnable
   */
  @Override
  public <T extends Runnable> void executeBeanAsync(ClusterGroup clusterGroup, Class<T> bean) {
    ignite.compute(mapClusterGroup(clusterGroup)).broadcastAsync(beanProviderFactory.getRunnable(bean));
  }

  /**
   * Asynchronously execute runnable bean on every cluster node
   * @param bean is a Spring bean class implements Runnable
   */
  @Override
  public <T extends Runnable> void executeBeanAsync(Class<T> bean) {
    ignite.compute().broadcastAsync(beanProviderFactory.getRunnable(bean));
  }

  /**
   * Execute runnable bean on cluster group nodes
   * @param clusterGroup represents set of nodes
   * @param bean is a Spring bean class implements Runnable
   */
  @Override
  public <T extends Runnable> void executeBean(ClusterGroup clusterGroup, Class<T> bean) {
    ignite.compute(mapClusterGroup(clusterGroup)).broadcast(beanProviderFactory.getRunnable(bean));
  }

  /**
   * Execute runnable bean on every cluster node
   * @param bean is a Spring bean class implements Runnable
   */
  @Override
  public <T extends Runnable> void executeBean(Class<T> bean) {
    ignite.compute().broadcast(beanProviderFactory.getRunnable(bean));
  }

  /**
   * Run bean on cluster group nodes for the result
   * @param clusterGroup represents set of nodes
   * @param bean is a Spring bean class implements ClusterJob
   * @param <R> result class
   * @return collection of results (one result per node)
   */
  @Override
  public <R, T extends ClusterJob<R>> Collection<R> runBean(ClusterGroup clusterGroup, Class<T> bean) {
    return ignite.compute(mapClusterGroup(clusterGroup)).broadcast(beanProviderFactory.getJob(bean));
  }

  /**
   * Run bean on every cluster node for the result
   * @param bean is a Spring bean class implements ClusterJob
   * @param <R> result class
   * @return collection of results (one result per node)
   */
  @Override
  public <R, T extends ClusterJob<R>> Collection<R> runBean(Class<T> bean) {
    return ignite.compute().broadcast(beanProviderFactory.getJob(bean));
  }

  /**
   * Run bean on every cluster node for the result
   * @param bean is a Spring bean class implements ClusterJob
   * @param arg is an argument for a bean to run with
   * @param <R> result class
   * @return collection of results (one result per node)
   */
  @Override
  public <A, R, T extends ClusterTask<A, R>> Collection<R> runBean(Class<T> bean, A arg) {
    return ignite.compute().broadcast(beanProviderFactory.getTask(bean, arg));
  }

  /**
   * Run bean on cluster group nodes for the result
   * @param clusterGroup represents set of nodes
   * @param bean is a Spring bean class implements ClusterJob
   * @param arg is an argument for a bean to run with
   * @param <R> result class
   * @return collection of results (one result per node)
   */
  @Override
  public <A, R, T extends ClusterTask<A, R>> Collection<R> runBean(ClusterGroup clusterGroup, Class<T> bean, A arg) {
    return ignite.compute(mapClusterGroup(clusterGroup)).broadcast(beanProviderFactory.getTask(bean, arg));
  }

  /**
   * Determine if the whole cluster with all listed hosts was activated successfully
   * @return #true if cluster was activated and #false otherwise
   */
  @Override
  public boolean isActivated() {
    return ContextHolder.isActivated();
  }

  /**
   * Waits until cluster ready
   * @throws InterruptedException on interruption
   */
  @Override
  public void waitForReady() throws InterruptedException {
    ContextHolder.await();
  }

  /**
   * Returns the order of the current local node starting from 1
   * @return the order
   */
  @Override
  public long getNodeOrder() {
    return ignite.cluster().localNode().order();
  }

  /**
   * Indicates if a current local node is the first cluster node
   * @return #true if a current node is the first and #false otherwise
   */
  @Override
  public boolean isFirstNode() {
    return ignite.cluster().localNode().order() == 1;
  }

  /**
   * Returns a local node identifier
   * @return node identifier
   */
  @Override
  public String getLocalNode() {
    return ignite.cluster().localNode().id().toString();
  }

  @SuppressWarnings("BusyWait")
  void activate() {
    if (isFirstNode()) {
      int waitTime = 0;
      while (!checkActivationForAllNodes()) {
        try {
          Thread.sleep(DEFAULT_SLEEP_INTERVAL);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        waitTime += DEFAULT_SLEEP_INTERVAL;
        if (waitTime >= startupTimeout) {
          throw new RuntimeException("Cluster startup timed out");
        }
      }
      setIsReady();
    }
  }

  private org.apache.ignite.cluster.ClusterGroup mapClusterGroup(ClusterGroup clusterGroup) {
    Collection<UUID> collection = clusterGroup.getNodes().stream().map(UUID::fromString)
            .collect(Collectors.toList());

    return ignite.cluster().forNodeIds(collection);
  }

  private boolean checkActivationForAllNodes() {
    boolean activated = false;
    if (!ContextHolder.isActivated()) {
      Collection<Boolean> activations = runBean(ReadyDetectorJob.class);

      if (activations.size() < hosts.length) {
        log.debug("Waiting for all hosts: {} of {} is up", activations.size(), hosts.length);
        return false;
      }

      activated = activations.stream()
          .allMatch(v -> Optional.ofNullable(v).orElse(Boolean.FALSE));
    }
    return activated;
  }

  synchronized void setIsReady() {
    ContextHolder.setActivated();
    runBean(PublishReadyEventJob.class);
  }

}
