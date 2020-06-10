package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterGroup;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Chain class allows to manage how to run tasks on cluster nodes
 * For example you can run 2 tasks one by one on every node.
 * When on separate node task_1 is be finished but on 2nd node task_1 is still in progress -
 * you do not need to wait until task_1 finished on all nodes and run on a node next task.
 * Or you can wait until all nodes finish their works on task_1 and only after that collect results and start task_2
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Chain {

  private final Cluster cluster;
  private String trackingId;
  ClusterGroup clusterGroup;
  Object arg;

  List<ChainStep<?, ?>> steps = new ArrayList<>();

  /**
   * Static constructor of Chain object
   * @param cluster is a cluster object that will accept cluster tasks
   * @return Chain object
   */
  public static Chain of(Cluster cluster) {
    return new Chain(cluster);
  }

  /**
   * Specifies cluster group to run a tasks chain on
   * @param clusterGroup represents a set of nodes to run a tasks chain
   * @return Chain object
   */
  public Chain on(ClusterGroup clusterGroup) {
    this.clusterGroup = clusterGroup;
    return this;
  }

  public Chain track(String trackingId) {
    this.trackingId = trackingId;
    return this;
  }

  /**
   * Adds a bean task with null argument to the chain
   * @param bean is a bean of the cluster task
   * @param <A> is an argument class for the task
   * @param <R> is a result class for the task
   * @param <T> is the class of a bean
   * @return the ChainStep object with the specified bean task
   */
  public <A, R, T extends ClusterTask<A, R>> ChainStep<A, R> map(Class<T> bean) {
    this.arg = null;
    ChainStep<A, R> step = new ChainStep<>(bean, this);
    steps.add(step);
    return step;
  }

  /**
   * Adds a bean task with specified argument to the chain
   * @param bean is a bean of the cluster task
   * @param arg an argument for the task
   * @param <A> is an argument class for the task
   * @param <R> is a result class for the task
   * @param <T> is the class of a bean
   * @return the ChainStep object with the specified bean task
   */
  public <A, R, T extends ClusterTask<A, R>> ChainStep<A, R> map(Class<T> bean, A arg) {
    this.arg = arg;
    ChainStep<A, R> step = new ChainStep<>(bean, this);
    steps.add(step);
    return step;
  }

  /**
   * Runs step by step list of beans on every node from defined cluster group.
   * If cluster group is not defined chain steps will be performed to every node on the cluster
   * @param <R> is a class of the result of running steps
   * @return a collection of result objects
   */
  <R> Collection<ChainResult<R>> run() {
    Collection<ChainResult<R>> result;
    if (clusterGroup == null) {
      result = cluster.runBean(ChainBean.class, new ChainArgument(arg, steps, trackingId));
    } else {
      result = cluster.runBean(clusterGroup, ChainBean.class, new ChainArgument(arg, steps, trackingId));
    }
    steps.clear();
    return result;
  }

}
