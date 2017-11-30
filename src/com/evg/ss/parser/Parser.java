package com.evg.ss.parser;

import com.evg.ss.ast.*;
import com.evg.ss.exceptions.UnknownCharacterException;
import com.evg.ss.exceptions.InvalidInterpolationException;
import com.evg.ss.exceptions.UnexpectedTokenException;
import com.evg.ss.lexer.Token;
import com.evg.ss.lexer.TokenType;
import com.evg.ss.values.NullValue;
import com.evg.ss.values.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 4erem6a
 */
public final class Parser {

    private static final Token EOF = new Token(TokenType.EOF, "");

    private final List<Token> tokens;
    private final int size;

    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.size = tokens.size();
    }

    public Statement parse() {
        final BlockStatement program = new BlockStatement();
        while (!match(TokenType.EOF)) {
            program.addStatement(statement());
            match(TokenType.Sc);
        }
        return program;
    }

    public Expression express() {
        return expression();
    }

    private Statement statementOrBlock() {
        if (lookMatch(0, TokenType.Lb))
            return block();
        return statement();
    }

    private Statement block() {
        consume(TokenType.Lb);
        final BlockStatement block = new BlockStatement();
        while (!match(TokenType.Rb)) {
            block.addStatement(statement());
            match(TokenType.Sc);
        }
        return block;
    }

    private Statement statement() {
        if (match(TokenType.Let)) {
            return let();
        } else if (match(TokenType.If)) {
            return ifElse();
        } else if (match(TokenType.Print)) {
            return new PrintStatement(expression());
        } else if (match(TokenType.For)) {
            return forStatement();
        } else if (match(TokenType.While)) {
            return whileStatement();
        } else if (match(TokenType.Do)) {
            return doWhileStatement();
        } else if (match(TokenType.Break)) {
            return new BreakStatement();
        } else if (match(TokenType.Continue)) {
            return new ContinueStatement();
        } else if (match(TokenType.Block)) {
            return block();
        } else if (match(TokenType.Import)) {
            return module();
        } else if (lookMatch(0, TokenType.Word) && lookMatch(1, TokenType.Lp)) {
            return new FunctionStatement((FunctionExpression) function());
        } else {
            return assignment();
        }
    }

    private Statement module() {
        consume(TokenType.Al);
        final String name = consume(TokenType.Word).getValue();
        consume(TokenType.Ar);
        return new ImportStatement(name);
    }

    private Statement doWhileStatement() {
        final Statement body = statementOrBlock();
        consume(TokenType.While);
        consume(TokenType.Lp);
        final Expression condition = expression();
        consume(TokenType.Rp);
        return new DoWhileStatement(condition, body);
    }

    private Statement whileStatement() {
        consume(TokenType.Lp);
        final Expression condition = expression();
        consume(TokenType.Rp);
        final Statement body = statementOrBlock();
        return new WhileStatement(condition, body);
    }

    private Statement forStatement() {
        consume(TokenType.Lp);
        UnitedStatement initialization = new UnitedStatement();
        UnitedStatement iteration = new UnitedStatement();
        if (!match(TokenType.Sc)) {
            do
                initialization.addStatement(let());
            while (match(TokenType.Cm));
            consume(TokenType.Sc);
        } else initialization = null;
        Expression condition;
        if (!match(TokenType.Sc)) {
            condition = expression();
            consume(TokenType.Sc);
        } else condition = null;
        if (!match(TokenType.Rp)) {
            do
                iteration.addStatement(assignment());
            while (match(TokenType.Cm));
            consume(TokenType.Rp);
        } else iteration = null;
        final Statement body = statementOrBlock();
        return new ForStatement(initialization, condition, iteration, body);
    }

    private Statement ifElse() {
        consume(TokenType.Lp);
        final Expression condition = expression();
        consume(TokenType.Rp);
        final Statement ifStatement = statementOrBlock();
        final Statement elseStatement;
        if (match(TokenType.Else))
            elseStatement = statementOrBlock();
        else elseStatement = null;
        return new IfStatement(condition, ifStatement, elseStatement);
    }

    private Statement assignment() {
        final String name = consume(TokenType.Word).getValue();
        consume(TokenType.Eq);
        final Expression value = expression();
        return new AssignmentStatement(name, value);
    }

    private Statement let() {
        match(TokenType.Let);
        final boolean isConst = match(TokenType.Const);
        final String name = consume(TokenType.Word).getValue();
        final Expression value;
        if (!isConst) {
            if (match(TokenType.Eq)) {
                value = expression();
            } else value = NullValue.NullExpression;
            return new LetStatement(name, value, false);
        } else {
            consume(TokenType.Eq);
            value = expression();
            return new LetStatement(name, value, true);
        }
    }

    private Expression expression() {
        return ternary();
    }

    private Expression ternary() {
        Expression result = logicalOr();
        while (true) {
            if (match(TokenType.Qm)) {
                final Expression ifTrue = logicalOr();
                consume(TokenType.Cl);
                final Expression ifFalse = logicalOr();
                result = new TernaryExpression(result, ifTrue, ifFalse);
                continue;
            }
            break;
        }
        return result;
    }

    private Expression logicalOr() {
        Expression result = logicalAnd();
        while (true) {
            if (match(TokenType.VbVb)) {
                result = new BinaryExpression("||", result, logicalAnd());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression logicalAnd() {
        Expression result = bitwiseOr();
        while (true) {
            if (match(TokenType.AmAm)) {
                result = new BinaryExpression("&&", result, bitwiseOr());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression bitwiseOr() {
        Expression result = bitwiseXor();
        while (true) {
            if (match(TokenType.Vb)) {
                result = new BinaryExpression("|", result, bitwiseXor());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression bitwiseXor() {
        Expression result = bitwiseAnd();
        while (true) {
            if (match(TokenType.Cr)) {
                result = new BinaryExpression("^", result, bitwiseAnd());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression bitwiseAnd() {
        Expression result = equality();
        while (true) {
            if (match(TokenType.Am)) {
                result = new BinaryExpression("&", result, equality());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression equality() {
        Expression result = comparison();
        while (true) {
            if (match(TokenType.EqEq)) {
                result = new BinaryExpression("==", result, comparison());
                continue;
            }
            if (match(TokenType.ExEq)) {
                result = new BinaryExpression("!=", result, comparison());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression comparison() {
        Expression result = addictive();
        while (true) {
            if (match(TokenType.Al)) {
                result = new BinaryExpression("<", result, addictive());
                continue;
            }
            if (match(TokenType.Ar)) {
                result = new BinaryExpression(">", result, addictive());
                continue;
            }
            if (match(TokenType.AlEq)) {
                result = new BinaryExpression("<=", result, addictive());
                continue;
            }
            if (match(TokenType.ArEq)) {
                result = new BinaryExpression(">=", result, addictive());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression addictive() {
        Expression result = multiplicative();
        while (true) {
            if (match(TokenType.Pl)) {
                result = new BinaryExpression("+", result, multiplicative());
                continue;
            }
            if (match(TokenType.Mn)) {
                result = new BinaryExpression("-", result, multiplicative());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression multiplicative() {
        Expression result = unary();
        while (true) {
            if (match(TokenType.St)) {
                result = new BinaryExpression("*", result, unary());
                continue;
            }
            if (match(TokenType.Sl)) {
                result = new BinaryExpression("/", result, unary());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression unary() {
        if (match(TokenType.Mn)) {
            return new UnaryExpression("-", primary());
        }
        if (match(TokenType.Pl)) {
            return new UnaryExpression("+", primary());
        }
        return primary();
    }

    private Expression primary() {
        final Token current = get(0);
        if (match(TokenType.Number)) {
            return new ValueExpression(Double.parseDouble(current.getValue()));
        } else if (match(TokenType.Nan)) {
            return new ValueExpression(Double.NaN);
        } else if (match(TokenType.String)) {
            return new ValueExpression(current.getValue());
        } else if (match(TokenType.InterpolatedString)) {
            return interpolated();
        } else if (match(TokenType.True)) {
            return new ValueExpression(true);
        } else if (match(TokenType.False)) {
            return new ValueExpression(false);
        } else if (match(TokenType.Null)) {
            return new ValueExpression();
        } else if (match(TokenType.Let)) {
            return letExpression();
        } else if (lookMatch(0, TokenType.Word) && lookMatch(1, TokenType.Lp)) {
            return function();
        } else if (match(TokenType.Word)) {
            return new VariableExpression(current.getValue());
        } else if (match(TokenType.Lp)) {
            Expression result = expression();
            match(TokenType.Rp);
            return result;
        } else throw new UnexpectedTokenException(current);
    }

    private Expression function() {
        final String name = consume(TokenType.Word).getValue();
        consume(TokenType.Lp);
        final List<Expression> args = new ArrayList<>();
        if (!match(TokenType.Rp)) {
            do args.add(expression()); while (match(TokenType.Cm));
            consume(TokenType.Rp);
        }
        return new FunctionExpression(name, args.toArray(new Expression[args.size()]));
    }

    private Expression interpolated() {
        final Token token = get(-1);
        final String string = token.getValue();
        try {
            return new InterpolatedStringExpression(string);
        } catch (UnknownCharacterException e) {
            throw new InvalidInterpolationException(token.getPosition(), string);
        }
    }

    private Expression letExpression() {
        final String name = consume(TokenType.Word).getValue();
        final Expression value;
        if (match(TokenType.Eq)) {
            value = expression();
        } else value = NullValue.NullExpression;
        return new LetExpression(name, value);
    }

    private boolean match(TokenType type) {
        if (!lookMatch(0, type)) return false;
        pos++;
        return true;
    }

    private boolean lookMultiMatch(int pos, TokenType... types) {
        for (TokenType type : types)
            if (lookMatch(pos, type))
                return true;
        return false;
    }

    private boolean lookMatch(int pos, TokenType type) {
        return compareTokenType(get(pos), type);
    }

    private Token get(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= size) return EOF;
        return tokens.get(position);
    }

    private Token consume(TokenType type) {
        final Token current = get(0);
        if (!compareTokenType(current, type))
            throw new UnexpectedTokenException(type, current);
        pos++;
        return current;
    }

    private boolean compareTokenType(Token token, TokenType type) {
        return token.getType() == type;
    }
}