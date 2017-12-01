package com.evg.ss.ast;

import com.evg.ss.exceptions.inner.SSBreakException;
import com.evg.ss.values.Value;

import java.util.ArrayList;
import java.util.List;

public class SwitchStatement implements Statement {

    private static final class Case {
        private Expression value;
        private Statement body;

        Case(Expression value, Statement body) {
            this.value = value;
            this.body = body;
        }

        public Expression getValue() {
            return value;
        }

        public Statement getBody() {
            return body;
        }
    }

    private Expression value;
    private List<Case> cases = new ArrayList<>();

    public SwitchStatement(Expression value) {
        this.value = value;
    }

    public void addCase(Expression value, Statement body) {
        cases.add(new Case(value, body));
    }

    @Override
    public void execute() {
        final Value value = this.value.eval();
        for (Case c : cases) {
            final Value caseValue = c.getValue().eval();
            if (value.compareTo(caseValue) == 0)
                try {
                    c.getBody().execute();
                } catch (SSBreakException e) {
                    break;
                }
        }
    }
}