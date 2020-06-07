package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.audit.*;
import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Primary
@Slf4j
@AllArgsConstructor
@Service
class ChainBeanAuditImpl<A, R> implements ChainBean<A, R> {

  private final ApplicationContext context;
  private final AuditNodeItemAccessor nodeItemAccessor;
  private final Cluster cluster;

  @Override
  public R run(@NotNull ChainArgument<A> chainArgument) {
    Object result = chainArgument.getArg();
    String trackingId = chainArgument.getTrackingId();

    for (ChainStep<?, ?> chainItem : chainArgument.getItems()) {
      if (chainItem.getBean() != null) {
        ClusterTask clusterTask = context.getBean(chainItem.getBean());

        if (clusterTask == null) {
          throw new RuntimeException("Cannot find bean class: " + chainItem.getBean());
        }

        Timestamp start = new Timestamp(System.currentTimeMillis());
        //TODO: Catch exception
        result = clusterTask.run(result);

        Timestamp end = new Timestamp(System.currentTimeMillis());

        AuditNodeItem auditNodeItem = new AuditNodeItem(
            UUID.randomUUID().toString(),
            trackingId,
            start,
            end,
            null,
            null,
            0,
            cluster.getLocalNode()
        );
        nodeItemAccessor.put(auditNodeItem.getId(), auditNodeItem);
      }
    }
    return (R)result;
  }

}
