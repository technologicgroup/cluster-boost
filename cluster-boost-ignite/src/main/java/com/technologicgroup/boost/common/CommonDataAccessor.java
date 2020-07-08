package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.ClusterDataAccessor;
import lombok.RequiredArgsConstructor;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;

import javax.cache.Cache;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Ignite data accessor implementation
 * Get operations can be local but in general it is slow network operation
 * @param <K> key class
 * @param <V> value class
 */
@RequiredArgsConstructor
public abstract class CommonDataAccessor<K, V> implements ClusterDataAccessor<K, V> {

  private final CommonRepository<K, V> repository;

  /**
   * Gets a value associated with a key from the cluster VIA network
   * This operation can be local but in general it is slow network operation
   * @param key object
   * @return a value associated with a key
   */
  @Override
  public V networkGet(K key) {
    return repository.cache().get(key);
  }

  /**
   * Gets a key/value map form the cluster VIA network
   * This operation can be local but in general it is slow network operation
   * @param keys a set of keys
   * @return a map of key/value pairs associated with given keys
   */
  @Override
  public Map<K, V> networkGet(Set<K> keys) {
    return repository.cache().getAll(keys);
  }

  /**
   * Gets all key/value pairs form the cluster VIA network
   * @return all cluster data as a key/value pairs map
   */
  @Override
  public Map<K, V> networkGetAll() {
    return find(v -> true);
  }

  /**
   * Puts a key/value pair to the repository associated with a service.
   * Repository cannot guarantee that the pair will be stored on a current local node
   * @param key object
   * @param value object
   */
  @Override
  public void put(K key, V value) {
    repository.put(key, value);
  }

  /**
   * Puts a key/value pair map to the repository associated with a service.
   * Repository cannot guarantee that the map will be stored on a current local node
   * @param map a key/value map
   */
  @Override
  public void putAll(Map<K, V> map) {
    repository.putAll(map);
  }

  /**
   * Finds all key/value pairs that satisfies to the predicate
   * Search performs to all nodes in the cluster
   * @param predicate to satisfy
   * @return a key/value map
   */
  @Override
  public Map<K, V> find(Predicate<V> predicate) {
    ScanQuery<K, V> query = new ScanQuery<>((k, v) -> predicate.test(v));

    try (QueryCursor<Cache.Entry<K, V>> cursor = repository.cache().query(query)) {
      return Stream.of(cursor.getAll())
          .flatMap(Collection::stream)
          .collect(Collectors.toMap(Cache.Entry::getKey, Cache.Entry::getValue));
    }
  }
}
