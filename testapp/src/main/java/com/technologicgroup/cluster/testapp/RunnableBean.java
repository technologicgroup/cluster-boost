package com.technologicgroup.cluster.testapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RunnableBean implements Runnable {
    @Override
    public void run() {
      log.info("Runnable executed");
    }
}
