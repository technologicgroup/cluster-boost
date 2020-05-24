package com.technologicgroup.core.ignite;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.resources.SpringApplicationContextResource;
import org.springframework.context.ApplicationContext;

@Slf4j
@RequiredArgsConstructor
class ClusterBeanProvider<R extends Runnable> implements IgniteRunnable {

  private final Class<R> beanClass;

  @SpringApplicationContextResource
  private transient ApplicationContext context;

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
    return context.getBean(beanClass);
  }
}
