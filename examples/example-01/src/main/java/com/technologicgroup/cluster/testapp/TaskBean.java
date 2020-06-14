package com.technologicgroup.cluster.testapp;

import com.technologicgroup.boost.core.ClusterTask;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskBean implements ClusterTask<Integer, Boolean> {
  @Override
  public Boolean run(Integer arg) {
    return arg > 0;
  }
}
