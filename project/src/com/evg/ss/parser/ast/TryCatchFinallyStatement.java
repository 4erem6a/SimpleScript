package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.SSThrownException;
import com.evg.ss.lib.SS;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

import java.util.ArrayList;
import java.util.List;

public final class TryCatchFinallyStatement implements Statement {

    private final Statement _try, _finally;
    private final List<Catch> catches;

    public TryCatchFinallyStatement(Statement _try, Statement _finally) {
        this._try = _try;
        this._finally = _finally;
        this.catches = new ArrayList<>();
    }

    public TryCatchFinallyStatement(Statement _try, List<Catch> catches, Statement _finally) {
        this._try = _try;
        this.catches = catches;
        this._finally = _finally;
    }

    public void addCatch(Catch _catch) {
        catches.add(_catch);
    }

    @Override
    public void execute() {
        try {
            _try.execute();
        } catch (SSThrownException e) {
            for (Catch _catch : catches) {
                SS.Variables.put(_catch.argName, e.getValue(), true);
                if (_catch.isAvailable()) {
                    _catch.getBody().execute();
                    break;
                }
            }
        } finally {
            if (_finally != null)
                _finally.execute();
        }
    }

    public Statement getTry() {
        return _try;
    }

    public List<Catch> getCatches() {
        return catches;
    }

    public Statement getFinally() {
        return _finally;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <TResult> TResult accept(ResultVisitor<TResult> visitor) {
        return visitor.visit(this);
    }

    public static class Catch {
        private final String argName;
        private final Expression condition;
        private final Statement body;

        public Catch(String argName, Expression condition, Statement body) {
            this.argName = argName;
            this.condition = condition;
            this.body = body;
        }

        public boolean isAvailable() {
            return (this.condition == null ? true : this.condition.eval().asBoolean());
        }

        public String getArgName() {
            return argName;
        }

        public Expression getCondition() {
            return condition;
        }

        public Statement getBody() {
            return body;
        }
    }
}