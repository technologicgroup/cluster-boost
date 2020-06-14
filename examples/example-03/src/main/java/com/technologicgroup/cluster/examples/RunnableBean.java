package com.technologicgroup.cluster.examples;

import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RunnableBean implements ClusterTask<String, Integer> {

  private final Cluster cluster;

  @Override
  public Integer run(String arg) {
    log.info("Run bean with argument {}", arg);

    log.info("Bean executed on node number: {}", cluster.getNodeOrder());
    return (int) cluster.getNodeOrder();
  }
}
