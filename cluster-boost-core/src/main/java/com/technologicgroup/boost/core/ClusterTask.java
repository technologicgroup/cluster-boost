package com.technologicgroup.boost.core;

/**
 * Represents a cluster task that can be executed on a set of nodes
 * @param <A> is a task argument class
 * @param <R> is a result class of the task
 */
public interface ClusterTask<A, R> {
  R run(A arg);
}
