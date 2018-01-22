package com.evg.ss.values;

public class ObjectValue extends MapValue {

    private final ClassValue _class;

    public ObjectValue(ClassValue _class) {
        this._class = _class;
    }

    public ClassValue getSSClass() {
        return _class;
    }

    public boolean isInstanceOfClass(ClassValue _class) {
        return this._class.equals(_class) || _class.getBase() != null && isInstanceOfClass(_class.getBase());
    }
}