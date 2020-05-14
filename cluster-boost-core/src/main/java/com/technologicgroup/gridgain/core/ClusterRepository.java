package com.technologicgroup.gridgain.core;

import java.util.Map;
import java.util.Set;

public interface ClusterRepository<K, V> {

  ClusterGroup getClusterGroup(ClusterRepository<K, V> repository);
  ClusterGroup getClusterGroup(ClusterRepository<K, V> repository, K key);

  V getLocal(K key);
  Map<K, V> getAllLocal(Set<K> keys);

  void put(K key, V value);
  void putAll(Map<K, V> map);

  String getName();

}
