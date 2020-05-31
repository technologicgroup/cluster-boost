package com.technologicgroup.cluster.testapp;

import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class IgniteEnvironmentConfig {

  @Bean
  public IgniteConfiguration igniteConfiguration() {
    return new IgniteConfiguration();
  }

}
