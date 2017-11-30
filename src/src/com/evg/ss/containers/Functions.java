package com.evg.ss.containers;

import com.evg.ss.lib.Function;

/**
 * @author 4erem6a
 */
public final class Functions {

    private static FunctionMap top = new FunctionMap(null);

    private Functions() {}

    public static boolean exists(String name) {
        return get(name) != null;
    }

    public static boolean existsTop(String name) {
        return top.contains(name);
    }

    public static Function get(String name) {
        return top.get(name);
    }

    public static void set(String name, Function value) {
        top.set(name, value);
    }

    public static void put(String name, Function value) {
        top.put(name, value);
    }

    public static void up() {
        top = new FunctionMap(top);
    }

    public static void down() {
        if (top.getParent() != null)
            top = top.getParent();
    }
}