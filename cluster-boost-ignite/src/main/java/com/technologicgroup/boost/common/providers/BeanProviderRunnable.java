package com.technologicgroup.boost.common.providers;

import com.technologicgroup.boost.common.ContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.lang.IgniteRunnable;

@Slf4j
@RequiredArgsConstructor
class BeanProviderRunnable<T extends Runnable> implements IgniteRunnable {
  private final Class<T> beanClass;

  private T getBean() {
        return ContextHolder.getContext().getBean(beanClass);
    }

  @Override
  public void run() {
    try {
      getBean().run();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }
}
