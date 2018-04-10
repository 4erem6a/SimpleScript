package com.evg.ss.values;

import com.evg.ss.util.builders.SSMapBuilder;

/**
 * @author 4erem6a
 */
public enum Types {

    Class,
    Object,
    Number,
    String,
    Boolean,
    Array,
    Function,
    Type,
    Map,
    Null,
    Undefined;

    public static MapValue TYPE_MAP = new SSMapBuilder()
            .setField("CLASS", Class.getTypeValue())
            .setField("OBJECT", Object.getTypeValue())
            .setField("NUMBER", Number.getTypeValue())
            .setField("STRING", String.getTypeValue())
            .setField("BOOLEAN", Boolean.getTypeValue())
            .setField("ARRAY", Array.getTypeValue())
            .setField("FUNCTION", Function.getTypeValue())
            .setField("TYPE", Type.getTypeValue())
            .setField("MAP", Map.getTypeValue())
            .setField("NULL", Null.getTypeValue())
            .setField("UNDEFINED", Undefined.getTypeValue())
            .build();

    public TypeValue getTypeValue() {
        return new TypeValue(this);
    }
}