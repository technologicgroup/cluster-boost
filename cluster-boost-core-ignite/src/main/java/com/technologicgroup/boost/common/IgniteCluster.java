package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
class IgniteCluster implements Cluster {
  private final Ignite ignite;
  private final String[] hosts;

  private volatile boolean activated;

  CountDownLatch readyLatch = new CountDownLatch(1);
  OnClusterReadyListener listener;

  @Override
  public <R> Collection<R> run(ClusterGroup clusterGroup, ClusterJob<R> job) {
    return ignite.compute(mapClusterGroup(clusterGroup)).broadcast(job::run);
  }

  @Override
  public <R> Collection<R> run(ClusterJob<R> job) {
    return ignite.compute().broadcast(job::run);
  }

  @Override
  public void execute(ClusterGroup clusterGroup, ClusterCall job) {
    ignite.compute(mapClusterGroup(clusterGroup)).broadcast(job::run);
  }

  @Override
  public void execute(ClusterCall job) {
    ignite.compute().broadcast(job::run);
  }

  @Override
  public synchronized void executeAsync(ClusterGroup clusterGroup, ClusterCall job) {
    ignite.compute(mapClusterGroup(clusterGroup)).broadcastAsync(job::run);
  }

  @Override
  public synchronized void executeAsync(ClusterCall job) {
    ignite.compute().broadcastAsync(job::run);
  }

  @Override
  public <T extends Runnable> void executeBeanAsync(ClusterGroup clusterGroup, Class<T> bean) {
    ignite.compute(mapClusterGroup(clusterGroup)).broadcastAsync(new RunnableBeanProvider<>(bean));
  }

  @Override
  public <T extends Runnable> void executeBeanAsync(Class<T> bean) {
    ignite.compute().broadcastAsync(new RunnableBeanProvider<>(bean));
  }

  @Override
  public <T extends Runnable> void executeBean(ClusterGroup clusterGroup, Class<T> bean) {
    ignite.compute(mapClusterGroup(clusterGroup)).broadcast(new RunnableBeanProvider<>(bean));
  }

  @Override
  public <T extends Runnable> void executeBean(Class<T> bean) {
    ignite.compute().broadcast(new RunnableBeanProvider<>(bean));
  }

  @Override
  public <R, T extends ClusterJob<R>> Collection<R> runBean(ClusterGroup clusterGroup, Class<T> bean) {
    return ignite.compute(mapClusterGroup(clusterGroup)).broadcast(new ClusterJobBeanProvider<>(bean));
  }

  @Override
  public <R, T extends ClusterJob<R>> Collection<R> runBean(Class<T> bean) {
    return ignite.compute().broadcast(new ClusterJobBeanProvider<>(bean));
  }

  @Override
  public <A, R, T extends ClusterArgJob<A, R>> Collection<R> runBean(Class<T> bean, A arg) {
    return ignite.compute().broadcast(new ClusterJobBeanArgProvider<>(bean, arg));
  }

  @Override
  public <A, R, T extends ClusterArgJob<A, R>> Collection<R> runBean(ClusterGroup clusterGroup, Class<T> bean, A arg) {
    return ignite.compute(mapClusterGroup(clusterGroup)).broadcast(new ClusterJobBeanArgProvider<>(bean, arg));
  }

  @Override
  public boolean isActivated() {
    return activated;
  }

  @Override
  public synchronized void setOnClusterReadyListener(OnClusterReadyListener listener) {
    this.listener = listener;
    if (isActivated()) {
      Optional.ofNullable(listener).ifPresent(OnClusterReadyListener::onClusterReady);
      this.listener = null;
    }
  }

  @Override
  public void waitForReady() throws InterruptedException {
    readyLatch.await();
  }

  @Override
  public long getNodeOrder() {
    return ignite.cluster().localNode().order();
  }

  @SuppressWarnings("BusyWait")
  void activate() {
    if (isFirstNode()) {
      while (!checkActivationForAllNodes()) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        } catch (Throwable th) {
          log.error("Cluster activation error: {}", th.getMessage(), th);
          throw th;
        }
      }
      execute(this::setIsReady);
    }
  }

  private org.apache.ignite.cluster.ClusterGroup mapClusterGroup(ClusterGroup clusterGroup) {
    Collection<UUID> collection = clusterGroup.getNodes().stream().map(UUID::fromString)
            .collect(Collectors.toList());

    return ignite.cluster().forNodeIds(collection);
  }

  private boolean isFirstNode() {
    return ignite.cluster().localNode().order() == 1;
  }

  private boolean checkActivationForAllNodes() {
    if (!activated) {
      Collection<Boolean> activations = run(() -> {
        boolean isActivated = isLocalNodeActivated();
        log.info("Cluster host {} activation {}", ignite.cluster().node().hostNames(), isActivated);
        return isActivated;
      });

      if (activations.size() < hosts.length) {
        log.debug("Waiting for all hosts: {} of {} is up", activations.size(), hosts.length);
        return false;
      }
      activated = activations.stream().allMatch(Boolean::booleanValue);
    }
    return activated;
  }

  synchronized void setIsReady() {
    activated = true;
    readyLatch.countDown();
    Optional.ofNullable(listener).ifPresent(OnClusterReadyListener::onClusterReady);
    listener = null;
  }

  synchronized boolean isLocalNodeActivated() {
    if (ignite == null) {
      return false;
    }
    if (ignite.cluster() == null) {
      return false;
    }
    return ignite.cluster().active() && Activator.isReady();
  }

}
