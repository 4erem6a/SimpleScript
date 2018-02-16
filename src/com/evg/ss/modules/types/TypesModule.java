package com.evg.ss.modules.types;

import com.evg.ss.modules.SSExports;
import com.evg.ss.values.Type;
import com.evg.ss.values.Value;

@SSExports("types")
public final class TypesModule {

    @SSExports("NUMBER")
    public static final Value TYPE_NUMBER = Type.Number.getTypeValue();

    @SSExports("STRING")
    public static final Value TYPE_STRING = Type.String.getTypeValue();

    @SSExports("BOOLEAN")
    public static final Value TYPE_BOOLEAN = Type.Boolean.getTypeValue();

    @SSExports("OBJECT")
    public static final Value TYPE_OBJECT = Type.Object.getTypeValue();

    @SSExports("ARRAY")
    public static final Value TYPE_ARRAY = Type.Array.getTypeValue();

    @SSExports("MAP")
    public static final Value TYPE_MAP = Type.Map.getTypeValue();

    @SSExports("FUNCTION")
    public static final Value TYPE_FUNCTION = Type.Function.getTypeValue();

    @SSExports("TYPE")
    public static final Value TYPE_TYPE = Type.Type.getTypeValue();

    @SSExports("CLASS")
    public static final Value TYPE_CLASS = Type.Class.getTypeValue();

    @SSExports("NULL")
    public static final Value TYPE_NULL = Type.Null.getTypeValue();

    @SSExports("UNDEFINED")
    public static final Value TYPE_UNDEFINED = Type.Undefined.getTypeValue();
}