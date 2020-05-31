package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterCall;
import com.technologicgroup.boost.core.ClusterGroup;
import com.technologicgroup.boost.core.ClusterJob;
import com.technologicgroup.boost.core.OnClusterReadyListener;

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

  private org.apache.ignite.cluster.ClusterGroup mapClusterGroup(ClusterGroup clusterGroup) {
    Collection<UUID> collection = clusterGroup.getNodes().stream().map(UUID::fromString)
        .collect(Collectors.toList());

    return ignite.cluster().forNodeIds(collection);
  }

  @Override
  public <R> Collection<R> run(ClusterGroup clusterGroup, ClusterJob<R> job) {
    return ignite.compute(mapClusterGroup(clusterGroup)).broadcast(job::run);
  }

  @Override
  public <R> Collection<R> run(ClusterJob<R> job) {
    return ignite.compute().broadcast(job::run);
  }

  @Override
  public void run(ClusterGroup clusterGroup, ClusterCall job) {
    ignite.compute(mapClusterGroup(clusterGroup)).broadcast(job::run);
  }

  @Override
  public void run(ClusterCall job) {
    ignite.compute().broadcast(job::run);
  }

  @Override
  public synchronized void runAsync(ClusterGroup clusterGroup, ClusterCall job) {
    ignite.compute(mapClusterGroup(clusterGroup)).broadcastAsync(job::run);
  }

  @Override
  public synchronized void runAsync(ClusterCall job) {
    ignite.compute().broadcastAsync(job::run);
  }

  @Override
  public <R extends Runnable> void runBeanAsync(ClusterGroup clusterGroup, Class<R> bean) {
    ignite.compute(mapClusterGroup(clusterGroup)).broadcastAsync(new ClusterBeanProvider<>(bean));
  }

  @Override
  public <R extends Runnable> void runBeanAsync(Class<R> bean) {
    ignite.compute().broadcastAsync(new ClusterBeanProvider<>(bean));
  }

  @Override
  public <R extends Runnable> Collection<R> runBean(ClusterGroup clusterGroup, Class<R> bean) {
    ignite.compute(mapClusterGroup(clusterGroup)).broadcast(new ClusterBeanProvider<>(bean));
    return null;
  }

  @Override
  public <R extends Runnable> Collection<R> runBean(Class<R> bean) {
    ignite.compute().broadcast(new ClusterBeanProvider<>(bean));
    return null;
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
      run(this::setIsReady);
    }
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
