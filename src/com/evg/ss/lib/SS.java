package com.evg.ss.lib;

import com.evg.ss.lib.containers.IdentifierMap;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

public final class SS {

    public static final class CallContext {

        private static CallContext callContext = null;
        private CallContext parent;
        private MapValue context;

        private CallContext(CallContext parent, MapValue context) {
            this.parent = parent;
            this.context = context;
        }

        private CallContext(MapValue context) {
            this(null, context);
        }

        public static void up(MapValue context) {
            callContext = new CallContext(callContext, context);
        }

        public static void down() {
            callContext = callContext.parent;
        }

        public static MapValue get() {
            return callContext == null ? null : callContext.context;
        }

        public static void set(MapValue context) {
            callContext.context.setMap(context);
        }
    }

    public static final class Scopes {

        private final IdentifierMap identifiers;

        public Scopes(IdentifierMap identifiers) {
            this.identifiers = identifiers;
        }

        public static void up() {
            Identifiers.up();
        }

        public static void down() {
            Identifiers.down();
        }

        public static void reset() {
            Identifiers.set(new IdentifierMap(null));
        }

        public static Scopes get() {
            return new Scopes(Identifiers.get());
        }

        public static void set(Scopes scopes) {
            Identifiers.set(scopes.identifiers);
        }

        public static Scopes lock() {
            final Scopes scopes = get();
            reset();
            return scopes;
        }

        public static int getCurrentLevel() {
            int level = 0;
            IdentifierMap temp = Identifiers.top;
            while (temp.getParent() != null) {
                temp = temp.getParent();
                level++;
            }
            return level;
        }

        public static void unlock(Scopes scopes) {
            set(scopes);
        }

        public IdentifierMap getIdentifiers() {
            return identifiers;
        }
    }

    public static final class Identifiers {

        private static IdentifierMap top = new IdentifierMap(null);

        private Identifiers() {
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
            final Identifier var = top.get(name);
            return var != null ? var.getValue() : null;
        }

        public static Identifier get(String name) {
            return top.get(name);
        }

        public static void set(String name, Value value) {
            top.set(name, new Identifier(value));
        }

        public static void put(String name, Value value, boolean isConst) {
            top.put(name, new Identifier(value, isConst));
        }

        private static IdentifierMap getMainScope() {
            IdentifierMap current = top;
            while (current.getParent() != null)
                current = current.getParent();
            return current;
        }

        public static void up() {
            top = new IdentifierMap(top);
        }

        public static void down() {
            if (top.getParent() != null)
                top = top.getParent();
        }

        public static IdentifierMap get() {
            return top;
        }

        public static void set(IdentifierMap top) {
            Identifiers.top = top;
        }
    }
}