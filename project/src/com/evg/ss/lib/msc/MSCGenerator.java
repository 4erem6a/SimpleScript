package com.evg.ss.lib.msc;

import com.evg.ss.SimpleScript;
import com.evg.ss.parser.ast.Expression;
import com.evg.ss.parser.ast.ExpressionStatement;
import com.evg.ss.parser.ast.Statement;

public final class MSCGenerator {

    private SimpleScript.CompiledScript script;

    public MSCGenerator(SimpleScript.CompiledScript script) {
        this.script = script;
    }

    public MSCGenerator(Statement astRoot) {
        this.script = SimpleScript.fromProgram(astRoot);
    }

    public MSCGenerator(Expression expression) {
        this.script = SimpleScript.fromProgram(new ExpressionStatement(expression));
    }

    public String generate() {
        return script.acceptResultVisitor(new MSCVisitor());
    }

}