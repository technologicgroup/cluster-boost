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
import java.util.Optional;
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
  public ChainResult<R> run(@NotNull ChainArgument<A> chainArgument) {
    Object result = chainArgument.getArg();
    String trackingId = chainArgument.getTrackingId();

    for (ChainStep<?, ?> chainItem : chainArgument.getItems()) {
      if (chainItem.getBean() != null) {
        ClusterTask clusterTask = context.getBean(chainItem.getBean());

        String message = null;
        String detailedMessage = null;
        int resultCode = 0;
        Timestamp start = new Timestamp(System.currentTimeMillis());

        try {
          if (clusterTask == null) {
            throw new RuntimeException("Cannot find bean class: " + chainItem.getBean());
          }
          result = clusterTask.run(result);

          // Interrupt chain if filter does not match
          if (clusterTask instanceof ChainFilterBean) {
            if (result == null) {
              break;
            }
          }

        } catch (Exception e) {
          message = e.getMessage();
          detailedMessage = Optional.ofNullable(e.getCause()).map(Throwable::toString).orElse(null);
          resultCode = 100;
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
              chainItem.getBean().getSimpleName(),
              cluster.getLocalNode()
          );
          nodeItemAccessor.put(auditNodeItem.getId(), auditNodeItem);
        }

      }
    }
    return new ChainResult<> (cluster.getLocalNode(), (R)result);
  }

}
