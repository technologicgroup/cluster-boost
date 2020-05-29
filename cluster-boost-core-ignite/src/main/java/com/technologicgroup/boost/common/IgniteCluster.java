package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterCall;
import com.technologicgroup.boost.core.ClusterGroup;
import com.technologicgroup.boost.core.ClusterJob;
import com.technologicgroup.boost.core.OnClusterReadyListener;

import org.apache.ignite.Ignite;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class IgniteCluster implements Cluster {
  private final Ignite ignite;

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
    return ignite.cluster().active() && Activator.isReady();
  }

  @Override
  public synchronized void setOnClusterReadyListener(OnClusterReadyListener listener) {
    this.listener = listener;
    if (isActivated()) {
      Optional.ofNullable(listener).ifPresent(OnClusterReadyListener::onClusterReady);
      this.listener = null;
    }
  }

  synchronized void setIsReady() {
    readyLatch.countDown();
    Optional.ofNullable(listener).ifPresent(OnClusterReadyListener::onClusterReady);
    listener = null;
  }

  @Override
  public void waitForReady() throws InterruptedException {
    readyLatch.await();
  }

}
