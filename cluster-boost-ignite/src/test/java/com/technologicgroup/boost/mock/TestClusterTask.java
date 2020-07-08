package com.technologicgroup.boost.mock;

import com.technologicgroup.boost.core.ClusterTask;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestClusterTask implements ClusterTask<String, Integer> {

  @Override
  public Integer run(String arg) {
    if (arg == null) {
      throw new RuntimeException();
    }
    return Integer.valueOf(arg);
  }
}
