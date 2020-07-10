package com.technologicgroup.boost.audit.providers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.lang.IgniteRunnable;

/**
 * Runnable provider holds a runnable bean class
 * This bean will be injected executed on every node from a defined node set
 * This provider contains audit functionality
 * @param <T> is a bean class
 */
@Slf4j
@RequiredArgsConstructor
class BeanProviderAuditRunnable<T extends Runnable> extends BeanProviderAudit<Void, T> implements IgniteRunnable {
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
    T bean = getBean(beanClass);
    if (bean == null) {
      throw new RuntimeException("Job execution failed. " + beanClass + " not found");
    }
    bean.run();
    return null;
  }
}
