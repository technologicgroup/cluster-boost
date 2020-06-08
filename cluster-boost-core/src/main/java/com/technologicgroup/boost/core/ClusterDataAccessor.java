package com.technologicgroup.boost.core;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Data access (Repository) interface to cluster data stored as key/value pairs
 * Get operations can be local but in general it is slow network operation
 * @param <K> key class
 * @param <V> value class
 */
public interface ClusterDataAccessor<K, V> {
    /**
     * Gets a value associated with a key from the cluster VIA network
     * This operation can be local but in general it is slow network operation
     * @param key object
     * @return a value associated with a key
     */
    V networkGet(K key);

    /**
     * Gets a key/value map form the cluster VIA network
     * This operation can be local but in general it is slow network operation
     * @param keys a set of keys
     * @return a map of key/value pairs associated with given keys
     */
    Map<K, V> networkGet(Set<K> keys);

    /**
     * Puts a key/value pair to the repository associated with a service.
     * Repository cannot guarantee that the pair will be stored on a current local node
     * @param key object
     * @param value object
     */
    void put(K key, V value);

    /**
     * Puts a key/value pair map to the repository associated with a service.
     * Repository cannot guarantee that the map will be stored on a current local node
     * @param map a key/value map
     */
    void putAll(Map<K, V> map);

    /**
     * Finds all key/value pairs that satisfies to the predicate
     * Search performs to all nodes in the cluster
     * @param predicate to satisfy
     * @return a key/value map
     */
    Map<K, V> find(Predicate<V> predicate);
}
