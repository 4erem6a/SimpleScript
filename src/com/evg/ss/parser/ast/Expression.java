package com.evg.ss.parser.ast;

import com.evg.ss.values.Value;

public interface Expression extends Node {

    Value eval();

}