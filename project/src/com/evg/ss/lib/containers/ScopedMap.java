package com.evg.ss.lib.containers;

import java.util.Map;
import java.util.Set;

public interface ScopedMap<TKey, TValue> {

    TValue get(TKey key);

    boolean contains(TKey key);

    void set(TKey key, TValue value);

    void put(TKey key, TValue value);

    ScopedMap<TKey, TValue> getParent();

    Set<Map.Entry<TKey, TValue>> entrySet();

}