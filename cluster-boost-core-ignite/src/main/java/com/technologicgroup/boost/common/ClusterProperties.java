package com.technologicgroup.boost.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ClusterProperties {
  @Value("${cluster.startupTimeout:60000}")
  private int startupTimeout;

  @Value("${cluster.hosts}")
  private String[] hosts;
}
