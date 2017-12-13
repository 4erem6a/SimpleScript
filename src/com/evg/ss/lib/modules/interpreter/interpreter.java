package com.evg.ss.lib.modules.interpreter;

import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.execution.FunctionExecutionException;
import com.evg.ss.lib.CallStack;
import com.evg.ss.lib.Function;
import com.evg.ss.lib.SS;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.*;

import java.util.Deque;

public class interpreter extends SSModule {

    @Override
    public MapValue require() {
        final SSMapBuilder scopes = SSMapBuilder.create();
        final SSMapBuilder interpreter = SSMapBuilder.create();
        scopes.setField("CURRENT", Value.of(0));
        scopes.setField("GLOBAL", Value.of(1));
        scopes.setField("MAIN", Value.of(2));
        scopes.setMethod("up", this::scopesUp);
        scopes.setMethod("down", this::scopesDown);
        interpreter.setField("scopes", scopes.build());
        interpreter.setField("version", Value.of(SimpleScript.VERSION.toString()));
        interpreter.setMethod("eval", this::eval);
        interpreter.setMethod("getVariable", this::getVariable);
        interpreter.setMethod("setVariable", this::setVariable);
        interpreter.setMethod("variableExists", this::variableExists);
        interpreter.setMethod("functionExists", this::functionExists);
        interpreter.setMethod("setFunction", this::setFunction);
        interpreter.setMethod("getFunction", this::getFunction);
        interpreter.setMethod("requireStackTrace", this::requireStackTrace);
        return interpreter.build();
    }

    private Value requireStackTrace(Value... values) {
        Arguments.checkArgcOrDie(values, 0);
        final Deque<CallStack.CallInfo> stackTrace = CallStack.getCalls();
        return Value.of(stackTrace.stream().map(CallStack.CallInfo::toString).map(Value::of).toArray(Value[]::new));
    }

    private Value getFunction(Value... values) {
        Arguments.checkArgTypesOrDie(values, StringValue.class);
        final String name = values[0].asString();
        return Value.of(SS.Functions.get(name));
    }

    private Value setFunction(Value... values) {
        Arguments.checkArgTypesOrDie(values, StringValue.class, FunctionValue.class);
        final String name = values[0].asString();
        final Function function = ((FunctionValue) values[1]).getValue();
        SS.Functions.put(name, function);
        return new NullValue();
    }

    private Value getVariable(Value... values) {
        Arguments.checkArgTypesOrDie(values, StringValue.class);
        final String name = values[0].asString();
        return SS.Variables.getValue(name);
    }

    private Value setVariable(Value... values) {
        Arguments.checkArgTypesOrDie(values, StringValue.class, null);
        final String name = values[0].asString();
        final Value value = values[1];
        SS.Variables.set(name, value);
        return new NullValue();
    }

    private Value variableExists(Value... values) {
        Arguments.checkArgTypesOrDie(values, StringValue.class, NumberValue.class);
        final String name = values[0].asString();
        final int scopeNumber = values[1].asNumber().intValue();
        if (scopeNumber < 0 || scopeNumber >= Scopes.values().length)
            throw new FunctionExecutionException("Invalid scope.");
        final Scopes scope = Scopes.values()[scopeNumber];
        switch (scope) {
            case CURRENT:
                return Value.of(SS.Variables.existsTop(name));
            case GLOBAL:
                return Value.of(SS.Variables.exists(name));
            case MAIN:
                return Value.of(SS.Variables.existsMain(name));
            default:
                return Value.of(false);
        }
    }

    private Value functionExists(Value... values) {
        Arguments.checkArgTypesOrDie(values, StringValue.class, NumberValue.class);
        final String name = values[0].asString();
        final int scopeNumber = values[1].asNumber().intValue();
        if (scopeNumber < 0 || scopeNumber >= Scopes.values().length)
            throw new FunctionExecutionException("Invalid scope.");
        final Scopes scope = Scopes.values()[scopeNumber];
        switch (scope) {
            case CURRENT:
                return Value.of(SS.Functions.existsTop(name));
            case GLOBAL:
                return Value.of(SS.Functions.exists(name));
            case MAIN:
                return Value.of(SS.Functions.existsMain(name));
            default:
                return Value.of(false);
        }
    }

    private Value scopesUp(Value... values) {
        Arguments.checkArgcOrDie(values, 0);
        SS.Scopes.up();
        return new NullValue();
    }

    private Value scopesDown(Value... values) {
        Arguments.checkArgcOrDie(values, 0);
        SS.Scopes.down();
        return new NullValue();
    }

    private Value eval(Value... args) {
        Arguments.checkArgTypesOrDie(args, StringValue.class);
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