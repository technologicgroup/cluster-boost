package com.technologicgroup.boost.common.providers;

import com.technologicgroup.boost.common.ContextHolder;
import com.technologicgroup.boost.core.ClusterJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.lang.IgniteCallable;

@Slf4j
@RequiredArgsConstructor
class BeanProviderJob<R, T extends ClusterJob<R>> implements IgniteCallable<R> {
  private final Class<T> beanClass;

  private T getBean() {
    return ContextHolder.getContext().getBean(beanClass);
  }

  @Override
  public R call() {
    try {
      return getBean().run();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }
}
