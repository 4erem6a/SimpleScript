package com.evg.ss.lib.modules.interpreter;

import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.execution.FunctionExecutionException;
import com.evg.ss.lib.CallStack;
import com.evg.ss.lib.SS;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.*;

import java.util.Deque;

public class interpreter extends SSModule {

    @Override
    public MapValue require() {
        final SSMapBuilder callContext = new SSMapBuilder();
        final SSMapBuilder scopes = new SSMapBuilder();
        final SSMapBuilder builder = new SSMapBuilder();
        callContext.setMethod("up", this::callContextUp);
        callContext.setMethod("down", this::callContextDown);
        callContext.setMethod("get", this::getCallContext);
        scopes.setField("CURRENT", Value.of(0));
        scopes.setField("GLOBAL", Value.of(1));
        scopes.setField("MAIN", Value.of(2));
        scopes.setMethod("up", this::scopesUp);
        scopes.setMethod("down", this::scopesDown);
        scopes.setMethod("getCurrent", this::scopesGet);
        scopes.setMethod("getLevel", this::scopesGetLevel);
        builder.setField("scopes", scopes.build());
        builder.setField("callContext", callContext.build());
        builder.setField("version", Value.of(SimpleScript.VERSION.toString()));
        builder.setMethod("eval", this::eval);
        builder.setMethod("getVariable", this::getVariable);
        builder.setMethod("setVariable", this::setVariable);
        builder.setMethod("variableExists", this::variableExists);
        builder.setMethod("requireStackTrace", this::requireStackTrace);
        builder.setMethod("loadModule", this::_loadModule);
        builder.setMethod("loadModules", this::_loadModules);
        builder.setMethod("isModuleLoaded", this::_isModuleLoaded);
        return builder.build();
    }

    private Value _isModuleLoaded(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        return Value.of(SSModule.isModuleExists(args[0].asString()));
    }

    private Value _loadModules(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        try {
            SSModule.loadModulesByPath(args[0].asString());
        } catch (Exception ignored) {
        }
        return new UndefinedValue();
    }

    private Value _loadModule(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        try {
            SSModule.loadModuleByPath(args[0].asString());
        } catch (Exception ignored) {
        }
        return new UndefinedValue();
    }

    private Value scopesGetLevel(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        return Value.of(SS.Scopes.getCurrentLevel());
    }

    private Value scopesGet(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        final SSMapBuilder variables = new SSMapBuilder();
        SS.Scopes.get().getIdentifiers().entrySet().forEach(entry -> variables.setField(entry.getKey(), entry.getValue().getValue()));
        return variables.build();
    }

    private Value callContextUp(Value... args) {
        if (!Arguments.checkArgTypes(args, Type.Map))
            return new UndefinedValue();
        SS.CallContext.up(((MapValue) args[0]));
        return new NullValue();
    }

    private Value callContextDown(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        SS.CallContext.down();
        return new NullValue();
    }

    private Value getCallContext(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        final MapValue context = SS.CallContext.get();
        return (context == null ? new NullValue() : context);
    }

    private Value requireStackTrace(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        final Deque<CallStack.CallInfo> stackTrace = CallStack.getCalls();
        return Value.of(stackTrace.stream().map(CallStack.CallInfo::toString).map(Value::of).toArray(Value[]::new));
    }

    private Value getVariable(Value... args) {
        if (!Arguments.checkArgTypes(args, Type.String))
            return new UndefinedValue();
        final String name = args[0].asString();
        return SS.Identifiers.getValue(name);
    }

    private Value setVariable(Value... args) {
        if (!Arguments.checkArgTypes(args, Type.String, null))
            return new UndefinedValue();
        final String name = args[0].asString();
        final Value value = args[1];
        SS.Identifiers.set(name, value);
        return new NullValue();
    }

    private Value variableExists(Value... args) {
        if (!Arguments.checkArgTypes(args, Type.String, Type.Number))
            return new UndefinedValue();
        final String name = args[0].asString();
        final int scopeNumber = args[1].asNumber().intValue();
        if (scopeNumber < 0 || scopeNumber >= Scopes.values().length)
            throw new FunctionExecutionException("Invalid scope.");
        final Scopes scope = Scopes.values()[scopeNumber];
        switch (scope) {
            case CURRENT:
                return Value.of(SS.Identifiers.existsTop(name));
            case GLOBAL:
                return Value.of(SS.Identifiers.exists(name));
            case MAIN:
                return Value.of(SS.Identifiers.existsMain(name));
            default:
                return Value.of(false);
        }
    }

    private Value scopesUp(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        SS.Scopes.up();
        return new NullValue();
    }

    private Value scopesDown(Value... args) {
        Arguments.checkArgcOrDie(args, 0);
        SS.Scopes.down();
        return new NullValue();
    }

    private Value eval(Value... args) {
        if (!Arguments.checkArgTypes(args, Type.String))
            return new UndefinedValue();
        final String source = args[0].asString();
        final SimpleScript ss = SimpleScript.fromSource(source);
        return ss.express().eval();
    }

    private enum Scopes {
        CURRENT,
        GLOBAL,
        MAIN
    }
}