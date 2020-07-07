package com.technologicgroup.boost.chain.mock;

import com.technologicgroup.boost.core.ClusterTask;

public class TestFailClusterTask implements ClusterTask<String, Integer> {
  @Override
  public Integer run(String arg) {
    throw new RuntimeException("Failure");
  }
}
