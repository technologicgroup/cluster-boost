package com.technologicgroup.core.ignite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class Activator {

  @Autowired
  private IgniteCluster cluster;

  @Autowired
  private ApplicationContext context;

  @PostConstruct
  public void init() {
  }

}
