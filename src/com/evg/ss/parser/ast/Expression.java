package com.evg.ss.parser.ast;

import com.evg.ss.values.Value;

public abstract class Expression extends Node {

    public abstract Value eval();

}