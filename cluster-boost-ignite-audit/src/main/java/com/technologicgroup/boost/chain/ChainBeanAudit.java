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
class ChainBeanAudit<A, R> extends ChainBeanCommon<A, R> {

  private final AuditItemAccessor nodeItemAccessor;

  public ChainBeanAudit(Cluster cluster, ApplicationContext context, AuditItemAccessor nodeItemAccessor) {
    super(cluster, context);
    this.nodeItemAccessor = nodeItemAccessor;
  }

  /**
   * Creates an audit item on a bean execution finished
   * @param trackingId is the tracking identifier
   * @param start is the start execution timestamp
   * @param end is the end of execution timestamp
   * @param message is a failure message if execution fails and {#code=null} otherwise
   * @param detailedMessage is a detailed failure message
   * @param bean is a bean that was finished execution on local node
   */
  @Override
  protected void onFinishBean(String trackingId, Timestamp start, Timestamp end, String message, String detailedMessage,
                              Class<? extends ClusterTask<?, ?>> bean) {

    super.onFinishBean(trackingId, start, end, message, detailedMessage, bean);

    AuditTaskInfo taskInfo = new AuditTaskInfo(start, end, message, detailedMessage, bean);
    AuditItem auditItem = new AuditItem(
        UUID.randomUUID().toString(),
        trackingId,
        taskInfo,
        cluster.getLocalNode()
    );
    nodeItemAccessor.put(auditItem.getId(), auditItem);
  }
}
