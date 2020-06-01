package com.technologicgroup.cluster.testapp;

import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.common.ClusterReadyEvent;
import com.technologicgroup.cluster.testapp.domain.TestKey;
import com.technologicgroup.cluster.testapp.domain.TestRepository;
import com.technologicgroup.cluster.testapp.domain.TestValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClusterReadyConsumer implements ApplicationListener<ClusterReadyEvent> {
  private final Cluster cluster;
  private final TestRepository testRepository;

  @Override
  public void onApplicationEvent(@NotNull ClusterReadyEvent event) {
    log.info("Cluster is ready: {}", event);

    long nodeOrder = cluster.getNodeOrder();
    int idPrefix = (int) (nodeOrder * 100);

    testRepository.put(new TestKey(idPrefix), new TestValue("0"));
    testRepository.put(new TestKey(idPrefix + 1), new TestValue("1"));

    cluster.execute(() -> log.info("TEST Cluster run"));
    int result = cluster.runBean(RunnableBean.class, "<Test Argument>").iterator().next();

    log.info("TEST Cluster run result: {}", result);
  }
}
