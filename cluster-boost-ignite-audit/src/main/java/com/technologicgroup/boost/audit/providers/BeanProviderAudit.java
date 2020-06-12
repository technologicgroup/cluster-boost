package com.technologicgroup.boost.audit.providers;

import com.technologicgroup.boost.audit.*;
import com.technologicgroup.boost.common.ContextHolder;
import com.technologicgroup.boost.core.Cluster;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

/**
 * Abstract base class for bean providers with audit functionality
 * @param <R>
 * @param <T>
 */
@Slf4j
abstract class BeanProviderAudit<R, T> {
  protected String trackingId;

  private AuditItemAccessor getAuditAccessor() {
    return ContextHolder.getContext().getBean(AuditItemAccessor.class);
  }
  protected AuditItemAccessor getItemService() {
    return ContextHolder.getContext().getBean(AuditItemAccessor.class);
  }

  protected T getBean(Class<T> beanClass) {
    return ContextHolder.getContext().getBean(beanClass);
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

    getAuditAccessor().put(item.getId(), item);
  }

  protected String getNodeId() {
    Cluster cluster = ContextHolder.getContext().getBean(Cluster.class);
    return cluster.getLocalNode();
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

      getItemService().put(auditItem.getId(), auditItem);
    }
    return result;
  }

}
