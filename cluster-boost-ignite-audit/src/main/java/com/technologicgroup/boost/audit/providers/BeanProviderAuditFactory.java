package com.technologicgroup.boost.audit.providers;

import com.technologicgroup.boost.audit.Trackable;
import com.technologicgroup.boost.common.providers.BeanProviderFactory;
import com.technologicgroup.boost.core.ClusterJob;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.RequiredArgsConstructor;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteRunnable;

import java.util.UUID;

@RequiredArgsConstructor
public class BeanProviderAuditFactory implements BeanProviderFactory {

  @Override
  public <T extends Runnable> IgniteRunnable getRunnable(Class<T> bean) {
    BeanProviderRunnable<T> provider = new BeanProviderRunnable<>(bean);
    provider.startAudit(UUID.randomUUID().toString());
    return provider;
  }

  @Override
  public <R, T extends ClusterJob<R>> IgniteCallable<R> getJob(Class<T> bean) {
    BeanProviderJob<R, T> provider = new BeanProviderJob<>(bean);
    provider.startAudit(UUID.randomUUID().toString());
    return provider;
  }

  @Override
  public <A, R, T extends ClusterTask<A, R>> IgniteCallable<R> getTask(Class<T> bean, A arg) {
    BeanProviderTask<A, R, T> provider = new BeanProviderTask<>(bean, arg);

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
