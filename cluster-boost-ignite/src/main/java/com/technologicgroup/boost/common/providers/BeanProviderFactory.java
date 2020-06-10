package com.technologicgroup.boost.common.providers;

import com.technologicgroup.boost.core.ClusterJob;
import com.technologicgroup.boost.core.ClusterTask;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteRunnable;

/**
 * Factory bean providers interface
 * Bean provider is an object that can be picked up by Ignite and broadcast for execution to set of nodes
 * The bean provider holds a bean class that will be executed on a every node from the set of nodes
 */
public interface BeanProviderFactory {
  <T extends Runnable> IgniteRunnable getRunnable(Class<T> bean);
  <R, T extends ClusterJob<R>> IgniteCallable<R> getJob(Class<T> bean);
  <A, R, T extends ClusterTask<A, R>> IgniteCallable<R> getTask(Class<T> bean, A arg);
}
