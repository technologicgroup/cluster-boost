package com.technologicgroup.boost.core;

import java.util.Set;

import lombok.Value;

/**
 * ClusterGroup represents a cluster group as a set of node identifiers
 */
@Value
public class ClusterGroup {
  Set<String> nodes;
}
