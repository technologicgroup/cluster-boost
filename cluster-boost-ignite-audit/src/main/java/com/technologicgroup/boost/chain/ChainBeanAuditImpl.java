package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.audit.*;
import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Primary
@Slf4j
@Service
class ChainBeanAuditImpl<A, R> extends ChainBean<A, R> {

  private final ApplicationContext context;
  private final AuditItemAccessor nodeItemAccessor;

  public ChainBeanAuditImpl(Cluster cluster, ApplicationContext context, AuditItemAccessor nodeItemAccessor) {
    super(cluster);
    this.context = context;
    this.nodeItemAccessor = nodeItemAccessor;
  }

  @Override
  protected <T> T getBean(Class<T> bean) {
    return context.getBean(bean);
  }

  @Override
  protected void onFinishBean(String trackingId, Timestamp start, Timestamp end, String message, String detailedMessage,
                              int resultCode, Class<? extends ClusterTask<?, ?>> bean, String localNode) {

    log.info("{}", String.format("Bean %s on node %s has finished with code %d. TrackingId: %s", bean, localNode, resultCode, trackingId));

    AuditTaskInfo taskInfo = new AuditTaskInfo(start, end, message, detailedMessage, resultCode, bean);

    AuditItem auditItem = new AuditItem(
        UUID.randomUUID().toString(),
        trackingId,
        taskInfo,
        localNode
    );
    nodeItemAccessor.put(auditItem.getId(), auditItem);
  }

  @Override
  protected void onStartBean(String trackingId, Timestamp start, Class<? extends ClusterTask<?, ?>> bean, String localNode) {
    log.info("{}", String.format("Bean %s on node %s has started. TrackingId %s", bean, localNode, trackingId));
  }
}
