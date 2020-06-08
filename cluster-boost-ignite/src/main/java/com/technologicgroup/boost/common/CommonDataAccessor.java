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

@RequiredArgsConstructor
public abstract class CommonDataAccessor<K, V> implements ClusterDataAccessor<K, V> {

  private final CommonRepository<K, V> repository;

  @Override
  public V networkGet(K key) {
    return repository.cache().get(key);
  }

  @Override
  public Map<K, V> networkGet(Set<K> keys) {
    return repository.cache().getAll(keys);
}

  @Override
  public void put(K key, V value) {
        repository.put(key, value);
    }

  @Override
  public void putAll(Map<K, V> map) {
    repository.putAll(map);
  }

  @Override
  public Map<K, V> find(Predicate<V> predicate) {
    ScanQuery<K,V> query = new ScanQuery<>((k, v) -> predicate.test(v));

    try (QueryCursor<Cache.Entry<K, V>> cursor = repository.cache().query(query)) {
      return Stream.of(cursor.getAll())
          .flatMap(Collection::stream)
          .collect(Collectors.toMap(Cache.Entry::getKey, Cache.Entry::getValue));
    }
  }
}
