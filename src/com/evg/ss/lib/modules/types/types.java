package com.evg.ss.lib.modules.types;

import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Type;

public final class types extends SSModule {

    @Override
    public MapValue require() {
        final SSMapBuilder builder = SSMapBuilder.create();
        builder.setField("NUMBER", Type.Number.getTypeValue());
        builder.setField("STRING", Type.String.getTypeValue());
        builder.setField("BOOLEAN", Type.Boolean.getTypeValue());
        builder.setField("OBJECT", Type.Object.getTypeValue());
        builder.setField("ARRAY", Type.Array.getTypeValue());
        builder.setField("MAP", Type.Map.getTypeValue());
        builder.setField("FUNCTION", Type.Function.getTypeValue());
        builder.setField("TYPE", Type.Type.getTypeValue());
        builder.setField("CLASS", Type.Class.getTypeValue());
        builder.setField("NULL", Type.Null.getTypeValue());
        builder.setField("UNDEFINED", Type.Undefined.getTypeValue());
        return builder.build();
    }
}