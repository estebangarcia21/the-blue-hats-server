package com.thebluehats.server.api.repos;

import java.util.function.Consumer;

public interface Repository<K, T> {
    void add(T model);

    void update(K key, Consumer<T> update);

    void delete(K key);

    T findUnique(K key);
}
