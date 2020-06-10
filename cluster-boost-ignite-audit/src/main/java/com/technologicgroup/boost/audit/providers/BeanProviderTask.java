package com.technologicgroup.boost.audit.providers;

import com.technologicgroup.boost.core.ClusterTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.lang.IgniteCallable;

@Slf4j
@RequiredArgsConstructor
class BeanProviderTask<A, R, T extends ClusterTask<A, R>> extends BeanProviderAudit<R, T> implements IgniteCallable<R> {
  private final Class<T> beanClass;
  private final A arg;

  @Override
  public R call() {
    return process();
  }

  @Override
  protected Class<T> getBeanClass() {
    return beanClass;
  }

  @Override
  protected R runBean() {
    return getBean(beanClass).run(arg);
  }
}
