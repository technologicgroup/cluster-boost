package com.technologicgroup.core.ignite;

import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.resources.SpringApplicationContextResource;
import org.springframework.context.ApplicationContext;

class ClusterBeanProvider implements IgniteRunnable {

  private Class<Runnable> beanClass;

  @SpringApplicationContextResource
  private transient ApplicationContext context;

  ClusterBeanProvider(Class<Runnable> beanClass) {
    this.beanClass = beanClass;
  }

  @Override
  public void run() {
    Runnable runnable = context.getBean(beanClass);
    runnable.run();
  }
}
