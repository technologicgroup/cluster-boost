package com.technologicgroup.cluster.testapp;

import com.technologicgroup.boost.core.ClusterTask;
import com.technologicgroup.cluster.testapp.domain.TestRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RunnableBean implements ClusterTask<String, Integer> {

  private final TestRepository testRepository;

  @Override
  public Integer run(String arg) {
    log.info("TEST run bean with argument {}", arg);

    int size = testRepository.getAllLocal().size();
    log.info("Runnable executed, items found on node: {}", size);

    return size;
  }
}
