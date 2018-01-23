package com.evg.ss.exceptions.parser;

import com.evg.ss.parser.ast.Expression;
import com.evg.ss.parser.ast.Statement;

public class InvalidLockException extends SSParserException {
    public InvalidLockException(Statement statement) {
        super(String.format("Failed to lock not lockable statement: %s.", statement.getClass().getSimpleName()));
    }

    public InvalidLockException(Expression expression) {
        super(String.format("Failed to lock not lockable expression: %s.", expression.getClass().getSimpleName()));
    }

    public InvalidLockException(String string) {
        super(string);
    }

    public InvalidLockException() {
        super("Multiple lock not supported.");
    }
}