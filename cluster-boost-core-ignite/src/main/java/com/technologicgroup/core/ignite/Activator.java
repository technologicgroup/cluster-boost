package com.technologicgroup.core.ignite;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@AllArgsConstructor
public class Activator {

  @Lazy
  private final IgniteCluster cluster;

  @Lazy
  private final ApplicationContext context;

}
