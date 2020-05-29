package com.technologicgroup.cluster.testapp;

import com.technologicgroup.boost.common.IgniteRepositoryConfig;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = { IgniteRepositoryConfig.class })
public class IgniteConfig {

  @Bean
  public Ignite ignite() {
    IgniteConfiguration cfg = new IgniteConfiguration();
    return Ignition.start(cfg);
  }

}
