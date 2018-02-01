package com.evg.ss.lib;

import com.evg.ss.exceptions.execution.ArgumentCountMismatchException;
import com.evg.ss.exceptions.execution.UnexpectedDefaultArgumentException;
import com.evg.ss.exceptions.execution.UnexpectedVariadicArgumentException;
import com.evg.ss.exceptions.inner.SSReturnException;
import com.evg.ss.parser.ast.ArgumentExpression;
import com.evg.ss.parser.ast.Expression;
import com.evg.ss.parser.ast.Statement;
import com.evg.ss.values.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class SSFunction implements ConstructorFunction {

    private final List<Argument> args;
    private final Statement body;
    private String name = null;
    private MapValue callContext;
    private boolean isLocked = false;

    public SSFunction(final SSFunction ssFunction) {
        this.args = ssFunction.args;
        this.body = ssFunction.body;
        this.name = ssFunction.name;
        this.callContext = ssFunction.callContext;
        this.isLocked = ssFunction.isLocked;
    }

    public SSFunction(MapValue callContext, ArgumentExpression[] args, Statement body) {
        this(callContext, Arrays.stream(args).map(ArgumentExpression::getArgument).toArray(Argument[]::new), body);
    }

    public SSFunction(MapValue callContext, Argument[] args, Statement body) {
        this.args = Arrays.stream(args).collect(Collectors.toList());
        this.body = body;
        for (int i = 0; i < this.args.size() - 2; i++)
            if (this.args.get(i).hasValue() && !this.args.get(i + 1).hasValue())
                throw new UnexpectedDefaultArgumentException();
        for (int i = 0; i < this.args.size(); i++)
            if (this.args.get(i).isVariadic() && i != this.args.size() - 1)
                throw new UnexpectedVariadicArgumentException();
        this.callContext = callContext == null ? new MapValue() : callContext;
    }

    private void tryValidateArgs(Value... args) {
        final boolean isVariadic = this.args.stream().anyMatch(Argument::isVariadic);
        final int minArgc = (int) this.args.stream().filter(arg -> !arg.hasValue() && !arg.isVariadic()).count();
        final int maxArgc = (isVariadic ? args.length : this.args.size());
        if (args.length < minArgc || args.length > maxArgc)
            throw new ArgumentCountMismatchException(args.length, args.length < minArgc ? minArgc : maxArgc);
    }

    public boolean validateArgs(Value... args) {
        try {
            tryValidateArgs(args);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Value execute(Value... args) {
        tryValidateArgs(args);
        final List<Value> argList = processArguments(args);
        CallStack.enter(name == null ? "$lambda" : name, this);
        SS.Scopes.up();
        SS.CallContext.up(callContext);
        final Value result;
        try {
            if (isLocked) {
                final SS.Scopes scopes = SS.Scopes.lock();
                try {
                    result = execute(argList);
                } finally {
                    SS.Scopes.unlock(scopes);
                }
            } else result = execute(argList);
        } finally {
            SS.CallContext.down();
            SS.Scopes.down();
            CallStack.exit();
        }
        return result;
    }

    private Value execute(List<Value> args) {
        for (int i = 0; i < args.size(); i++)
            SS.Identifiers.put(this.args.get(i).getName(), args.get(i), false);
        try {
            body.execute();
        } catch (SSReturnException e) {
            return e.getValue();
        }
        return new UndefinedValue();
    }

    private List<Value> processArguments(Value[] args) {
        final List<Value> argList = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (this.args.get(i).isVariadic() && args[i].getType() != Type.Array) {
                final List<Value> variadicArgList = new ArrayList<>();
                for (; i < args.length; i++)
                    variadicArgList.add(args[i]);
                argList.add(Value.of(variadicArgList.toArray(new Value[0])));
            } else argList.add(args[i]);
        }
        if (argList.size() != this.args.size()) {
            final int idx = this.args.size() - (this.args.size() - argList.size());
            for (int i = idx; i < this.args.size(); i++) {
                final Argument arg = this.args.get(i);
                final Expression argValue = arg.getValue();
                if (argValue != null)
                    argList.add(argValue.eval());
                else argList.add(new ArrayValue());
            }
        }
        return argList;
    }

    @Override
    public MapValue executeAsNew(Value... args) {
        final MapValue context = this.callContext;
        this.callContext = new MapValue();
        execute(args);
        final MapValue result = this.callContext;
        this.callContext = context;
        return result;
    }

    public MapValue getCallContext() {
        return callContext;
    }

    public void setCallContext(MapValue callContext) {
        this.callContext = callContext;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    @Override
    public int hashCode() {
        return args.hashCode()
                ^ body.hashCode()
                ^ Boolean.hashCode(isLocked);
    }

    @Override
    public String toString() {
        return String.format("function[%s]:%s", name != null ? name : "$lambda", hashCode());
    }

    public Statement getBody() {
        return body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Argument> getArgs() {
        return args;
    }

    @Override
    public SSFunction clone() {
        return new SSFunction(this);
    }
}