package com.technologicgroup.cluster.testapp;

import com.technologicgroup.cluster.testapp.domain.TestRepository;
import com.technologicgroup.gridgain.core.Cluster;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RunnableBean implements Runnable {

    private final TestRepository testRepository;
    private final Cluster cluster;

    @Override
    public void run() {
      int size = testRepository.getAllLocal().size();
      log.info("Runnable executed, items found on node: {}", size);

      cluster.run(() -> log.info("You can call cluster from the bean as well"));
    }
}
