package com.technologicgroup.boost.core;

/**
 * Represents a cluster task that can be executed on a set of nodes
 * @param <A> is a task argument class
 * @param <R> is a result class of the task
 */
public interface ClusterTask<A, R> {

  /**
   * Method run will be executed on a local node
   * @param arg is a task argument
   * @return the result of local task execution
   */
  R run(A arg);
}
