package com.evg.ss.parser;

import com.evg.ss.exceptions.parser.UnexpectedTokenException;
import com.evg.ss.lexer.Token;
import com.evg.ss.lexer.TokenTypes;
import com.evg.ss.parser.ast.Expression;
import com.evg.ss.parser.ast.Statement;

import java.util.List;

public abstract class AbstractParser {

    protected static final Token EOF = new Token(TokenTypes.EOF, "");

    protected final List<Token> tokens;
    protected final int size;

    protected int pos;

    protected AbstractParser(List<Token> tokens) {
        this.tokens = tokens;
        this.size = tokens.size();
    }

    public abstract Statement parse();

    public abstract Expression express();

    protected boolean match(TokenTypes type) {
        if (!lookMatch(0, type))
            return false;
        pos++;
        return true;
    }

    protected boolean match(String word) {
        if (!lookMatch(0, TokenTypes.Word)
                || !get(0).getValue().equals(word))
            return false;
        pos++;
        return true;
    }

    protected boolean lookMatch(int pos, TokenTypes type) {
        return compareTokenType(get(pos), type);
    }

    protected Token get(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= size) return EOF;
        return tokens.get(position);
    }

    protected Token consume(TokenTypes type) {
        final Token current = get(0);
        if (!compareTokenType(current, type))
            throw new UnexpectedTokenException(type, current);
        pos++;
        return current;
    }

    protected Token consume(String word) {
        if (!lookMatch(0, TokenTypes.Word)
                || !get(0).getValue().equals(word))
            throw new UnexpectedTokenException(get(0).getPosition(), word);
        return consume(TokenTypes.Word);
    }

    protected Token consume() {
        final Token current = get(0);
        pos++;
        return current;
    }

    protected boolean compareTokenType(Token token, TokenTypes type) {
        return token.getType() == type;
    }

}