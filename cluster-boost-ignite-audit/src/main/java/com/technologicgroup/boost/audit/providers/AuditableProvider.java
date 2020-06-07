package com.technologicgroup.boost.audit.providers;

import com.technologicgroup.boost.audit.AuditData;
import com.technologicgroup.boost.audit.AuditDataService;
import com.technologicgroup.boost.audit.AuditNodeItem;
import com.technologicgroup.boost.audit.AuditNodeItemService;
import com.technologicgroup.boost.common.ContextHolder;
import com.technologicgroup.boost.core.Cluster;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.UUID;

@Slf4j
abstract class AuditableProvider<R, T> {
  protected String trackingId;

  private AuditDataService getDataService() {
    return ContextHolder.getContext().getBean(AuditDataService.class);
  }
  protected AuditNodeItemService getItemService() {
    return ContextHolder.getContext().getBean(AuditNodeItemService.class);
  }

  protected T getBean(Class<T> beanClass) {
    return ContextHolder.getContext().getBean(beanClass);
  }

  public void startAudit(Class<?> beanClass, String trackingId) {
    this.trackingId = trackingId;

    AuditData auditData = new AuditData(
        trackingId,
        new Timestamp(System.currentTimeMillis()),
        beanClass.getSimpleName(),
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
    Timestamp end;
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
      end = new Timestamp(System.currentTimeMillis());

      AuditNodeItem auditNodeItem = new AuditNodeItem(
          UUID.randomUUID().toString(),
          trackingId,
          start,
          end,
          message,
          detailedMessage,
          resultCode,
          getNodeId());

      getItemService().put(auditNodeItem.getId(), auditNodeItem);
    }
    return result;
  }

}
