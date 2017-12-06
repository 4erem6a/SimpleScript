package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.InvalidValueTypeException;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

import java.util.HashMap;
import java.util.Map;

public final class MapExpression implements Expression {

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
        for (Map.Entry<Expression, Expression> entry : this.map.entrySet())
            map.put(entry.getKey().eval(), entry.getValue().eval());
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
}