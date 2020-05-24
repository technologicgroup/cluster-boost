package com.technologicgroup.cluster.testapp;

import com.technologicgroup.core.ignite.IgniteRepositoryConfig;
import com.technologicgroup.gridgain.core.Cluster;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.IgniteSpring;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = { IgniteRepositoryConfig.class })
public class Config {

  @Autowired
  private Cluster cluster;

  @Bean
  public Ignite ignite(ApplicationContext context) throws IgniteCheckedException {
    IgniteConfiguration cfg = new IgniteConfiguration();
//    IgniteLogger log = new Slf4jLogger();
//    cfg.setGridLogger(log);
    return IgniteSpring.start(cfg, context);
  }

  @PostConstruct
  public void init() throws InterruptedException {
    log.info("TEST 1");

    cluster.setOnClusterReadyListener(() -> log.info("TEST 2"));
    cluster.waitForReady();
    cluster.run(() -> log.info("TEST 3"));

    // Must release context creation before run the bean
    CompletableFuture.runAsync(() -> cluster.runBean(RunnableBean.class));
  }

}
