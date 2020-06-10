package com.technologicgroup.cluster.testapp;

import com.technologicgroup.boost.audit.AuditService;
import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.common.ClusterReadyEvent;
import com.technologicgroup.boost.chain.Chain;
import com.technologicgroup.cluster.testapp.domain.TestKey;
import com.technologicgroup.cluster.testapp.domain.TestRepository;
import com.technologicgroup.cluster.testapp.domain.TestValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClusterReadyConsumer implements ApplicationListener<ClusterReadyEvent> {
  private final Cluster cluster;
  private final TestRepository testRepository;
  private final AuditService auditService;

  @Override
  public void onApplicationEvent(@NotNull ClusterReadyEvent event) {
    log.info("Cluster is ready: {}", event);

    long nodeOrder = cluster.getNodeOrder();
    int idPrefix = (int) (nodeOrder * 100);

    testRepository.put(new TestKey(idPrefix), new TestValue("0"));
    testRepository.put(new TestKey(idPrefix + 1), new TestValue("1"));

    if (cluster.isFirstNode()) {
      String firstNode = cluster.getLocalNode();
      cluster.execute(() -> log.info("TEST Cluster run"));
      int result = cluster.runBean(RunnableBean.class, "<Test Argument>").iterator().next();
      log.info("TEST Cluster run result: {}", result);

      String trackingId = UUID.randomUUID().toString();
      boolean allNodesHasData = Chain.of(cluster)
          .track(trackingId)
          .map(RunnableBean.class, "Chain argument")
          .map(TaskBean.class)
          .filter(p -> p.getNodeId().equals(firstNode))  // After filtering all steps will be performed only to 1st node
          .collect(c -> c.stream().allMatch(Boolean::booleanValue));

      log.info("TEST Cluster chain run result: {}", allNodesHasData);
      log.info("Collected audit items: {}", auditService.getItems(trackingId).toString());
    }
  }
}
