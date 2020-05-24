package com.technologicgroup.core.ignite;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.lang.IgniteRunnable;

@Slf4j
@RequiredArgsConstructor
class ClusterBeanProvider<R extends Runnable> implements IgniteRunnable {

  private final Class<R> beanClass;

  @Override
  public void run() {
    try {
      getBean().run();
    } catch (Throwable th) {
      log.error(th.getMessage(), th);
      throw th;
    }
  }

  private R getBean() {
    return Activator.getContext().getBean(beanClass);
  }
}
