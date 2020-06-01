package com.technologicgroup.boost.core;

import java.util.List;

import lombok.Value;

/**
 * ClusterGroup represents a cluster group as a list of node identifiers
 */
@Value
public class ClusterGroup {
  List<String> nodes;
}
