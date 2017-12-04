package com.evg.ss.ast;

import com.evg.ss.lib.SS;
import com.evg.ss.lib.SSFunction;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class FunctionDefinitionStatement implements Statement {

    private String name;
    private String[] argNames;
    private Statement body;

    public FunctionDefinitionStatement(String name, String[] argNames, Statement body) {
        this.name = name;
        this.argNames = argNames;
        this.body = body;
    }

    @Override
    public void execute() {
        SS.Functions.put(name, new SSFunction(Arrays.stream(argNames).collect(Collectors.toList()), body));
    }
}