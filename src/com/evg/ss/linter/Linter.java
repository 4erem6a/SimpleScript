package com.evg.ss.linter;

import com.evg.ss.SimpleScript;
import com.evg.ss.linter.visitors.InvalidImportExpressionValidator;
import com.evg.ss.linter.visitors.RequirementDoesNotExistsValidator;
import com.evg.ss.parser.ast.Statement;
import com.evg.ss.parser.visitors.Visitor;

public final class Linter {

    private static final Visitor[] LINTERS = new Visitor[]{
            //new VariableUsedWithoutBeingDeclaredValidator(),
            new InvalidImportExpressionValidator(),
            new RequirementDoesNotExistsValidator()
    };

    private final SimpleScript.CompiledScript script;

    public Linter(Statement program) {
        this.script = SimpleScript.fromProgram(program);
    }

    public Linter(SimpleScript.CompiledScript script) {
        this.script = script;
    }

    public void lint() throws LintException {
        for (Visitor linter : LINTERS)
            script.acceptVisitor(linter);
    }

}