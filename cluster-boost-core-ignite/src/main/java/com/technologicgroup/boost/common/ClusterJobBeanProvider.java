package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.ClusterJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.lang.IgniteCallable;

@Slf4j
@RequiredArgsConstructor
class ClusterJobBeanProvider<R, T extends ClusterJob<R>> implements IgniteCallable<R> {

  private final Class<T> beanClass;

  private T getBean() {
    return Activator.getContext().getBean(beanClass);
  }

  @Override
  public R call() {
    try {
      return getBean().run();
    } catch (Throwable th) {
      log.error(th.getMessage(), th);
      throw th;
    }
  }
}
