package com.evg.ss.parser;

import com.evg.ss.ast.*;
import com.evg.ss.ast.BinaryExpression.BinaryOperations;
import com.evg.ss.exceptions.UnknownCharacterException;
import com.evg.ss.exceptions.InvalidInterpolationException;
import com.evg.ss.exceptions.UnexpectedTokenException;
import com.evg.ss.lexer.Token;
import com.evg.ss.lexer.TokenType;
import com.evg.ss.values.NullValue;

import java.util.ArrayList;
import java.util.List;

import static com.evg.ss.ast.UnaryExpression.*;

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
        final UnitedStatement program = new UnitedStatement();
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
        } else if (match(TokenType.Function)) {
            return functionDefinition();
        } else if (match(TokenType.Return)) {
            return new ReturnStatement(expression());
        } else if (match(TokenType.Foreach)) {
            return foreach();
        } else if (match(TokenType.Switch)) {
            return switchCase();
        } else if (lookMatch(0, TokenType.Word) && lookMatch(1, TokenType.Lp)) {
            return new FunctionStatement((FunctionCallExpression) function());
        } else {
            return new ExpressionStatement(expression());
        }
    }

    private Statement switchCase() {
        consume(TokenType.Lp);
        final Expression value = expression();
        consume(TokenType.Rp);
        final SwitchStatement switchStatement = new SwitchStatement(value);
        consume(TokenType.Lb);
        while (match(TokenType.Case)) {
            final Expression expression = expression();
            consume(TokenType.Cl);
            final Statement body = statementOrBlock();
            switchStatement.addCase(expression, body);
            match(TokenType.Sc);
        }
        consume(TokenType.Rb);
        return switchStatement;
    }

    private Statement foreach() {
        consume(TokenType.Lp);
        final Statement iteratorDefinition = notEqualsLet();
        consume(TokenType.In);
        final Expression target = express();
        consume(TokenType.Rp);
        final Statement body = statementOrBlock();
        return new ForEachStatement(iteratorDefinition, target, body);
    }

    private Statement functionDefinition() {
        final String name = consume(TokenType.Word).getValue();
        final List<String> argNames = new ArrayList<>();
        consume(TokenType.Lp);
        if (!match(TokenType.Rp))
            do argNames.add(consume(TokenType.Word).getValue());
            while (match(TokenType.Cm));
        match(TokenType.Rp);
        final Statement body;
        if (match(TokenType.Eq)) {
            body = new ReturnStatement(expression());
        } else body = statementOrBlock();
        return new FunctionDefininitionStatement(name, argNames.toArray(new String[argNames.size()]), body);
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
                initialization.addStatement(notConstLet());
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
                iteration.addStatement(new ExpressionStatement(expression()));
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

    private Statement let() {
        match(TokenType.Let);
        final boolean isConst = match(TokenType.Const);
        final String name = consume(TokenType.Word).getValue();
        final Expression value;
        if (!isConst) {
            if (match(TokenType.Lb))
                value = map();
            else if (match(TokenType.Eq)) {
                value = expression();
            } else value = NullValue.NullExpression;
            return new LetStatement(name, value, false);
        } else {
            consume(TokenType.Eq);
            value = expression();
            return new LetStatement(name, value, true);
        }
    }

    private Statement notConstLet() {
        match(TokenType.Let);
        final String name = consume(TokenType.Word).getValue();
        final Expression value;
        if (match(TokenType.Eq)) {
            value = expression();
        } else value = NullValue.NullExpression;
        return new LetStatement(name, value, false);
    }

    private Statement notEqualsLet() {
        match(TokenType.Let);
        final String name = consume(TokenType.Word).getValue();
        return new LetStatement(name);
    }

    private Expression expression() {
        return low();
    }

    private Expression low() {
        Expression result = logicalOr();
        while (true) {
            if (match(TokenType.Qm)) {
                final Expression ifTrue = logicalOr();
                consume(TokenType.Cl);
                final Expression ifFalse = logicalOr();
                result = new TernaryExpression(result, ifTrue, ifFalse);
                continue;
            }
            if (match(TokenType.Eq)) {
                result = new AssignmentExpression(result, logicalOr());
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
                result = new BinaryExpression(BinaryOperations.LogicalOr, result, logicalAnd());
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
                result = new BinaryExpression(BinaryOperations.LogicalAnd, result, bitwiseOr());
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
                result = new BinaryExpression(BinaryOperations.BitwiseOr, result, bitwiseXor());
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
                result = new BinaryExpression(BinaryOperations.BitwiseXor, result, bitwiseAnd());
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
                result = new BinaryExpression(BinaryOperations.BitwiseAnd, result, equality());
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
                result = new BinaryExpression(BinaryOperations.Equals, result, comparison());
                continue;
            }
            if (match(TokenType.ExEq)) {
                result = new BinaryExpression(BinaryOperations.NotEquals, result, comparison());
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
                result = new BinaryExpression(BinaryOperations.LessThen, result, addictive());
                continue;
            }
            if (match(TokenType.Ar)) {
                result = new BinaryExpression(BinaryOperations.GreaterThen, result, addictive());
                continue;
            }
            if (match(TokenType.AlEq)) {
                result = new BinaryExpression(BinaryOperations.LessThenOrEquals, result, addictive());
                continue;
            }
            if (match(TokenType.ArEq)) {
                result = new BinaryExpression(BinaryOperations.GreaterThenOrEquals, result, addictive());
                continue;
            }
            if (match(TokenType.Is)) {
                final String type = consume().getValue();
                return new BinaryExpression(BinaryOperations.Equals, new TypeofExpression(result), new ConstTypeExpression(type));
            }
            break;
        }
        return result;
    }

    private Expression addictive() {
        Expression result = multiplicative();
        while (true) {
            if (match(TokenType.Pl)) {
                result = new BinaryExpression(BinaryOperations.Addition, result, multiplicative());
                continue;
            }
            if (match(TokenType.Mn)) {
                result = new BinaryExpression(BinaryOperations.Subtraction, result, multiplicative());
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
                result = new BinaryExpression(BinaryOperations.Multiplication, result, unary());
                continue;
            }
            if (match(TokenType.Sl)) {
                result = new BinaryExpression(BinaryOperations.Division, result, unary());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression unary() {
        if (match(TokenType.Mn)) {
            return new UnaryExpression(UnaryOperations.UnaryMinus, postfix());
        }
        if (match(TokenType.Pl)) {
            return new UnaryExpression(UnaryOperations.UnaryPlus, postfix());
        }
        if (match(TokenType.PlPl)) {
            return new UnaryExpression(UnaryOperations.PrefixIncrement, postfix());
        }
        if (match(TokenType.MnMn)) {
            return new UnaryExpression(UnaryOperations.PrefixDecrement, postfix());
        }
        return postfix();
    }

    private Expression postfix() {
        Expression result = primary();
        while (true) {
            if (match(TokenType.Dt)) {
                final Expression field = mapAccessKey();
                result = new MapAccessExpression(result, field);
                continue;
            }
            if (match(TokenType.Lc)) {
                final List<Expression> indices = new ArrayList<>();
                do {
                    indices.add(expression());
                    consume(TokenType.Rc);
                } while (match(TokenType.Lc));
                result = new ArrayAccessExpression(result, indices.toArray(new Expression[indices.size()]));
                continue;
            }
            if (match(TokenType.Lp)) {
                final List<Expression> args = new ArrayList<>();
                if (!match(TokenType.Rp)) {
                    do args.add(expression()); while (match(TokenType.Cm));
                    consume(TokenType.Rp);
                }
                final Expression[] argsArray = args.toArray(new Expression[args.size()]);
                result = new FunctionCallExpression(result, argsArray);
                continue;
            }
            if (match(TokenType.PlPl)) {
                result = new UnaryExpression(UnaryOperations.PostfixIncrement, result);
                continue;
            }
            if (match(TokenType.MnMn)) {
                result = new UnaryExpression(UnaryOperations.PostfixDecrement, result);
                continue;
            }
            break;
        }
        return result;
    }

    private Expression mapAccessKey() {
        final Expression field;
        if (lookMatch(0, TokenType.Word))
            field = new ValueExpression(consume(TokenType.Word).getValue());
        else {
            consume(TokenType.Lp);
            field = expression();
            consume(TokenType.Rp);
        }
        return field;
    }

    private Expression mapDefinitionKey() {
        final Expression field;
        if (lookMatch(0, TokenType.Word) && lookMatch(1, TokenType.Cl))
            field = new ValueExpression(consume(TokenType.Word).getValue());
        else field = expression();
        return field;
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
        } else if (match(TokenType.Typeof)) {
            return typeof();
        } else if (match(TokenType.Type)) {
            return type();
        } else if (match(TokenType.Function)) {
            return anonymousFunction();
        } else if (match(TokenType.Lc)) {
            return array();
        } else if (isLambdaDefinition()) {
            return lambda();
        } else if (match(TokenType.Lb)) {
            return map();
        } else if (match(TokenType.Word)) {
            return new VariableExpression(current.getValue());
        } else if (match(TokenType.Lp)) {
            Expression result = expression();
            match(TokenType.Rp);
            return result;
        } else {
            throw new UnexpectedTokenException(current);
        }
    }

    private Expression lambda() {
        consume(TokenType.Lp);
        final List<String> argNames = new ArrayList<>();
        if (!match(TokenType.Rp)) {
            do {
                argNames.add(consume(TokenType.Word).getValue());
            } while (match(TokenType.Cm));
            consume(TokenType.Rp);
        }
        consume(TokenType.MnAr);
        final Statement body = new ReturnStatement(expression());
        return new AnonymousFunctionExpression(argNames.toArray(new String[argNames.size()]), body);
    }

    private boolean isLambdaDefinition() {
        boolean result = true;
        int rollback = 1;
        if (!match(TokenType.Lp)) return false;
        if (!lookMatch(0, TokenType.Rp)) {
            do {
                rollback++;
                result = result && match(TokenType.Word);
                rollback++;
            } while (match(TokenType.Cm));
            rollback--;
        } else {
            if (match(TokenType.Rp)) {
                rollback++;
                if (match(TokenType.MnAr))
                    rollback++;
                else result = false;
            } else result = false;
        }
        pos -= rollback;
        return result;
    }

    private Expression map() {
        final MapExpression map = new MapExpression();
        if (!match(TokenType.Rb)) {
            do {
                if (match(TokenType.Rb))
                    return map;
                final Expression key = mapDefinitionKey();
                final Expression value = match(TokenType.Cl) ? expression() : NullValue.NullExpression;
                map.addField(key, value);
            } while (match(TokenType.Cm));
            consume(TokenType.Rb);
        }
        if (match(TokenType.Extends)) {
            final Expression base = expression();
            map.setBase(base);
        }
        return map;
    }

    private Expression type() {
        consume(TokenType.Lp);
        final String type = consume().getValue();
        consume(TokenType.Rp);
        return new ConstTypeExpression(type);
    }

    private Expression typeof() {
        consume(TokenType.Lp);
        final Expression expression = expression();
        consume(TokenType.Rp);
        return new TypeofExpression(expression);
    }

    private Expression anonymousFunction() {
        consume(TokenType.Lp);
        final List<String> argNames = new ArrayList<>();
        if (!match(TokenType.Rp)) {
            do {
                argNames.add(consume(TokenType.Word).getValue());
            } while (match(TokenType.Cm));
            consume(TokenType.Rp);
        }
        final Statement body;
        if (match(TokenType.Eq)) {
            body = new ReturnStatement(expression());
        } else body = statementOrBlock();
        return new AnonymousFunctionExpression(argNames.toArray(new String[argNames.size()]), body);
    }

    private Expression array() {
        if (match(TokenType.Rc))
            return new ArrayExpression();
        final List<Expression> expressions = new ArrayList<>();
        do expressions.add(expression()); while (match(TokenType.Cm));
        consume(TokenType.Rc);
        return new ArrayExpression(expressions.toArray(new Expression[expressions.size()]));
    }

    private Expression function() {
        final String name = consume(TokenType.Word).getValue();
        consume(TokenType.Lp);
        final List<Expression> args = new ArrayList<>();
        if (!match(TokenType.Rp)) {
            do args.add(expression()); while (match(TokenType.Cm));
            consume(TokenType.Rp);
        }
        return new FunctionCallExpression(new VariableExpression(name), args.toArray(new Expression[args.size()]));
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

    private Token consume() {
        final Token current = get(0);
        pos++;
        return current;
    }

    private boolean compareTokenType(Token token, TokenType type) {
        return token.getType() == type;
    }
}