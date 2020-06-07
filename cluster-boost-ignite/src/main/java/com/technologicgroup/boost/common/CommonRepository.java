package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.ClusterGroup;
import com.technologicgroup.boost.core.ClusterRepository;

import lombok.RequiredArgsConstructor;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

import javax.cache.Cache;

@RequiredArgsConstructor
public abstract class CommonRepository<K, V> implements ClusterRepository<K, V> {

  private CacheConfiguration<K, V> configuration;

  @Override
  public String getName() {
    return getKeyClass().getSimpleName() + getValueClass().getSimpleName() + "Cache";
  }

  @Override
  public ClusterGroup getClusterGroup() {
    org.apache.ignite.cluster.ClusterGroup
        clusterGroup = ContextHolder.getContext().getBean(Ignite.class).cluster().forCacheNodes(cache().getName());

    Set<String> ids = clusterGroup.nodes()
            .stream()
            .map(ClusterNode::id)
            .map(UUID::toString)
            .collect(Collectors.toSet());

    return new ClusterGroup(ids);
  }

  @Override
  public ClusterGroup getClusterGroup(Set<K> keys) {
    Affinity<K> affinity = ContextHolder.getContext().getBean(Ignite.class).affinity(cache().getName());

    Set<String> ids = keys.stream().map(affinity::mapKeyToNode)
            .map(ClusterNode::id)
            .map(UUID::toString)
            .collect(Collectors.toSet());

    return new ClusterGroup(ids);
  }

  @Override
  public V getLocal(K key) {
    for (Cache.Entry<K, V> entry : getAllLocalIterable()) {
      if (entry.getKey().equals(key)) {
        return entry.getValue();
      }
    }
    return null;
  }

  @Override
  public Map<K, V> getLocal(Set<K> keys) {
    Map<K, V> result = new HashMap<>();

    for (Cache.Entry<K, V> entry : getAllLocalIterable()) {
      if (keys.contains(entry.getKey())) {
        result.put(entry.getKey(), entry.getValue());
      }
    }
    return result;
  }

  @Override
  public Map<K, V> getAllLocal() {
    Map<K, V> result = new HashMap<>();

    for (Cache.Entry<K, V> entry : getAllLocalIterable()) {
      result.put(entry.getKey(), entry.getValue());
    }
    return result;
  }

  @Override
  public void put(K key, V value) {
    cache().put(key, value);
  }

  @Override
  public void putAll(Map<K, V> map) {
    cache().putAll(map);
  }

  synchronized CacheConfiguration<K, V> getConfiguration() {
    if (configuration == null) {
      configuration = createCacheConfiguration();
    }
    return configuration;
  }

  protected CacheConfiguration<K, V> createCacheConfiguration() {
    CacheConfiguration<K, V> result = new CacheConfiguration<>();
    result.setCacheMode(CacheMode.PARTITIONED);
    result.setName(getName());
    result.setIndexedTypes(getKeyClass(), getValueClass());

    return result;
  }

  @SuppressWarnings("unchecked")
  private Class<K> getKeyClass() {
    return (Class<K>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  @SuppressWarnings("unchecked")
  private Class<V> getValueClass() {
    return (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
  }

  IgniteCache<K, V> cache() {
    return ContextHolder.getContext().getBean(Ignite.class).cache(getConfiguration().getName());
  }

  private Iterable<Cache.Entry<K, V>> getAllLocalIterable() {
    return cache().localEntries(CachePeekMode.ALL);
  }

}
