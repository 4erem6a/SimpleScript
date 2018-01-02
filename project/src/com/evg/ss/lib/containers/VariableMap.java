package com.evg.ss.lib.containers;

import com.evg.ss.exceptions.execution.ConstantAccessException;
import com.evg.ss.exceptions.execution.VariableAlreadyExistsException;
import com.evg.ss.exceptions.execution.VariableNotFoundException;
import com.evg.ss.lib.Variable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class VariableMap implements ScopedMap<String, Variable> {

    private Map<String, Variable> map = new HashMap<>();
    private VariableMap parent = null;

    public VariableMap(VariableMap parent) {
        this.parent = parent;
    }

    public boolean contains(String name) {
        return map.containsKey(name);
    }

    public Variable get(String key) {
        if (map.containsKey(key))
            return map.get(key);
        else if (parent != null)
            return parent.get(key);
        else return null;
    }

    public void set(String key, Variable value) {
        if (map.containsKey(key)) {
            final Variable var = map.get(key);
            if (var.isConst())
                throw new ConstantAccessException(key);
            map.remove(key);
            map.put(key, new Variable(value.getValue(), false));
        } else if (parent != null) parent.set(key, value);
        else throw new VariableNotFoundException(key);
    }

    public void put(String key, Variable value) {
        if (map.containsKey(key))
            throw new VariableAlreadyExistsException(key);
        map.put(key, value);
    }

    public VariableMap getParent() {
        return parent;
    }

    @Override
    public Set<Map.Entry<String, Variable>> entrySet() {
        return map.entrySet();
    }
}