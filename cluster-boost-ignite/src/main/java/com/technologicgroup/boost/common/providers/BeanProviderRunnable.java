package com.technologicgroup.boost.common.providers;

import com.technologicgroup.boost.common.ContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.lang.IgniteRunnable;

/**
 * Runnable provider holds a runnable bean class
 * This bean will be injected executed on every node from a defined node set
 * @param <T> is a bean class
 */
@Slf4j
@RequiredArgsConstructor
class BeanProviderRunnable<T extends Runnable> implements IgniteRunnable {
  private final Class<T> beanClass;

  private T getBean() {
        return ContextHolder.getBean(beanClass);
    }

  @Override
  public void run() {
    try {
      T bean = getBean();
      if (bean == null) {
        log.warn("Runnable execution failed. {} not found", beanClass);
        return;
      }
      bean.run();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }
}
