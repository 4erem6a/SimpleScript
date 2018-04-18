package com.evg.ss.values;

import com.evg.ss.lib.SSFunction;

public class ObjectValue extends MapValue {
    private final ClassValue _class;
    private SSFunction constructor = null;

    public ObjectValue(ClassValue _class) {
        this._class = _class;
    }

    public ObjectValue(MapValue map, ClassValue _class) {
        super(map);
        this._class = _class;
    }

    public SSFunction getConstructor() {
        return constructor;
    }

    public void setConstructor(SSFunction constructor) {
        this.constructor = constructor;
    }

    public ClassValue getSSClass() {
        return _class;
    }

    public boolean isInstanceOfClass(ClassValue _class) {
        return this._class.equals(_class) || _class.getBase() != null && isInstanceOfClass(_class.getBase());
    }

    @Override
    public Value clone() {
        return new ObjectValue(((MapValue) super.clone()), _class);
    }
}