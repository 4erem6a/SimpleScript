package com.evg.ss.parser.ast;

import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

public final class ClassDefenitionStatement implements Statement {



    @Override
    public void execute() {

    }

    @Override
    public void accept(Visitor visitor) {

    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return null;
    }

}