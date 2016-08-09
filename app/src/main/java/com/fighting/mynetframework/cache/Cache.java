package com.fighting.mynetframework.cache;

/**
 * 描述：
 * Created by MaJD on 2016/8/8.
 */

public interface Cache<K, V> {
    V get(K key);

    void put(K key, V value);

    void remove(K key);
}
