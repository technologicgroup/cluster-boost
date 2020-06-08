package com.technologicgroup.boost.audit.providers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.lang.IgniteRunnable;

@Slf4j
@RequiredArgsConstructor
class BeanProviderRunnable<T extends Runnable> extends AuditableProvider<Void, T> implements IgniteRunnable {
  private final Class<T> beanClass;

  @Override
  public void run() {
    process();
  }

  @Override
  protected Class<T> getBeanClass() {
    return beanClass;
  }

  @Override
  protected Void runBean() {
    getBean(beanClass).run();
    return null;
  }
}
