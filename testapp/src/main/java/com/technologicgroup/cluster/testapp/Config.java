package com.technologicgroup.cluster.testapp;

import com.technologicgroup.core.ignite.IgniteRepositoryConfig;
import com.technologicgroup.gridgain.core.Cluster;
import com.technologicgroup.gridgain.core.ClusterCall;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = { IgniteRepositoryConfig.class })
public class Config {

  @Bean
  public Ignite ignite() {
    IgniteConfiguration cfg = new IgniteConfiguration();
//    IgniteLogger log = new Slf4jLogger();
//    cfg.setGridLogger(log);
    return Ignition.start(cfg);
  }

  @Autowired
  private Cluster cluster;

  @PostConstruct
  public void init() throws InterruptedException {
    log.info("TEST 1");

    cluster.setOnClusterReadyListener(() -> log.info("TEST 2"));
    cluster.waitForReady();
    cluster.run(() -> log.info("TEST 3"));
  }

}
