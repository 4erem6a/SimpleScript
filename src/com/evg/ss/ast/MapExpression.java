package com.evg.ss.ast;

import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public final class MapExpression implements Expression {

    private boolean isStatic;
    private Map<String, Pair<Expression, Boolean>> map = new HashMap<>();

    public MapExpression(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public void addField(String name, Boolean isStatic, Expression expression) {
        map.put(name, new Pair<>(expression, isStatic));
    }

    @Override
    public Value eval() {
        final MapValue map = new MapValue(isStatic);
        for (Map.Entry<String, Pair<Expression, Boolean>> entry : this.map.entrySet())
            map.putField(entry.getKey(), entry.getValue().getKey().eval(), entry.getValue().getValue());
        return map;
    }
}