package com.evg.ss.modules.interpreter;

import com.evg.ss.SimpleScript;
import com.evg.ss.lib.CallStack;
import com.evg.ss.lib.Identifier;
import com.evg.ss.lib.SS;
import com.evg.ss.modules.SSExports;
import com.evg.ss.modules.SSModule;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSArrayBuilder;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.*;

import java.util.Deque;

public final class interpreter {

    @SSExports("version")
    public static final Value _Version = Value.of(SimpleScript.VERSION.toString());

    @SSExports("Scopes")
    public static final Value _Scopes = new SSMapBuilder()
            .setMethod("up", interpreter::scopesUp)
            .setMethod("down", interpreter::scopesDown)
            .setMethod("level", interpreter::scopesGetLevel)
            .setMethod("current", interpreter::scopesGet)
            .build();

    @SSExports("CallContext")
    public static final Value _CallContext = new SSMapBuilder()
            .setMethod("up", interpreter::callContextUp)
            .setMethod("down", interpreter::callContextDown)
            .setMethod("get", interpreter::getCallContext)
            .build();

    @SSExports("isModuleLoaded")
    public static Value isModuleLoaded(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        return Value.of(SSModule.isModuleExists(args[0].asString()));
    }

    @SSExports("loadModules")
    public static Value loadModules(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        try {
            SSModule.loadModulesByPath(args[0].asString());
        } catch (Exception ignored) {
        }
        return new UndefinedValue();
    }

    @SSExports("loadModule")
    public static Value loadModule(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        try {
            SSModule.loadModuleByPath(args[0].asString());
        } catch (Exception ignored) {
        }
        return new UndefinedValue();
    }

    @SSExports("listModules")
    public static Value listModules(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        final SSArrayBuilder builder = new SSArrayBuilder();
        for (String name : SSModule.getLoadedModules().keySet())
            builder.setElement(Value.of(name));
        return builder.build();
    }

    private static Value scopesGetLevel(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        return Value.of(SS.Scopes.getCurrentLevel());
    }

    private static Value scopesGet(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        final SSMapBuilder variables = new SSMapBuilder();
        SS.Scopes.get().getIdentifiers().entrySet().forEach(entry -> variables.setField(entry.getKey(), entry.getValue().getValue()));
        return variables.build();
    }

    private static Value callContextUp(Value... args) {
        if (!Arguments.checkArgTypes(args, Types.Map))
            return new UndefinedValue();
        SS.CallContext.up(((MapValue) args[0]));
        return new NullValue();
    }

    private static Value callContextDown(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        SS.CallContext.down();
        return new NullValue();
    }

    private static Value getCallContext(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        final MapValue context = SS.CallContext.get();
        return (context == null ? new NullValue() : context);
    }

    @SSExports("requireStackTrace")
    public static Value requireStackTrace(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        final Deque<CallStack.CallInfo> stackTrace = CallStack.getCalls();
        final SSArrayBuilder trace = new SSArrayBuilder();
        for (CallStack.CallInfo info : stackTrace) {
            trace.setElement(Value.of(info.getFunction()));
        }
        return trace.build();
    }

    @SSExports("variable")
    public static Value variable(Value... args) {
        Arguments.checkArgcOrDie(args, 1, 2);
        if (args.length == 1)
            return getVariable(args);
        else if (args.length == 2)
            return setVariable(args);
        return new UndefinedValue();
    }

    private static Value getVariable(Value... args) {
        final String name = args[0].asString();
        final Identifier identifier = SS.Identifiers.get(name);
        return identifier == null ? new UndefinedValue() : new SSMapBuilder()
                .setField("name", Value.of(name))
                .setField("value", identifier.getValue())
                .setField("isConst", Value.of(identifier.isConst()))
                .build();
    }

    private static Value setVariable(Value... args) {
        final String name = args[0].asString();
        final Value value = args[1];
        SS.Identifiers.set(name, value);
        return new UndefinedValue();
    }

    private static Value scopesUp(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        SS.Scopes.up();
        return new UndefinedValue();
    }

    private static Value scopesDown(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        SS.Scopes.down();
        return new UndefinedValue();
    }

    @SSExports("eval")
    public static Value eval(Value... args) {
        if (!Arguments.checkArgTypes(args, Types.String))
            return new UndefinedValue();
        final String source = args[0].asString();
        final SimpleScript ss = SimpleScript.fromSource(source);
        return ss.express().eval();
    }

    @SSExports("deconst")
    public static Value deconst(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        final String name = args[0].asString();
        if (SS.Identifiers.exists(name))
            SS.Identifiers.get(name).setConst(false);
        return new UndefinedValue();
    }
}