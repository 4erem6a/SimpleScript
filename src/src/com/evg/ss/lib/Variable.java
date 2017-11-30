package com.evg.ss.lib;

import com.evg.ss.values.Value;

/**
 * @author 4erem6a
 */
public final class Variable {
    private Value value;
    private boolean isConst;

    public Variable(Value value) {
        this(value, false);
    }

    public Variable(Value value, boolean isConst) {
        this.value = value;
        this.isConst = isConst;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public boolean isConst() {
        return isConst;
    }

    public void setConst(boolean aConst) {
        isConst = aConst;
    }
}