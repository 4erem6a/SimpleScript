package com.evg.ss.lib;

import com.evg.ss.exceptions.execution.ArgumentCountMismatchException;
import com.evg.ss.exceptions.execution.UnexpectedVariadicArgumentException;
import com.evg.ss.exceptions.inner.SSReturnException;
import com.evg.ss.parser.ast.Statement;
import com.evg.ss.util.builders.SSArrayBuilder;
import com.evg.ss.values.*;

import java.util.ArrayList;
import java.util.List;

public final class SSFunction implements ConstructorFunction {

    private final Arguments args;
    private final Statement body;
    private String name = null;
    private MapValue callContext;
    private boolean isLocked = false;

    private SSFunction(final SSFunction ssFunction) {
        this.args = ssFunction.args;
        this.body = ssFunction.body;
        this.name = ssFunction.name;
        this.callContext = ssFunction.callContext;
        this.isLocked = ssFunction.isLocked;
    }

    public SSFunction(MapValue callContext, Arguments args, Statement body) {
        this.args = args;
        this.body = body;
        for (int i = 0; i < this.args.size(); i++)
            if (this.args.get(i).isVariadic() && i != this.args.size() - 1)
                throw new UnexpectedVariadicArgumentException();
        this.callContext = callContext == null ? new MapValue() : callContext;
    }

    private void tryValidateArgs(Value... args) {
        final int minArgc = this.args.minArgc();
        final int maxArgc = this.args.maxArgc() == -1 ? args.length : this.args.maxArgc();
        if (args.length < minArgc || args.length > maxArgc)
            throw new ArgumentCountMismatchException(args.length, args.length < minArgc ? minArgc : maxArgc);
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
        }
        CallStack.exit();
        return result;
    }

    private Value execute(List<Value> args) {
        for (int i = 0; i < args.size(); i++) {
            final Argument arg = this.args.get(i);
            SS.Identifiers.put(arg.getName(), args.get(i), arg.isConst());
        }
        try {
            body.execute();
        } catch (SSReturnException e) {
            return e.getValue();
        }
        return new UndefinedValue();
    }

    private List<Value> processArguments(Value[] args) {
        //  (_ D _ D D)
        //  (x x x)
        final List<Value> argList = new ArrayList<>();
        int i = 0;
        for (; i < args.length; i++) {
            if (this.args.get(i).isVariadic() && args[i].getType() != Types.Array) {
                final SSArrayBuilder varArgBuilder = new SSArrayBuilder();
                for (; i < args.length; i++)
                    varArgBuilder.setElement(args[i]);
                argList.add(varArgBuilder.build());
            } else if (this.args.get(i).hasValue() && args[i].getType() == Types.Undefined) {
                argList.add(this.args.get(i).getValue().eval());
            } else {
                argList.add(args[i]);
            }
        }
        for (; i < this.args.size(); i++) {
            if (this.args.get(i).hasValue())
                argList.add(this.args.get(i).getValue().eval());
            else argList.add(new ArrayValue());
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

    public Arguments getArgs() {
        return args;
    }

    @Override
    public SSFunction clone() {
        return new SSFunction(this);
    }
}