package com.evg.ss.lib;

import com.evg.ss.lib.containers.FunctionMap;
import com.evg.ss.lib.containers.VariableMap;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

public final class SS {

    // FIXME: 16.12.2017 call context
    public static final class CallContext {

        private CallContext parent;
        private MapValue context;

        private CallContext(CallContext parent, MapValue context) {
            this.parent = parent;
            this.context = context;
        }

        private CallContext(MapValue context) {
            this(null, context);
        }

        private static CallContext callContext = null;

        public static void up(MapValue context) {
            callContext = new CallContext(callContext, context);
        }

        public static void down() {
            callContext = callContext.parent;
        }

        public static MapValue get() {
            return callContext == null ? null : callContext.context;
        }
    }

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

        private static FunctionMap top = new FunctionMap(null);

        private Functions() {
        }

        public static boolean exists(String name) {
            return get(name) != null;
        }

        public static boolean existsTop(String name) {
            return top.contains(name);
        }

        public static boolean existsMain(String name) {
            return getMainScope().contains(name);
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

        private static FunctionMap getMainScope() {
            FunctionMap current = top;
            while (current.getParent() != null)
                current = current.getParent();
            return current;
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

        private static VariableMap top = new VariableMap(null);

        private Variables() {
        }

        public static boolean exists(String name) {
            return get(name) != null;
        }

        public static boolean existsTop(String name) {
            return top.contains(name);
        }

        public static boolean existsMain(String name) {
            return getMainScope().contains(name);
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

        private static VariableMap getMainScope() {
            VariableMap current = top;
            while (current.getParent() != null)
                current = current.getParent();
            return current;
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