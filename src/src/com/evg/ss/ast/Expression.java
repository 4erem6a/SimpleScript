package com.evg.ss.ast;

import com.evg.ss.values.Value;

public interface Expression extends Node {

    Value eval();

}