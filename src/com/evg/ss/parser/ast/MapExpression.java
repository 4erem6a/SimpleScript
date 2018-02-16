package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.InvalidValueTypeException;
import com.evg.ss.lib.SS;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

import java.util.HashMap;
import java.util.Map;

public final class MapExpression extends Expression {

    private Map<Expression, Expression> map = new HashMap<>();
    private Expression base = null;

    public void addField(Expression key, Expression value) {
        map.put(key, value);
    }

    public void setBase(Expression expression) {
        this.base = expression;
    }

    @Override
    public Value eval() {
        final MapValue map;
        if (base != null) {
            final Value baseValue = base.eval();
            if (!(baseValue instanceof MapValue))
                throw new InvalidValueTypeException(baseValue.getType());
            else map = new MapValue((MapValue) baseValue);
        } else map = new MapValue();
        SS.CallContext.up(map);
        for (Map.Entry<Expression, Expression> entry : this.map.entrySet()) {
            map.set(entry.getKey().eval(), entry.getValue().eval());
        }
        SS.CallContext.down();
        return map;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public Map<Expression, Expression> getMap() {
        return map;
    }

    @Override
    public int hashCode() {
        return map.hashCode()
                ^ (base == null ? 1 : base.hashCode())
                ^ (26 * 20 * 31);
    }
}