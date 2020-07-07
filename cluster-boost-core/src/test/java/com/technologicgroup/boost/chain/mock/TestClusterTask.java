package com.technologicgroup.boost.chain.mock;

import com.technologicgroup.boost.core.ClusterTask;

public class TestClusterTask implements ClusterTask<String, Integer> {
  @Override
  public Integer run(String arg) {
    return arg.length();
  }
}
