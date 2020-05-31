package com.technologicgroup.cluster.testapp;

import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.OnClusterReadyListener;
import com.technologicgroup.cluster.testapp.domain.TestKey;
import com.technologicgroup.cluster.testapp.domain.TestRepository;
import com.technologicgroup.cluster.testapp.domain.TestValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClusterReadyListener implements OnClusterReadyListener {
  private final Cluster cluster;
  private final TestRepository testRepository;

  @Override
  public void onClusterReady() {
    log.info("Cluster is ready");

    testRepository.put(new TestKey(0), new TestValue("0"));
    testRepository.put(new TestKey(1), new TestValue("1"));

    cluster.execute(() -> log.info("TEST Cluster run"));
    cluster.executeBean(RunnableBean.class);
  }
}
