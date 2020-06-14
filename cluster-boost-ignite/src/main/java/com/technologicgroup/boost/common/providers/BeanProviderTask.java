package com.technologicgroup.boost.common.providers;

import com.technologicgroup.boost.common.ContextHolder;
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
class BeanProviderTask<A, R, T extends ClusterTask<A, R>> implements IgniteCallable<R> {
  private final Class<T> beanClass;
  private final A arg;

  private T getBean() {
    return ContextHolder.getBean(beanClass);
  }

  @Override
  public R call() {
    try {
      T bean = getBean();
      if (bean == null) {
        log.warn("Job execution failed. {} not found", beanClass);
        return null;
      }
      return bean.run(arg);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }
}
