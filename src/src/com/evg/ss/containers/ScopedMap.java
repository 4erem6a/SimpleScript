package com.evg.ss.containers;

public interface ScopedMap<TKey, TValue> {

    TValue get(TKey key);

    boolean contains(TKey key);

    void set(TKey key, TValue value);

    void put(TKey key, TValue value);

    ScopedMap<TKey, TValue> getParent();

}