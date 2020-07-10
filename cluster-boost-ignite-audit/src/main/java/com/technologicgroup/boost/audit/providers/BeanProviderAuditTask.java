package com.technologicgroup.boost.audit.providers;

import com.technologicgroup.boost.core.ClusterTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.lang.IgniteCallable;

/**
 * Task provider holds a cluster task bean class
 * This bean will be injected and executed on every node from a defined node set
 * @param <A> is a task argument
 * @param <R> is a bean execution result class
 * @param <T> is a bean class
 */
@Slf4j
@RequiredArgsConstructor
class BeanProviderAuditTask<A, R, T extends ClusterTask<A, R>> extends BeanProviderAudit<R, T> implements IgniteCallable<R> {
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
    T bean = getBean(beanClass);
    if (bean == null) {
      throw new RuntimeException("Job execution failed. " + beanClass + " not found");
    }
    return bean.run(arg);
  }
}
