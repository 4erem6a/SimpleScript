package com.evg.ss.lib;

import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

public interface ConstructorFunction extends Function {

    MapValue executeAsNew(Value... args);

}