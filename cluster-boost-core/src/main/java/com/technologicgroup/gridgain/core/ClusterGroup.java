package com.technologicgroup.gridgain.core;

import java.util.List;

import lombok.Value;

@Value
public class ClusterGroup {
  private List<String> nodes;
}
