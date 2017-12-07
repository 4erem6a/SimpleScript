package com.evg.ss.lib;

import com.evg.ss.lib.containers.FunctionMap;
import com.evg.ss.lib.containers.VariableMap;
import com.evg.ss.values.Value;

public final class SS {

    public static final class Scopes {

        private Scopes() {
        }

        public static void up() {
            Variables.up();
            Functions.up();
        }

        public static void down() {
            Functions.down();
            Variables.down();
        }

    }

    public static final class Functions {

        public static final Functions INSTANCE = new Functions();

        private static FunctionMap top = new FunctionMap(null);

        private Functions() {
        }

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

    public static final class Variables {

        public static final Variables INSTANCE = new Variables();

        private static VariableMap top = new VariableMap(null);

        private Variables() {
        }

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
}