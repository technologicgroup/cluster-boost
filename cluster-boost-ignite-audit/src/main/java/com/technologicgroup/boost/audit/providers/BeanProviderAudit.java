package com.technologicgroup.boost.audit.providers;

import com.technologicgroup.boost.audit.*;
import com.technologicgroup.boost.common.ContextHolder;
import com.technologicgroup.boost.core.Cluster;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

/**
 * Abstract base class for bean providers with audit functionality
 * @param <R>
 * @param <T>
 */
@Slf4j
abstract class BeanProviderAudit<R, T> implements Serializable {
  protected String trackingId;

  private AuditItemAccessor getAuditAccessor() {
    return getAnyBean(AuditItemAccessor.class);
  }
  protected AuditItemAccessor getItemService() {
    return getAnyBean(AuditItemAccessor.class);
  }

  protected T getBean(Class<T> beanClass) {
    return getAnyBean(beanClass);
  }

  private <RT> RT getAnyBean(Class<RT> beanClass) {
    return Optional.ofNullable(ContextHolder.getContext())
        .map(c -> c.getBean(beanClass))
        .orElse(null);
  }

  protected abstract Class<T> getBeanClass();

  public void startAudit(String trackingId) {
    this.trackingId = trackingId;

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    AuditTaskInfo taskInfo = new AuditTaskInfo(timestamp, timestamp, null, null, getClass());

    AuditItem item = new AuditItem(
        UUID.randomUUID().toString(),
        trackingId,
        taskInfo,
        getNodeId());

    Optional.ofNullable(getAuditAccessor()).ifPresent(a -> a.put(item.getId(), item));
  }

  protected String getNodeId() {
    return Optional.ofNullable(ContextHolder.getContext())
        .map(c -> c.getBean(Cluster.class))
        .map(Cluster::getLocalNode)
        .orElse("Unknown");
  }

  protected abstract R runBean();

  protected R process() {
    Timestamp start = new Timestamp(System.currentTimeMillis());

    String message = null;
    String detailedMessage = null;

    R result;
    try {
      result = runBean();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      message = e.getMessage();
      detailedMessage = Optional.ofNullable(e.getCause()).map(Throwable::toString).orElse(null);
      throw e;
    } finally {
      Timestamp end = new Timestamp(System.currentTimeMillis());

      AuditTaskInfo taskInfo = new AuditTaskInfo(start, end, message, detailedMessage, getBeanClass());
      AuditItem auditItem = new AuditItem(
          UUID.randomUUID().toString(),
          trackingId,
          taskInfo,
          getNodeId());

      Optional.ofNullable(getItemService()).ifPresent(s -> s.put(auditItem.getId(), auditItem));
    }
    return result;
  }

}
