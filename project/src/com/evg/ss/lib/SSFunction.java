package com.evg.ss.lib;

import com.evg.ss.exceptions.execution.ArgumentCountMismatchException;
import com.evg.ss.exceptions.execution.ArgumentTypeMismatchException;
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

    public SSFunction(MapValue callContext, String name, ArgumentExpression[] args, Statement body) {
        this(callContext, args, body);
        this.name = name;
    }

    public SSFunction(MapValue callContext, ArgumentExpression[] args, Statement body) {
        this.args = Arrays.stream(args).map(ArgumentExpression::getArgument).collect(Collectors.toList());
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
        if (!isVariadic) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].getType() != this.args.get(i).getType() && this.args.get(i).getType() != null)
                    throw new ArgumentTypeMismatchException(this.args.get(i).getType(), args[i].getType());
            }
        } else {
            for (int i = 0; i < args.length; i++) {
                final Argument arg;
                if (i == this.args.size() - 1 && args[i].getType() == Type.Array)
                    if (this.args.get(i).hasType())
                        throw new ArgumentTypeMismatchException(this.args.get(i).getType(), Type.Array);
                    else break;
                if (i >= this.args.size())
                    arg = this.args.get(this.args.size() - 1);
                else arg = this.args.get(i);
                if (args[i].getType() != arg.getType() && arg.getType() != null)
                    throw new ArgumentTypeMismatchException(arg.getType(), args[i].getType());
            }
        }
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
        if (isLocked) {
            final SS.Scopes scopes = SS.Scopes.get();
            SS.Scopes.reset();
            result = execute(argList);
            SS.Scopes.set(scopes);
        } else result = execute(argList);
        execute(argList);
        SS.CallContext.down();
        SS.Scopes.down();
        CallStack.exit();
        return result;
    }

    private Value execute(List<Value> args) {
        for (int i = 0; i < args.size(); i++)
            SS.Variables.put(this.args.get(i).getName(), args.get(i), false);
        try {
            body.execute();
        } catch (SSReturnException e) {
            return e.getValue();
        }
        return new NullValue();
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

    private String getLambdaName() {
        return String.format("$lambda:%d", hashCode());
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    @Override
    public int hashCode() {
        return Type.Function.ordinal() | args.size() | (args.size() > 0 ? args.stream().mapToInt(Argument::hashCode).reduce((a, b) -> a | b).getAsInt() : 0);
    }

    @Override
    public String toString() {
        return String.format("function[%s]:%s", name != null ? name : "$lambda", hashCode());
    }

    public Statement getBody() {
        return body;
    }
}