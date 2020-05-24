package com.technologicgroup.core.ignite;

import com.technologicgroup.gridgain.core.Cluster;
import com.technologicgroup.gridgain.core.ClusterCall;
import com.technologicgroup.gridgain.core.ClusterGroup;
import com.technologicgroup.gridgain.core.ClusterJob;
import com.technologicgroup.gridgain.core.OnClusterReadyListener;

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
  public <R> R run(ClusterGroup clusterGroup, ClusterJob<R> job) {
    return ignite.compute(mapClusterGroup(clusterGroup)).call(job::run);
  }

  @Override
  public void run(ClusterGroup clusterGroup, ClusterCall job) {
    ignite.compute(mapClusterGroup(clusterGroup)).run(job::run);
  }

  @Override
  public <R> R run(ClusterJob<R> job) {
    return ignite.compute().call(job::run);
  }

  @Override
  public void run(ClusterCall job) {
    ignite.compute().run(job::run);
  }

  @Override
  public synchronized void runAsync(ClusterGroup clusterGroup, ClusterCall job) {
    ignite.compute(mapClusterGroup(clusterGroup)).runAsync(job::run);
  }

  @Override
  public synchronized void runAsync(ClusterCall job) {
    ignite.compute().runAsync(job::run);
  }

  @Override
  public <R extends Runnable> void runBean(ClusterGroup clusterGroup, Class<R> bean) {
    ignite.compute(mapClusterGroup(clusterGroup)).run(new ClusterBeanProvider<>(bean));
  }

  @Override
  public <R extends Runnable> void runBean(Class<R> bean) {
    ignite.compute().run(new ClusterBeanProvider<>(bean));
  }

  @Override
  public boolean isActivated() {
    return ignite.cluster().active();
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
