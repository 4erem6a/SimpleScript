package com.evg.ss.parser;

import com.evg.ss.exceptions.SSException;
import com.evg.ss.exceptions.execution.InvalidInterpolationException;
import com.evg.ss.exceptions.parser.InvalidLockException;
import com.evg.ss.exceptions.parser.ParserException;
import com.evg.ss.exceptions.parser.SSParserException;
import com.evg.ss.exceptions.parser.UnexpectedTokenException;
import com.evg.ss.lexer.Token;
import com.evg.ss.lexer.TokenTypes;
import com.evg.ss.lib.AutoClassGenerator;
import com.evg.ss.lib.CompoundAssignmentProcessor;
import com.evg.ss.lib.Operations;
import com.evg.ss.parser.ast.*;
import com.evg.ss.values.NullValue;
import com.evg.ss.values.Types;
import com.evg.ss.values.UndefinedValue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 4erem6a
 */
public final class Parser extends AbstractParser {

    private BlockStatement program = new BlockStatement();

    public Parser(List<Token> tokens) {
        super(tokens);
    }

    @Override
    public Statement parse() throws SSParserException {
        program = new BlockStatement();
        while (!match(TokenTypes.EOF)) {
            program.addStatement(statement());
            match(TokenTypes.Sc);
        }
        return program;
    }

    @Override
    public Expression express() throws SSParserException {
        return expression();
    }

    private Statement statementOrBlock() {
        if (lookMatch(0, TokenTypes.Lb))
            return block();
        return statement();
    }

    private Statement block() {
        final List<String> mods = new ArrayList<>();
        while (match(TokenTypes.NsNs))
            mods.add(consume(TokenTypes.Word).getValue());
        consume(TokenTypes.Lb);
        final BlockStatement block = new BlockStatement();
        mods.forEach(block::modify);
        while (!match(TokenTypes.Rb)) {
            block.addStatement(statement());
            match(TokenTypes.Sc);
        }
        return block;
    }

    private Statement statement() {
        if (lookMatch(0, TokenTypes.Sc))
            return new ExpressionStatement(UndefinedValue.UndefinedExpression);
        if (match(TokenTypes.Let)) {
            return let();
        } else if (lookMatch(0, TokenTypes.Const)) {
            return let();
        } else if (match(TokenTypes.If)) {
            return ifElse();
        } else if (match(TokenTypes.For)) {
            return forStatement();
        } else if (match(TokenTypes.While)) {
            return whileStatement();
        } else if (match(TokenTypes.Do)) {
            return doWhileStatement();
        } else if (match(TokenTypes.Break)) {
            return new BreakStatement();
        } else if (match(TokenTypes.Continue)) {
            return new ContinueStatement();
        } else if (match(TokenTypes.Block)) {
            return block();
        } else if (match(TokenTypes.Function)) {
            return functionDefinition();
        } else if (match(TokenTypes.Return)) {
            if (lookMatch(0, TokenTypes.Sc))
                return new ReturnStatement(UndefinedValue.UndefinedExpression);
            return new ReturnStatement(expression());
        } else if (match(TokenTypes.Foreach)) {
            return foreach();
        } else if (match(TokenTypes.Switch)) {
            return switchCase();
        } else if (match(TokenTypes.Exports)) {
            return new ExportsStatement(expression());
        } else if (match(TokenTypes.Ar)) {
            return new ExpressionStatement(expression());
        } else if (match(TokenTypes.Import)) {
            return new ImportStatement(expression());
        } else if (match(TokenTypes.Throw)) {
            return new ThrowStatement(expression());
        } else if (match(TokenTypes.Try)) {
            return _try();
        } else if (match(TokenTypes.Class)) {
            return _class();
//        } else if (match(TokenTypes.Delete)) {
//            return delete();
        } else if (match(TokenTypes.Locked)) {
            return lockedStatement();
        } else if (lookMatch(0, TokenTypes.Ex) && lookMatch(1, TokenTypes.NsNs)) {
            consume(TokenTypes.Ex);
            consume(TokenTypes.NsNs);
            program.modify(consume(TokenTypes.Word).getValue());
            return new ExpressionStatement(UndefinedValue.UndefinedExpression);
        } else if (match(TokenTypes.NsNs)) {
            final String mod = consume(TokenTypes.Word).getValue();
            final Statement stmt = statement();
            stmt.modify(mod);
            return stmt;
        } else {
            return new ExpressionStatement(expression());
        }
    }

    private Statement _class() {
        final AnonymousClassExpression _class;
        final String name = consume(TokenTypes.Word).getValue();
        if (lookMatch(0, TokenTypes.Lp))
            _class = (AnonymousClassExpression) autoClass();
        else _class = ((AnonymousClassExpression) anonymousClass());
        return new ClassDefinitionStatement(name, _class);
    }

    private Statement _try() {
        final Statement _try = statementOrBlock();
        final List<TryCatchFinallyStatement.Catch> catches = new ArrayList<>();
        while (match(TokenTypes.Catch)) {
            consume(TokenTypes.Lp);
            final String argName = consume(TokenTypes.Word).getValue();
            consume(TokenTypes.Rp);
            final Expression condition;
            if (match(TokenTypes.If)) {
                match(TokenTypes.Lp);
                condition = expression();
                match(TokenTypes.Rp);
            } else condition = null;
            catches.add(new TryCatchFinallyStatement.Catch(argName, condition, statementOrBlock()));
        }
        final Statement _finally;
        if (match(TokenTypes.Finally))
            _finally = statementOrBlock();
        else _finally = null;
        return new TryCatchFinallyStatement(_try, catches, _finally);
    }

    private Statement lockedStatement() {
        final Statement statement = statement();
        if (statement instanceof Lockable) {
            final Lockable lockable = ((Lockable) statement);
            if (lockable.isLocked())
                throw new InvalidLockException();
            lockable.lock();
        } else throw new InvalidLockException(statement);
        return statement;
    }

    private Statement switchCase() {
        consume(TokenTypes.Lp);
        final Expression value = expression();
        consume(TokenTypes.Rp);
        final SwitchStatement switchStatement = new SwitchStatement(value);
        consume(TokenTypes.Lb);
        while (match(TokenTypes.Case)) {
            final Expression expression = expression();
            consume(TokenTypes.Cl);
            final Statement body = statementOrBlock();
            switchStatement.addCase(expression, body);
            match(TokenTypes.Sc);
        }
        consume(TokenTypes.Rb);
        return switchStatement;
    }

    private Statement foreach() {
        consume(TokenTypes.Lp);
        final Statement iteratorDefinition = simpleLet();
        consume(TokenTypes.In);
        final Expression target = express();
        consume(TokenTypes.Rp);
        final Statement body = statementOrBlock();
        return new ForEachStatement(iteratorDefinition, target, body);
    }

    private ArgumentExpression argument() {
        final boolean isConst = match(TokenTypes.Const);
        final boolean variadic;
        final String name = consume(TokenTypes.Word).getValue();
        final Expression value;
        variadic = match(TokenTypes.DtDtDt);
        if (match(TokenTypes.Eq)) {
            value = expression();
        } else value = null;
        return new ArgumentExpression(name, isConst, variadic, value);
    }

    private Statement functionDefinition() {
        final String name = consume(TokenTypes.Word).getValue();
        final AnonymousFunctionExpression function = ((AnonymousFunctionExpression) anonymousFunction());
        return new FunctionDefinitionStatement(name, function);
    }

    private Expression requireExpression() {
        consume(TokenTypes.Lp);
        final String name = consume(TokenTypes.String).getValue();
        consume(TokenTypes.Rp);
        return new RequireExpression(name);
    }

    private Statement doWhileStatement() {
        final Statement body = statementOrBlock();
        consume(TokenTypes.While);
        consume(TokenTypes.Lp);
        final Expression condition = expression();
        consume(TokenTypes.Rp);
        return new DoWhileStatement(condition, body);
    }

    private Statement whileStatement() {
        consume(TokenTypes.Lp);
        final Expression condition = expression();
        consume(TokenTypes.Rp);
        final Statement body = statementOrBlock();
        return new WhileStatement(condition, body);
    }

    private Statement forStatement() {
        consume(TokenTypes.Lp);
        final Statement initialization = match(TokenTypes.Sc) ? null : statement();
        match(TokenTypes.Sc);
        final Expression condition = match(TokenTypes.Sc) ? null : expression();
        match(TokenTypes.Sc);
        final Statement iteration = match(TokenTypes.Rp) ? null : statement();
        match(TokenTypes.Rp);
        final Statement body = statementOrBlock();
        return new ForStatement(initialization, condition, iteration, body);
    }

    private Statement ifElse() {
        consume(TokenTypes.Lp);
        final Expression condition = expression();
        consume(TokenTypes.Rp);
        final Statement ifStatement = statementOrBlock();
        final Statement elseStatement;
        if (match(TokenTypes.Else))
            elseStatement = statementOrBlock();
        else elseStatement = null;
        return new IfStatement(condition, ifStatement, elseStatement);

    }

    private Statement let() {
        final boolean isConst = match(TokenTypes.Const);
        final UnitedStatement lets = new UnitedStatement();
        do {
            final String name = consume(TokenTypes.Word).getValue();
            final Expression value;
            if (match(TokenTypes.Eq))
                value = expression();
            else value = NullValue.NullExpression;
            lets.addStatement(new LetStatement(name, value, isConst));
        } while (match(TokenTypes.Cm));
        if (lets.size() == 1)
            return lets.first();
        return lets;
    }

    private Statement simpleLet() {
        match(TokenTypes.Let);
        final boolean isConst = match(TokenTypes.Const);
        final String name = consume(TokenTypes.Word).getValue();
        return new LetStatement(name, NullValue.NullExpression, isConst);
    }

    private Expression expression() {
        return assignment();
    }

    private Expression assignment() {
        Expression result = ternary();
        while (true) {
            if (match(TokenTypes.Qm)) {
                final Expression ifTrue = ternary();
                consume(TokenTypes.Cl);
                final Expression ifFalse = ternary();
                result = new TernaryExpression(result, ifTrue, ifFalse);
                continue;
            }
            if (match(TokenTypes.Eq)) {
                result = new AssignmentExpression(result, ternary());
                continue;
            }
            if (match(TokenTypes.PlEq)) {
                result = new CompoundAssignmentProcessor(Operations.Addition, result, ternary()).processBinary();
                continue;
            }
            if (match(TokenTypes.MnEq)) {
                result = new CompoundAssignmentProcessor(Operations.Subtraction, result, ternary()).processBinary();
                continue;
            }
            if (match(TokenTypes.StEq)) {
                result = new CompoundAssignmentProcessor(Operations.Multiplication, result, ternary()).processBinary();
                continue;
            }
            if (match(TokenTypes.SlEq)) {
                result = new CompoundAssignmentProcessor(Operations.Division, result, ternary()).processBinary();
                continue;
            }
            if (match(TokenTypes.PrEq)) {
                result = new CompoundAssignmentProcessor(Operations.Modulus, result, ternary()).processBinary();
                continue;
            }
            if (match(TokenTypes.AmEq)) {
                result = new CompoundAssignmentProcessor(Operations.BitwiseAnd, result, ternary()).processBinary();
                continue;
            }
            if (match(TokenTypes.VbEq)) {
                result = new CompoundAssignmentProcessor(Operations.BitwiseOr, result, ternary()).processBinary();
                continue;
            }
            if (match(TokenTypes.CrEq)) {
                result = new CompoundAssignmentProcessor(Operations.BitwiseXor, result, ternary()).processBinary();
                continue;
            }
            if (match(TokenTypes.AtEq)) {
                result = new CompoundAssignmentProcessor(Operations.ValueClone, result, ternary()).processUnary();
                continue;
            }
            if (match(TokenTypes.ArArEq)) {
                result = new CompoundAssignmentProcessor(Operations.RShift, result, ternary()).processBinary();
                continue;
            }
            if (match(TokenTypes.AlAlEq)) {
                result = new CompoundAssignmentProcessor(Operations.LShift, result, ternary()).processBinary();
                continue;
            }
            if (match(TokenTypes.ArArArEq)) {
                result = new CompoundAssignmentProcessor(Operations.URShift, result, ternary()).processBinary();
                continue;
            }
            break;
        }
        return result;
    }

    private Expression ternary() {
        Expression result = logicalOr();
        while (true) {
            if (match(TokenTypes.Qm)) {
                final Expression ifTrue = logicalOr();
                consume(TokenTypes.Cl);
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
            if (match(TokenTypes.VbVb)) {
                result = new BinaryExpression(Operations.LogicalOr, result, logicalAnd());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression logicalAnd() {
        Expression result = bitwiseOr();
        while (true) {
            if (match(TokenTypes.AmAm)) {
                result = new BinaryExpression(Operations.LogicalAnd, result, bitwiseOr());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression bitwiseOr() {
        Expression result = bitwiseXor();
        while (true) {
            if (match(TokenTypes.Vb)) {
                result = new BinaryExpression(Operations.BitwiseOr, result, bitwiseXor());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression bitwiseXor() {
        Expression result = bitwiseAnd();
        while (true) {
            if (match(TokenTypes.Cr)) {
                result = new BinaryExpression(Operations.BitwiseXor, result, bitwiseAnd());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression bitwiseAnd() {
        Expression result = equality();
        while (true) {
            if (match(TokenTypes.Am)) {
                result = new BinaryExpression(Operations.BitwiseAnd, result, equality());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression equality() {
        Expression result = comparison();
        while (true) {
            if (match(TokenTypes.EqEq)) {
                result = new BinaryExpression(Operations.Equals, result, comparison());
                continue;
            }
            if (match(TokenTypes.ExEq)) {
                result = new BinaryExpression(Operations.NotEquals, result, comparison());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression comparison() {
        Expression result = shift();
        while (true) {
            if (match(TokenTypes.Al)) {
                result = new BinaryExpression(Operations.LessThen, result, shift());
                continue;
            }
            if (match(TokenTypes.Ar)) {
                result = new BinaryExpression(Operations.GreaterThen, result, shift());
                continue;
            }
            if (match(TokenTypes.AlEq)) {
                result = new BinaryExpression(Operations.LessThenOrEquals, result, shift());
                continue;
            }
            if (match(TokenTypes.ArEq)) {
                result = new BinaryExpression(Operations.GreaterThenOrEquals, result, shift());
                continue;
            }
            if (match(TokenTypes.Is)) {
                result = new BinaryExpression(Operations.Is, result, shift());
                continue;
            }
            if (match(TokenTypes.As)) {
                result = new BinaryExpression(Operations.As, result, shift());
                continue;
            }
            if (match(TokenTypes.EqQm)) {
                result = new BinaryExpression(Operations.Compare, result, shift());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression shift() {
        Expression result = addictive();
        while (true) {
            if (match(TokenTypes.ArAr)) {
                result = new BinaryExpression(Operations.RShift, result, addictive());
                continue;
            }
            if (match(TokenTypes.AlAl)) {
                result = new BinaryExpression(Operations.LShift, result, addictive());
                continue;
            }
            if (match(TokenTypes.ArArAr)) {
                result = new BinaryExpression(Operations.URShift, result, addictive());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression addictive() {
        Expression result = multiplicative();
        while (true) {
            if (match(TokenTypes.Pl)) {
                result = new BinaryExpression(Operations.Addition, result, multiplicative());
                continue;
            }
            if (match(TokenTypes.Mn)) {
                result = new BinaryExpression(Operations.Subtraction, result, multiplicative());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression multiplicative() {
        Expression result = unary();
        while (true) {
            if (match(TokenTypes.St)) {
                result = new BinaryExpression(Operations.Multiplication, result, unary());
                continue;
            }
            if (match(TokenTypes.Sl)) {
                result = new BinaryExpression(Operations.Division, result, unary());
                continue;
            }
            if (match(TokenTypes.Pr)) {
                result = new BinaryExpression(Operations.Modulus, result, unary());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression unary() {
        if (match(TokenTypes.Mn)) {
            return new UnaryExpression(Operations.UnaryMinus, allocation());
        }
        if (match(TokenTypes.Pl)) {
            return new UnaryExpression(Operations.UnaryPlus, allocation());
        }
        if (match(TokenTypes.PlPl)) {
            return new UnaryExpression(Operations.PrefixIncrement, allocation());
        }
        if (match(TokenTypes.MnMn)) {
            return new UnaryExpression(Operations.PrefixDecrement, allocation());
        }
        if (match(TokenTypes.Ex)) {
            return new UnaryExpression(Operations.LogicalNot, allocation());
        }
        if (match(TokenTypes.Tl)) {
            return new UnaryExpression(Operations.BitwiseNot, allocation());
        }
        if (match(TokenTypes.At)) {
            return new UnaryExpression(Operations.ValueClone, expression());
        }
        return allocation();
    }

    private Expression allocation() {
        if (match(TokenTypes.New)) {
            return new NewExpression(expression());
        }
        return postfix();
    }

    private Expression postfix() {
        Expression result = primary();
        while (true) {
            if (match(TokenTypes.Dt)) {
                if (match(TokenTypes.Class)) {
                    result = new UnaryExpression(Operations.ClassAccess, result);
                } else if (match(TokenTypes.Static)) {
                    result = new UnaryExpression(Operations.StaticAccess, result);
                } else if (match(TokenTypes.New)) {
                    result = new UnaryExpression(Operations.ConstructorAccess, result);
                } else if (match(TokenTypes.Type)) {
                    result = new TypeofExpression(result);
                } else if (match(TokenTypes.Super)) {
                    result = new UnaryExpression(Operations.PrototypeAccess, result);
                } else if (match(TokenTypes.Catch)) {
                    final Expression handler;
                    if (match(TokenTypes.Lp)) {
                        handler = expression();
                        consume(TokenTypes.Rp);
                    } else handler = null;
                    result = new CatchExpression(result, handler);
                } else {
                    result = new ContainerAccessExpression(result, mapAccessKey());
                }
                continue;
            }
            if (match(TokenTypes.Lc)) {
                final Expression key = expression();
                consume(TokenTypes.Rc);
                result = new ContainerAccessExpression(result, key);
                continue;
            }
            if (match(TokenTypes.Lp)) {
                final List<Expression> args = new ArrayList<>();
                if (!match(TokenTypes.Rp)) {
                    boolean lastArray = false;
                    do {
                        args.add(expression());
                        if (match(TokenTypes.DtDtDt)) {
                            lastArray = true;
                            break;
                        }
                    }
                    while (match(TokenTypes.Cm));
                    final Expression[] argsArray = args.toArray(new Expression[0]);
                    result = new CallExpression(result, lastArray, argsArray);
                    consume(TokenTypes.Rp);
                } else {
                    result = new CallExpression(result, false);
                }
                continue;
            }
            if (match(TokenTypes.PlPl)) {
                result = new UnaryExpression(Operations.PostfixIncrement, result);
                continue;
            }
            if (match(TokenTypes.MnMn)) {
                result = new UnaryExpression(Operations.PostfixDecrement, result);
                continue;
            }
            break;
        }
        return result;
    }

    private Expression mapAccessKey() {
        final Expression field;
        if (lookMatch(0, TokenTypes.Word))
            field = new ValueExpression(consume(TokenTypes.Word).getValue());
        else {
            consume(TokenTypes.Lp);
            field = expression();
            consume(TokenTypes.Rp);
        }
        return field;
    }

    private Expression mapDefinitionKey() {
        final Expression field;
        if (match(TokenTypes.Lc)) {
            field = expression();
            consume(TokenTypes.Rc);
        } else {
            final String name;
            if (lookMatch(0, TokenTypes.String))
                name = consume(TokenTypes.String).getValue();
            else name = consume(TokenTypes.Word).getValue();
            field = new ValueExpression(name);
        }
        return field;
    }

    private Expression primary() {
        final Token current = get(0);
        if (lookMatch(0, TokenTypes.InterpolatedString)) {
            return interpolated();
        } else if (match(TokenTypes.Let)) {
            return letExpression();
        } else if (match(TokenTypes.Typeof)) {
            return typeof();
        } else if (match(TokenTypes.Nameof)) {
            return nameof();
        } else if (match(TokenTypes.Require)) {
            return requireExpression();
        } else if (match(TokenTypes.This)) {
            return new ThisExpression();
        } else if (match(TokenTypes.That)) {
            return new ThatExpression();
        } else if (isLambdaDefinition()) {
            return lambda();
        } else if (match(TokenTypes.Word)) {
            return new VariableExpression(current.getValue());
        } else if (match(TokenTypes.Lp)) {
            Expression result = expression();
            match(TokenTypes.Rp);
            return result;
        }
        return value();
    }

    private Expression value() {
        final Token current = get(0);
        if (match(TokenTypes.Number)) {
            return new ValueExpression(Double.parseDouble(current.getValue()));
        } else if (match(TokenTypes.Nan)) {
            return new ValueExpression(Double.NaN);
        } else if (match(TokenTypes.String)) {
            return new ValueExpression(current.getValue());
        } else if (match(TokenTypes.True)) {
            return new ValueExpression(true);
        } else if (match(TokenTypes.False)) {
            return new ValueExpression(false);
        } else if (match(TokenTypes.Null)) {
            return NullValue.NullExpression;
        } else if (match(TokenTypes.Undefined)) {
            return UndefinedValue.UndefinedExpression;
        } else if (match(TokenTypes.Type)) {
            return type();
        } else if (match(TokenTypes.Lc)) {
            return array();
        } else if (match(TokenTypes.Lb)) {
            return map();
        } else if (match(TokenTypes.Function)) {
            if (lookMatch(0, TokenTypes.Lb))
                return new InstantFunctionExpression(block());
            return anonymousFunction();
        } else if (match(TokenTypes.Class)) {
            if (lookMatch(0, TokenTypes.Lp))
                return autoClass();
            return anonymousClass();
        } else if (match(TokenTypes.Locked)) {
            return lockedExpression();
        } else if (match(TokenTypes.NsNs)) {
            final String mod = consume(TokenTypes.Word).getValue();
            final Expression expr = expression();
            expr.modify(mod);
            return expr;
        } else throw new UnexpectedTokenException(current);
    }

    private Expression autoClass() {
        match(TokenTypes.Lp);
        List<ArgumentExpression> classArgs = new ArrayList<>();
        if (!match(TokenTypes.Rp)) {
            do classArgs.add(argument()); while (match(TokenTypes.Cm));
            consume(TokenTypes.Rp);
        }
        final Expression base = (match(TokenTypes.Extends) ? expression() : null);
        return new AutoClassGenerator(classArgs, base).generate();
    }

    private Expression anonymousClass() {
        AnonymousFunctionExpression constructor = null;
        final List<AnonymousClassExpression.ASTClassMember> members = new ArrayList<>();
        final Expression base;
        if (match(TokenTypes.Extends))
            base = expression();
        else base = null;
        consume(TokenTypes.Lb);
        while (!match(TokenTypes.Rb)) {
            if (match(TokenTypes.New) || lookMatch(0, TokenTypes.Locked) && lookMatch(1, TokenTypes.New)) {
                final boolean locked = match(TokenTypes.Locked);
                match(TokenTypes.New);
                if (constructor != null)
                    throw new ParserException("Class can have only 1 constructor.");
                else constructor = ((AnonymousFunctionExpression) anonymousFunction());
                constructor.setLocked(locked);
                match(TokenTypes.Sc);
            } else members.add(classMember());
        }
        return new AnonymousClassExpression(constructor, members, base);
    }

    private AnonymousClassExpression.ASTClassMember classMember() {
        final boolean isStatic = match(TokenTypes.Static);
        final boolean isLocked = match(TokenTypes.Locked);
        final Expression key = mapDefinitionKey();
        if (lookMatch(0, TokenTypes.Lp)) {
            final AnonymousFunctionExpression function = ((AnonymousFunctionExpression) anonymousFunction());
            function.setLocked(isLocked);
            match(TokenTypes.Sc);
            return new AnonymousClassExpression.ASTClassMethod(isStatic, key, function);
        }
        if (isLocked)
            throw new InvalidLockException("Unable to lock class field.");
        final Expression value;
        if (match(TokenTypes.Eq) || match(TokenTypes.Cl))
            value = expression();
        else value = NullValue.NullExpression;
        if (!match(TokenTypes.Cm))
            match(TokenTypes.Sc);
        return new AnonymousClassExpression.ASTClassField(isStatic, key, value);
    }

    private Expression lockedExpression() {
        final Expression expression = expression();
        if (expression instanceof Lockable) {
            final Lockable lockable = ((Lockable) expression);
            if (lockable.isLocked())
                throw new InvalidLockException();
            lockable.lock();
        } else throw new InvalidLockException(expression);
        return expression;
    }

    private Expression nameof() {
        consume(TokenTypes.Lp);
        final String name = consume(TokenTypes.Word).getValue();
        consume(TokenTypes.Rp);
        return new NameofExpression(name);
    }

    private Expression lambda() {
        final List<ArgumentExpression> args = new ArrayList<>();
        if (!match(TokenTypes.Lp))
            args.add(new ArgumentExpression(consume(TokenTypes.Word).getValue(), false, false, null));
        else {
            if (!match(TokenTypes.Rp)) {
                do
                    args.add(argument());
                while (match(TokenTypes.Cm));
                match(TokenTypes.Rp);
            }
        }
        match(TokenTypes.MnAr);
        final Statement body;
        if (lookMatch(0, TokenTypes.Lb))
            body = block();
        else body = new ReturnStatement(expression());
        return new AnonymousFunctionExpression(args.toArray(new ArgumentExpression[0]), body);
    }

    private boolean isLambdaDefinition() {
        if (lookMatch(0, TokenTypes.Word) && lookMatch(1, TokenTypes.MnAr))
            return true;
        boolean result = true;
        int rollback = 1;
        if (!match(TokenTypes.Lp))
            return false;
        if (!lookMatch(0, TokenTypes.Rp)) {
            if (!lookMatch(0, TokenTypes.Word)) {
                pos -= rollback;
                return false;
            }
            do {
                rollback++;
                result = result && match(TokenTypes.Word);
                rollback++;
            } while (match(TokenTypes.Cm));
            rollback--;
            if (!lookMatch(0, TokenTypes.Rp)) {
                pos -= rollback;
                return false;
            }
        } else {
            if (match(TokenTypes.Rp)) {
                rollback++;
                if (match(TokenTypes.MnAr))
                    rollback++;
                else result = false;
            } else result = false;
        }
        pos -= rollback;
        return result;
    }

    private Expression map() {
        final MapExpression map = new MapExpression();
        if (!match(TokenTypes.Rb)) {
            while (!match(TokenTypes.Rb)) {
                final boolean isLocked = match(TokenTypes.Locked);
                final Expression key = mapDefinitionKey();
                final Expression value;
                if (lookMatch(0, TokenTypes.Lp)) {
                    value = anonymousFunction();
                    ((AnonymousFunctionExpression) value).setLocked(isLocked);
                } else {
                    if (!match(TokenTypes.Eq) && !match(TokenTypes.Cl))
                        value = NullValue.NullExpression;
                    else value = expression();
                }
                if (!match(TokenTypes.Sc))
                    match(TokenTypes.Cm);
                map.addField(key, value);
            }
        }
        if (match(TokenTypes.At)) {
            consume(TokenTypes.Extends);
            final Expression base = expression();
            map.setBase(new UnaryExpression(Operations.ValueClone, base));
        }
        if (match(TokenTypes.Extends)) {
            final Expression base = expression();
            map.setBase(base);
        }
        return map;
    }

    private Expression type() {
        if (match(TokenTypes.Lp)) {
            final ConstTypeExpression type = new ConstTypeExpression(consume(TokenTypes.String).getValue());
            consume(TokenTypes.Rp);
            return new TypeExpression(type);
        } else return new ValueExpression(Types.TYPE_MAP);
    }

    private Expression typeof() {
        consume(TokenTypes.Lp);
        final Expression expression = expression();
        consume(TokenTypes.Rp);
        return new TypeofExpression(expression);
    }

    private Expression anonymousFunction() {
        final List<ArgumentExpression> args = new ArrayList<>();
        consume(TokenTypes.Lp);
        if (!match(TokenTypes.Rp))
            do
                args.add(argument());
            while (match(TokenTypes.Cm));
        match(TokenTypes.Rp);
        final Statement body;
        if (match(TokenTypes.MnAr)) {
            body = new ReturnStatement(expression());
        } else body = statementOrBlock();
        return new AnonymousFunctionExpression(args.toArray(new ArgumentExpression[0]), body);
    }

    private Expression array() {
        if (match(TokenTypes.Rc))
            return new ArrayExpression();
        final List<Expression> expressions = new ArrayList<>();
        do expressions.add(expression()); while (match(TokenTypes.Cm));
        consume(TokenTypes.Rc);
        return new ArrayExpression(expressions.toArray(new Expression[expressions.size()]));
    }

    private Expression interpolated() {
        final Token token = consume(TokenTypes.InterpolatedString);
        final String string = token.getValue();
        try {
            return new InterpolatedStringExpression(string);
        } catch (SSException e) {
            throw new InvalidInterpolationException(token.getPosition(), string);
        }
    }

    private Expression letExpression() {
        final String name = consume(TokenTypes.Word).getValue();
        final Expression value;
        if (match(TokenTypes.Eq)) {
            value = expression();
        } else value = NullValue.NullExpression;
        return new LetExpression(name, value);
    }
}