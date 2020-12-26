package com.thebluehats.server.api.utils;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class CrudRepository<T, K> {
    private final ArrayList<T> objects = new ArrayList<>();
    private final Function<T, K> indexer;

    public CrudRepository(Function<T, K> indexer) {
        this.indexer = indexer;
    }

    public void add(T object) {
        objects.add(object);
    }

    public T findUnique(K key) {
        for (T object : objects) {
            if (indexer.apply(object) == key) {
                return object;
            }
        }

        return null;
    }

    public void delete(K key) {
        T object = findUnique(key);

        objects.remove(object);
    }

    public void update(K key, Consumer<T> action) {
        T object = findUnique(key);

        action.accept(object);
    }
}
