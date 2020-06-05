package com.technologicgroup.boost.common.providers;

import com.technologicgroup.boost.common.ContextHolder;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.lang.IgniteCallable;

@Slf4j
@RequiredArgsConstructor
class BeanProviderTask<A, R, T extends ClusterTask<A, R>> implements IgniteCallable<R> {
  private final Class<T> beanClass;
  private final A arg;

  private T getBean() {
        return ContextHolder.getContext().getBean(beanClass);
    }

  @Override
  public R call() {
    try {
      return getBean().run(arg);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }
}
