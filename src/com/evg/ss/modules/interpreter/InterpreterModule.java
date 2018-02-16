package com.evg.ss.modules.interpreter;

import com.evg.ss.SimpleScript;
import com.evg.ss.lib.CallStack;
import com.evg.ss.lib.SS;
import com.evg.ss.modules.SSExports;
import com.evg.ss.modules.SSModule;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.*;

import java.util.Deque;

@SSExports("interpreter")
public class InterpreterModule {

    @SSExports("version")
    public static final Value _Version = Value.of(SimpleScript.VERSION.toString());

    @SSExports("Scopes")
    public static final Value _Scopes = new SSMapBuilder()
            .setMethod("up", InterpreterModule::scopesUp)
            .setMethod("down", InterpreterModule::scopesDown)
            .setMethod("getLevel", InterpreterModule::scopesGetLevel)
            .setMethod("getCurrent", InterpreterModule::scopesGet)
            .build();

    @SSExports("CallContext")
    public static final Value _CallContext = new SSMapBuilder()
            .setMethod("up", InterpreterModule::callContextUp)
            .setMethod("down", InterpreterModule::callContextDown)
            .setMethod("get", InterpreterModule::getCallContext)
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
        if (!Arguments.checkArgTypes(args, Type.Map))
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
        return Value.of(stackTrace.stream().map(CallStack.CallInfo::toString).map(Value::of).toArray(Value[]::new));
    }

    @SSExports("variable")
    public static Value variable(Value... args) {
        if (args.length == 1)
            return getVariable(args);
        else if (args.length == 2)
            return setVariable(args);
        return new UndefinedValue();
    }

    private static Value getVariable(Value... args) {
        final String name = args[0].asString();
        final Value value = SS.Identifiers.getValue(name);
        return value == null ? new UndefinedValue() : value;
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
        if (!Arguments.checkArgTypes(args, Type.String))
            return new UndefinedValue();
        final String source = args[0].asString();
        final SimpleScript ss = SimpleScript.fromSource(source);
        return ss.express().eval();
    }
}