package com.technologicgroup.cluster.examples;

import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.common.ClusterReadyEvent;
import com.technologicgroup.cluster.examples.domain.TestAccessor;
import com.technologicgroup.cluster.examples.domain.TestKey;
import com.technologicgroup.cluster.examples.domain.TestRepository;
import com.technologicgroup.cluster.examples.domain.TestValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClusterReadyConsumer implements ApplicationListener<ClusterReadyEvent> {
  private final Cluster cluster;
  private final TestRepository testRepository;
  private final TestAccessor testAccessor;

  @Override
  public void onApplicationEvent(@NotNull ClusterReadyEvent event) {
    log.info("Cluster is ready: {}", event);

    long nodeOrder = cluster.getNodeOrder();
    int idPrefix = (int) (nodeOrder * 100);

    testRepository.put(new TestKey(idPrefix), new TestValue("0"));
    testRepository.put(new TestKey(idPrefix + 1), new TestValue("1"));

    Map<TestKey, TestValue> map = testRepository.getAllLocal();
    log.info("Repository local data: {}", map);

    map = testAccessor.networkGetAll();
    log.info("Repository cluster data: {}", map);
  }
}
