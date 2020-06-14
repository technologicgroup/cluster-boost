package com.technologicgroup.cluster.examples;

import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChainBean1 implements ClusterTask<String, Integer> {

  private final Cluster cluster;

  @Override
  public Integer run(String arg) {
    return (int) cluster.getNodeOrder() % 2;
  }
}
