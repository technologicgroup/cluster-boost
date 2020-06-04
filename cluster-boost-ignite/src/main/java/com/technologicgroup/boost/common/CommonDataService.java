package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.ClusterDataService;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public abstract class CommonDataService<K, V> implements ClusterDataService<K, V> {

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
}
