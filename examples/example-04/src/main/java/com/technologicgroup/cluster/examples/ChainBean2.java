package com.technologicgroup.cluster.examples;

import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChainBean2 implements ClusterTask<Integer, String> {

  private final Cluster cluster;

  @Override
  public String run(Integer integer) {
    return String.format("Argument: %d, Node order: %d", integer, cluster.getNodeOrder());
  }
}
