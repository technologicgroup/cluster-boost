package com.technologicgroup.cluster.examples;

import com.technologicgroup.boost.audit.AuditItem;
import com.technologicgroup.boost.audit.AuditService;
import com.technologicgroup.boost.chain.Chain;
import com.technologicgroup.boost.chain.ChainResult;
import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.common.ClusterReadyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClusterReadyConsumer implements ApplicationListener<ClusterReadyEvent> {
  private final Cluster cluster;
  private final AuditService auditService;

  @Override
  public void onApplicationEvent(@NotNull ClusterReadyEvent event) {
    log.info("Cluster is ready: {}", event);

    String trackingId = UUID.randomUUID().toString();

    if (cluster.isFirstNode()) {
      Collection<ChainResult<String>> results = Chain.of(cluster)
          .track(trackingId)
          .map(ChainBean1.class, "Chain argument")  // Start chain with string argument
          .filter(r -> r.getResult() == 1)               // Continue chain only for odd nodes
          .map(ChainBean2.class)                         // On even nodes create a string result
          .run();                                        // Run chain steps

      log.info("Chain result: {}", results);

      // Get audit items for tracking id
      List<AuditItem> auditItems = auditService.getItems(trackingId);
      log.info("Audit items: {}", auditItems);
    }
  }
}
