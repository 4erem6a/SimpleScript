package com.evg.ss.lib.containers;

import com.evg.ss.exceptions.execution.FunctionAlreadyExistsException;
import com.evg.ss.exceptions.execution.FunctionNotFoundException;
import com.evg.ss.lib.Function;

import java.util.HashMap;
import java.util.Map;

public final class FunctionMap implements ScopedMap<String, Function> {

    private Map<String, Function> map = new HashMap<>();
    private FunctionMap parent = null;

    public FunctionMap(FunctionMap parent) {
        this.parent = parent;
    }

    public boolean contains(String name) {
        return map.containsKey(name);
    }

    public Function get(String key) {
        if (map.containsKey(key))
            return map.get(key);
        else if (parent != null)
            return parent.get(key);
        else return null;
    }

    public void set(String key, Function value) {
        if (map.containsKey(key)) {
            map.remove(key);
            map.put(key, value);
        } else if (parent != null) parent.set(key, value);
        else throw new FunctionNotFoundException(key);
    }

    public void put(String key, Function value) {
        if (map.containsKey(key))
            throw new FunctionAlreadyExistsException(key);
        map.put(key, value);
    }

    public FunctionMap getParent() {
        return parent;
    }
}