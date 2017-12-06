package com.evg.ss.parser.ast;

import com.evg.ss.values.Value;

public interface Accessible {

    Value get();

    Value set(Value value);

}