package com.technologicgroup.boost.core;

/**
 * Represents a cluster job that can be executed on a set of nodes
 * @param <R> is a result class of the job
 */
public interface ClusterJob<R> {
  R run();
}
