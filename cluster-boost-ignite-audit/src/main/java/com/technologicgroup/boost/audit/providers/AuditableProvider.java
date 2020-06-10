package com.technologicgroup.boost.audit.providers;

import com.technologicgroup.boost.audit.*;
import com.technologicgroup.boost.common.ContextHolder;
import com.technologicgroup.boost.core.Cluster;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.UUID;

@Slf4j
abstract class AuditableProvider<R, T> {
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

    AuditTaskInfo taskInfo = new AuditTaskInfo(timestamp, timestamp, null, null, 0, getClass());

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
    int resultCode = 0;

    R result;
    try {
      result = runBean();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultCode = 100;
      message = e.getMessage();
      detailedMessage = e.getCause().toString();
      throw e;
    } finally {
      Timestamp end = new Timestamp(System.currentTimeMillis());

      AuditTaskInfo taskInfo = new AuditTaskInfo(start, end, message, detailedMessage, resultCode, getBeanClass());

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
