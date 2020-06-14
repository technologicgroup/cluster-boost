package com.technologicgroup.cluster.testapp;

import com.technologicgroup.boost.common.ClusterConfig;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = { ClusterConfig.class })
public class ApplicationConfig {

  @Bean
  public IgniteConfiguration igniteConfiguration() {
    return new IgniteConfiguration();
  }

}
