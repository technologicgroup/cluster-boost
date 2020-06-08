package com.technologicgroup.boost.audit.providers;

import com.technologicgroup.boost.audit.AuditData;
import com.technologicgroup.boost.audit.AuditDataAccessor;
import com.technologicgroup.boost.audit.AuditNodeItem;
import com.technologicgroup.boost.audit.AuditNodeItemAccessor;
import com.technologicgroup.boost.common.ContextHolder;
import com.technologicgroup.boost.core.Cluster;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.UUID;

@Slf4j
abstract class AuditableProvider<R, T> {
  protected String trackingId;

  private AuditDataAccessor getDataService() {
    return ContextHolder.getContext().getBean(AuditDataAccessor.class);
  }
  protected AuditNodeItemAccessor getItemService() {
    return ContextHolder.getContext().getBean(AuditNodeItemAccessor.class);
  }

  protected T getBean(Class<T> beanClass) {
    return ContextHolder.getContext().getBean(beanClass);
  }
  protected abstract Class<T> getBeanClass();

  public void startAudit(String trackingId) {
    this.trackingId = trackingId;

    AuditData auditData = new AuditData(
        trackingId,
        new Timestamp(System.currentTimeMillis()),
        getBeanClass().getSimpleName(),
        getNodeId());

    getDataService().put(auditData.getTrackingId(), auditData);
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

      AuditNodeItem auditNodeItem = new AuditNodeItem(
          UUID.randomUUID().toString(),
          trackingId,
          start,
          end,
          message,
          detailedMessage,
          resultCode,
          getBeanClass().getSimpleName(),
          getNodeId());

      getItemService().put(auditNodeItem.getId(), auditNodeItem);
    }
    return result;
  }

}
