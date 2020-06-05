package com.technologicgroup.boost.common.providers;

import com.technologicgroup.boost.core.ClusterJob;
import com.technologicgroup.boost.core.ClusterTask;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteRunnable;
import org.springframework.stereotype.Service;

@Service
class BeanProviderFactoryImpl implements BeanProviderFactory {

  @Override
  public <T extends Runnable> IgniteRunnable getRunnable(Class<T> bean) {
    return new BeanProviderRunnable<>(bean);
  }

  @Override
  public <R, T extends ClusterJob<R>> IgniteCallable<R> getJob(Class<T> bean) {
    return new BeanProviderJob<>(bean);
  }

  @Override
  public <A, R, T extends ClusterTask<A, R>> IgniteCallable<R> getTask(Class<T> bean, A arg) {
    return new BeanProviderTask<>(bean, arg);
  }
}
