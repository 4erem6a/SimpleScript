package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.inner.SSBreakException;
import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

import java.util.ArrayList;
import java.util.List;

public class SwitchStatement extends Statement {

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

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public Expression getValue() {
        return value;
    }

    public List<Case> getCases() {
        return cases;
    }

    @Override
    public int hashCode() {
        return value.hashCode() ^ cases.hashCode() ^ (34 * 12 * 31);
    }

    @Override
    public String toString() {
        return new MSCGenerator(this).generate();
    }

    public static final class Case {
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

        @Override
        public int hashCode() {
            return value.hashCode() ^ body.hashCode();
        }
    }
}