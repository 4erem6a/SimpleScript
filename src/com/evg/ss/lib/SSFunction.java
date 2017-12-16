package com.evg.ss.lib;

import com.evg.ss.exceptions.execution.ArgumentCountMismatchException;
import com.evg.ss.exceptions.execution.ArgumentTypeMismatchException;
import com.evg.ss.exceptions.execution.UnexpectedDefaultArgumentException;
import com.evg.ss.exceptions.inner.SSReturnException;
import com.evg.ss.parser.ast.ArgumentExpression;
import com.evg.ss.parser.ast.Statement;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.NullValue;
import com.evg.ss.values.Type;
import com.evg.ss.values.Value;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class SSFunction implements ConstructorFunction {

    private final List<Argument> args;
    private final Statement body;
    private String name = null;
    private MapValue callContext;

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
        this.callContext = callContext == null ? new MapValue() : callContext;
    }

    private void tryValidateArgs(Value... args) {
        final int minArgc = (int) this.args.stream().filter(arg -> !arg.hasValue()).count();
        final int maxArgc = this.args.size();
        if (args.length < minArgc || args.length > maxArgc)
            throw new ArgumentCountMismatchException(args.length < minArgc ? minArgc : maxArgc, args.length);
        for (int i = 0; i < args.length; i++) {
            if (args[i].getType() != this.args.get(i).getType() && this.args.get(i).getType() != null)
                throw new ArgumentTypeMismatchException(this.args.get(i).getType(), args[i].getType());
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

    public Statement getBody() {
        return body;
    }

    @Override
    public Value execute(Value... args) {
        tryValidateArgs(args);
        final List<Value> argList = Arrays.stream(args).collect(Collectors.toList());
        if (argList.size() != this.args.size()) {
            final int idx = this.args.size() - (this.args.size() - argList.size());
            for (int i = idx; i < this.args.size(); i++) {
                final Argument arg = this.args.get(i);
                final Value value = arg.getValue().eval();
                argList.add(value);
            }
        }
        CallStack.enter(name == null ? "$lambda" : name, this);
        SS.Scopes.up();
        for (int i = 0; i < argList.size(); i++)
            SS.Variables.put(this.args.get(i).getName(), argList.get(i), false);
        SS.CallContext.up(callContext);
        try {
            body.execute();
        } catch (SSReturnException e) {
            SS.CallContext.down();
            SS.Scopes.down();
            return e.getValue();
        }
        SS.CallContext.down();
        SS.Scopes.down();
        CallStack.exit();
        return new NullValue();
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

    @Override
    public int hashCode() {
        return Type.Function.ordinal() | args.size() | (args.size() > 0 ? args.stream().mapToInt(Argument::hashCode).reduce((a, b) -> a | b).getAsInt() : 0);
    }

    @Override
    public String toString() {
        return String.format("function[%s]:%s", name != null ? name : "$lambda", hashCode());
    }
}