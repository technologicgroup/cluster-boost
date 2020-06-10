package com.technologicgroup.boost.common.providers;

import com.technologicgroup.boost.core.ClusterJob;
import com.technologicgroup.boost.core.ClusterTask;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteRunnable;
import org.springframework.stereotype.Service;

/**
 * Bean providers factory
 * Bean provider is an object that can be picked up by Ignite and broadcast for execution to set of nodes
 * The bean provider holds a bean class that will be executed on a every node from the set of nodes
 */
@Service
class BeanProviderCommonFactory implements BeanProviderFactory {

  /**
   * Creates a runnable bean
   * @param bean is a bean class
   * @return the created Spring bean
   */
  @Override
  public <T extends Runnable> IgniteRunnable getRunnable(Class<T> bean) {
    return new BeanProviderRunnable<>(bean);
  }

  /**
   * Creates a bean job
   * @param bean is a bean class
   * @return the created Spring bean
   */
  @Override
  public <R, T extends ClusterJob<R>> IgniteCallable<R> getJob(Class<T> bean) {
    return new BeanProviderJob<>(bean);
  }

  /**
   * Creates a bean task
   * @param bean is a bean class
   * @return the created Spring bean
   */
  @Override
  public <A, R, T extends ClusterTask<A, R>> IgniteCallable<R> getTask(Class<T> bean, A arg) {
    return new BeanProviderTask<>(bean, arg);
  }
}
