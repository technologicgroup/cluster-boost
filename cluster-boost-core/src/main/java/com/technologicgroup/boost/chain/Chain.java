package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterGroup;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Chain {

  private final Cluster cluster;
  private ClusterGroup clusterGroup;
  List<ChainStep<?, ?>> steps = new ArrayList<>();
  private Object arg;

  public static Chain of(Cluster cluster) {
    return new Chain(cluster);
  }

  public Chain on(ClusterGroup clusterGroup) {
    this.clusterGroup = clusterGroup;
    return this;
  }

  public <A, R, T extends ClusterTask<A, R>> ChainStep<A, R> start(Class<T> bean, A arg) {
    this.arg = arg;
    ChainStep<A, R> step = new ChainStep<>(bean, this);
    steps.add(step);
    return step;
  }

  <R> Collection<R> run() {
    if (clusterGroup == null) {
      return cluster.runBean(ChainBean.class, new ChainArgument(arg, steps));
    } else {
      return cluster.runBean(clusterGroup, ChainBean.class, new ChainArgument(arg, steps));
    }
  }

}
