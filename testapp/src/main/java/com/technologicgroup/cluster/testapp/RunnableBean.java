package com.technologicgroup.cluster.testapp;

import com.technologicgroup.boost.core.ClusterTask;
import com.technologicgroup.cluster.testapp.domain.TestRepository;
import com.technologicgroup.boost.core.Cluster;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RunnableBean implements ClusterTask<String, Integer> {

  private final TestRepository testRepository;
  private final Cluster cluster;

  @Override
  public Integer run(String arg) {
    log.info("TEST run bean with argument {}", arg);

    int size = testRepository.getAllLocal().size();
    log.info("Runnable executed, items found on node: {}", size);

    cluster.execute(() -> log.info("You can call cluster from the bean as well"));
    return size;
  }
}
