package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.exceptions.inner.SSBreakException;
import com.evg.ss.exceptions.inner.SSContinueException;
import com.evg.ss.exceptions.inner.SSInnerException;
import com.evg.ss.lib.SS;
import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.ArrayValue;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.StringValue;
import com.evg.ss.values.Value;

public final class ForEachStatement extends Statement {

    private Statement iteratorDefinition;
    private Expression target;
    private Statement body;

    public ForEachStatement(Statement iteratorDefinition, Expression target, Statement body) {
        this.iteratorDefinition = iteratorDefinition;
        this.target = target;
        this.body = body;
    }

    @Override
    public void execute() {
        SS.Scopes.up();
        iteratorDefinition.execute();
        final String name = ((LetStatement) iteratorDefinition).getName();
        final Value value = target.eval();
        final ArrayValue array;
        if (value instanceof StringValue)
            array = ((StringValue) value).asCharArray();
        else if (value instanceof ArrayValue)
            array = (ArrayValue) value;
        else if (value instanceof MapValue)
            array = ((MapValue) value).toArray();
        else throw new InvalidValueTypeException(value.getType());
        try {
            for (Value iteration : array.getValue()) {
                try {
                    if (value instanceof MapValue) {
                        final MapValue keyValueMap = new SSMapBuilder()
                                .setField("key", ((ArrayValue) iteration).get(Value.of(0)))
                                .setField("value", ((ArrayValue) iteration).get(Value.of(1)))
                                .build();
                        SS.Identifiers.set(name, keyValueMap);
                    } else SS.Identifiers.set(name, iteration);
                    body.execute();
                } catch (SSInnerException e) {
                    if (e instanceof SSBreakException)
                        break;
                    if (e instanceof SSContinueException) {
                        continue;
                    }
                    throw e;
                }
            }
        } finally {
            SS.Scopes.down();
        }
    }

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public Statement getIteratorDefinition() {
        return iteratorDefinition;
    }

    public Expression getTarget() {
        return target;
    }

    public Statement getBody() {
        return body;
    }

    @Override
    public int hashCode() {
        return iteratorDefinition.hashCode() ^ target.hashCode() ^ body.hashCode() ^ (15 * 31 * 31);
    }
}