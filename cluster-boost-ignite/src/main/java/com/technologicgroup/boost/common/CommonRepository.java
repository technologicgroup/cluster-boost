package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.ClusterGroup;
import com.technologicgroup.boost.core.ClusterRepository;

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

/**
 * Ignite repository implementation
 * @param <K> key class
 * @param <V> value class
 */
public abstract class CommonRepository<K, V> implements ClusterRepository<K, V> {

  private CacheConfiguration<K, V> configuration;
  private Ignite ignite;

  /**
   * Gets a data storage name that can be used to direct data access
   * @return a name of the data storage (f.e. in case of Ignite it is a cache name)
   */
  @Override
  public String getName() {
    return getKeyClass().getSimpleName() + getValueClass().getSimpleName() + "Cache";
  }

  /**
   * Returns cluster group that contains the data that current repository can access
   * @return cluster group - a list of nodes containing repository data
   */
  @Override
  public ClusterGroup getClusterGroup() {
    org.apache.ignite.cluster.ClusterGroup
        clusterGroup = ignite().cluster().forCacheNodes(cache().getName());

    Set<String> ids = clusterGroup.nodes()
            .stream()
            .map(ClusterNode::id)
            .map(UUID::toString)
            .collect(Collectors.toSet());

    return new ClusterGroup(ids);
  }

  /**
   * Returns cluster group that contains specified keys and related values from current repository
   * @return cluster group
   */
  @Override
  public ClusterGroup getClusterGroup(Set<K> keys) {
    Affinity<K> affinity = ignite().affinity(cache().getName());

    Set<String> ids = keys.stream().map(affinity::mapKeyToNode)
            .map(ClusterNode::id)
            .map(UUID::toString)
            .collect(Collectors.toSet());

    return new ClusterGroup(ids);
  }

  /**
   * Gets a value associated with a key from local node
   * @param key object
   * @return a value associated with a key
   */
  @Override
  public V getLocal(K key) {
    for (Cache.Entry<K, V> entry : getAllLocalIterable()) {
      if (entry.getKey().equals(key)) {
        return entry.getValue();
      }
    }
    return null;
  }

  /**
   * Gets a key/value map form local node
   * @param keys a set of keys
   * @return a map of key/value pairs associated with given keys
   */
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

  /**
   * Gets all repository data from local node
   * @return a map of key/value pairs
   */
  @Override
  public Map<K, V> getAllLocal() {
    Map<K, V> result = new HashMap<>();

    for (Cache.Entry<K, V> entry : getAllLocalIterable()) {
      result.put(entry.getKey(), entry.getValue());
    }
    return result;
  }

  /**
   * Puts a key/value pair to the repository.
   * Repository cannot guarantee that the pair will be stored on a current local node
   * @param key object
   * @param value object
   */
  @Override
  public void put(K key, V value) {
    cache().put(key, value);
  }

  /**
   * Puts a key/value pair map to the repository.
   * Repository cannot guarantee that the map will be stored on a current local node
   * @param map a key/value map
   */
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

  synchronized Ignite ignite() {
    if (ignite == null) {
      ignite = ContextHolder.getContext().getBean(Ignite.class);
    }
    return ignite;
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
    return ignite().cache(getConfiguration().getName());
  }

  private Iterable<Cache.Entry<K, V>> getAllLocalIterable() {
    return cache().localEntries(CachePeekMode.ALL);
  }

}
