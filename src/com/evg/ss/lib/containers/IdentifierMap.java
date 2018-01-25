package com.evg.ss.lib.containers;

import com.evg.ss.exceptions.execution.ConstantAccessException;
import com.evg.ss.exceptions.execution.IdentifierAlreadyExistsException;
import com.evg.ss.exceptions.execution.IdentifierNotFoundException;
import com.evg.ss.lib.Identifier;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class IdentifierMap implements ScopedMap<String, Identifier> {

    private volatile Map<String, Identifier> map = new ConcurrentHashMap<>();
    private volatile IdentifierMap parent = null;

    public IdentifierMap(IdentifierMap parent) {
        this.parent = parent;
    }

    public synchronized boolean contains(String name) {
        return map.containsKey(name);
    }

    public synchronized Identifier get(String key) {
        if (map.containsKey(key))
            return map.get(key);
        else if (parent != null)
            return parent.get(key);
        else return null;
    }

    public synchronized void set(String key, Identifier value) {
        if (map.containsKey(key)) {
            final Identifier id = map.get(key);
            if (id.isConst())
                throw new ConstantAccessException(key);
            map.remove(key);
            map.put(key, new Identifier(value.getValue(), false));
        } else if (parent != null) parent.set(key, value);
        else throw new IdentifierNotFoundException(key);
    }

    public synchronized void put(String key, Identifier value) {
        if (map.containsKey(key))
            throw new IdentifierAlreadyExistsException(key);
        map.put(key, value);
    }

    public IdentifierMap getParent() {
        return parent;
    }

    @Override
    public Set<Map.Entry<String, Identifier>> entrySet() {
        return map.entrySet();
    }
}