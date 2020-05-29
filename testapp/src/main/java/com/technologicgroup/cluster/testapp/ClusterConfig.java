package com.technologicgroup.cluster.testapp;

import com.technologicgroup.boost.core.Cluster;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@AllArgsConstructor
public class ClusterConfig {

  private final Cluster cluster;
  private final ClusterReadyListener readyListener;

  @PostConstruct
  public void init() {
    cluster.setOnClusterReadyListener(readyListener);
  }
}
