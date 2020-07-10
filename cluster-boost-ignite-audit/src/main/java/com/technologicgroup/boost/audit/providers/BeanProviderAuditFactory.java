package com.technologicgroup.boost.audit.providers;

import com.technologicgroup.boost.audit.Trackable;
import com.technologicgroup.boost.common.providers.BeanProviderFactory;
import com.technologicgroup.boost.core.ClusterJob;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.RequiredArgsConstructor;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteRunnable;

import java.util.UUID;

/**
 * Factory bean providers interface
 * Bean provider is an object that can be picked up by Ignite and broadcast for execution to set of nodes
 * The bean provider holds a bean class that will be executed on a every node from the set of nodes
 */
@RequiredArgsConstructor
public class BeanProviderAuditFactory implements BeanProviderFactory {

  /**
   * Creates a runnable bean
   * @param bean is a bean class
   * @return the created Spring bean
   */
  @Override
  public <T extends Runnable> IgniteRunnable getRunnable(Class<T> bean) {
    BeanProviderAuditRunnable<T> provider = new BeanProviderAuditRunnable<>(bean);
    provider.startAudit(UUID.randomUUID().toString());
    return provider;
  }

  /**
   * Creates a bean job
   * @param bean is a bean class
   * @return the created Spring bean
   */
  @Override
  public <R, T extends ClusterJob<R>> IgniteCallable<R> getJob(Class<T> bean) {
    BeanProviderAuditJob<R, T> provider = new BeanProviderAuditJob<>(bean);
    provider.startAudit(UUID.randomUUID().toString());
    return provider;
  }

  /**
   * Creates a bean task
   * @param bean is a bean class
   * @return the created Spring bean
   */
  @Override
  public <A, R, T extends ClusterTask<A, R>> IgniteCallable<R> getTask(Class<T> bean, A arg) {
    BeanProviderAuditTask<A, R, T> provider = new BeanProviderAuditTask<>(bean, arg);

    String trackingId;
    if (arg instanceof Trackable) {
      trackingId = ((Trackable) arg).getTrackingId();
    } else {
      trackingId = UUID.randomUUID().toString();
    }

    provider.startAudit(trackingId);
    return provider;
  }
}
