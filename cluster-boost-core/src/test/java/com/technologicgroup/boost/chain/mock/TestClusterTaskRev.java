package com.technologicgroup.boost.chain.mock;

import com.technologicgroup.boost.core.ClusterTask;

public class TestClusterTaskRev implements ClusterTask<Integer, String> {
  @Override
  public String run(Integer arg) {
    return String.valueOf(arg);
  }
}
