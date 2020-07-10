package com.technologicgroup.boost.mock;

import com.technologicgroup.boost.core.ClusterTask;

public class TestTrackableClusterTask implements ClusterTask<TestTrackableArg, Integer> {
  @Override
  public Integer run(TestTrackableArg arg) {
    return null;
  }
}
