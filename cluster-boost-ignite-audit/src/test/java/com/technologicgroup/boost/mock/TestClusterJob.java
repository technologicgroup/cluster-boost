package com.technologicgroup.boost.mock;

import com.technologicgroup.boost.core.ClusterJob;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestClusterJob implements ClusterJob<Integer> {

  public static final int EXCEPTION_CODE = 10101;

  private final Integer result;

  @Override
  public Integer run() {
    if (result == EXCEPTION_CODE) {
      throw new RuntimeException(TestClusterTask.MESSAGE);
    }
    return result;
  }
}
