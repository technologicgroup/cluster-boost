package com.technologicgroup.boost.core;

import java.util.Map;
import java.util.Set;

/**
 * Data access (Repository) interface to cluster data stored as key/value pairs
 * @param <K> key class
 * @param <V> value class
 */
public interface ClusterRepository<K, V> {

  /**
   * Returns cluster group that contains the data that current repository can access
   * @return cluster group - a list of nodes containing repository data
   */
  ClusterGroup getClusterGroup();

  /**
   * Returns cluster group that contains specified keys and related values from current repository
   * @return cluster group
   */
  ClusterGroup getClusterGroup(Set<K> keys);

  /**
   * Gets a value associated with a key from local node
   * @param key object
   * @return a value associated with a key
   */
  V getLocal(K key);

  /**
   * Gets a key/value map form local node
   * @param keys a set of keys
   * @return a map of key/value pairs associated with given keys
   */
  Map<K, V> getLocal(Set<K> keys);

  /**
   * Gets all repository data from local npde
   * @return a map of key/value pairs
   */
  Map<K, V> getAllLocal();

  /**
   * Puts a key/value pair to the repository.
   * Repository cannot guarantee that the pair will be stored on a current local node
   * @param key object
   * @param value object
   */
  void put(K key, V value);

  /**
   * Puts a key/value pair map to the repository.
   * Repository cannot guarantee that the map will be stored on a current local node
   * @param map a key/value map
   */
  void putAll(Map<K, V> map);

  /**
   * Gets a data storage name that can be used to direct data access
   * @return a name of the data storage (f.e. in case of Ignite it is a cache name)
   */
  String getName();

}
