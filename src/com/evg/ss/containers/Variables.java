package com.evg.ss.containers;

import com.evg.ss.lib.Variable;
import com.evg.ss.values.Value;

/**
 * @author 4erem6a
 */
public final class Variables {

    private static VariableMap top = new VariableMap(null);

    private Variables() {}

    public static boolean exists(String name) {
        return get(name) != null;
    }

    public static boolean existsTop(String name) {
        return top.contains(name);
    }

    public static Value getValue(String name) {
        final Variable var = top.get(name);
        return var != null ? var.getValue() : null;
    }

    public static Variable get(String name) {
        return top.get(name);
    }

    public static void set(String name, Value value) {
        top.set(name, new Variable(value));
    }

    public static void put(String name, Value value, boolean isConst) {
        top.put(name, new Variable(value, isConst));
    }

    public static void up() {
        top = new VariableMap(top);
    }

    public static void down() {
        if (top.getParent() != null)
            top = top.getParent();
    }
}