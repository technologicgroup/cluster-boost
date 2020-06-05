package com.technologicgroup.boost.common.providers;

import com.technologicgroup.boost.core.ClusterJob;
import com.technologicgroup.boost.core.ClusterTask;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteRunnable;

public interface BeanProviderFactory {
  <T extends Runnable> IgniteRunnable getRunnable(Class<T> bean);
  <R, T extends ClusterJob<R>> IgniteCallable<R> getJob(Class<T> bean);
  <A, R, T extends ClusterTask<A, R>> IgniteCallable<R> getTask(Class<T> bean, A arg);
}
