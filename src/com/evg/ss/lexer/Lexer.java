package com.evg.ss.lexer;

import com.evg.ss.exceptions.lexer.InvalidTokenDefinitionException;
import com.evg.ss.exceptions.lexer.UnknownCharacterException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 4erem6a
 */
public final class Lexer extends AbstractLexer {
    //Operator characters:
    private static final String OPERATOR_CHARS = "@#(){}[]/*%-+=&|^~!?<>|:.,;";
    private static final String QUOTES = "'\"`";
    private static final Map<String, TokenType> OPERATOR_TOKEN_MAP = new HashMap<>();
    private static final Map<String, TokenType> KEYWORD_MAP = new HashMap<>();

    //Operator initialization:
    static {
        OPERATOR_TOKEN_MAP.put("(", TokenType.Lp);
        OPERATOR_TOKEN_MAP.put(")", TokenType.Rp);
        OPERATOR_TOKEN_MAP.put("{", TokenType.Lb);
        OPERATOR_TOKEN_MAP.put("}", TokenType.Rb);
        OPERATOR_TOKEN_MAP.put("[", TokenType.Lc);
        OPERATOR_TOKEN_MAP.put("]", TokenType.Rc);
        OPERATOR_TOKEN_MAP.put("/", TokenType.Sl);
        OPERATOR_TOKEN_MAP.put("*", TokenType.St);
        OPERATOR_TOKEN_MAP.put("%", TokenType.Pr);
        OPERATOR_TOKEN_MAP.put("-", TokenType.Mn);
        OPERATOR_TOKEN_MAP.put("+", TokenType.Pl);
        OPERATOR_TOKEN_MAP.put("=", TokenType.Eq);
        OPERATOR_TOKEN_MAP.put("&", TokenType.Am);
        OPERATOR_TOKEN_MAP.put("|", TokenType.Vb);
        OPERATOR_TOKEN_MAP.put("^", TokenType.Cr);
        OPERATOR_TOKEN_MAP.put("~", TokenType.Tl);
        OPERATOR_TOKEN_MAP.put("!", TokenType.Ex);
        OPERATOR_TOKEN_MAP.put("?", TokenType.Qm);
        OPERATOR_TOKEN_MAP.put("<", TokenType.Al);
        OPERATOR_TOKEN_MAP.put(">", TokenType.Ar);
        OPERATOR_TOKEN_MAP.put("@", TokenType.At);
        OPERATOR_TOKEN_MAP.put("#", TokenType.Ns);
        OPERATOR_TOKEN_MAP.put("||", TokenType.VbVb);
        OPERATOR_TOKEN_MAP.put("&&", TokenType.AmAm);
        OPERATOR_TOKEN_MAP.put("<=", TokenType.AlEq);
        OPERATOR_TOKEN_MAP.put(">=", TokenType.ArEq);
        OPERATOR_TOKEN_MAP.put("==", TokenType.EqEq);
        OPERATOR_TOKEN_MAP.put("++", TokenType.PlPl);
        OPERATOR_TOKEN_MAP.put("--", TokenType.MnMn);
        OPERATOR_TOKEN_MAP.put("!=", TokenType.ExEq);
        OPERATOR_TOKEN_MAP.put("->", TokenType.MnAr);
        OPERATOR_TOKEN_MAP.put("::", TokenType.ClCl);
        OPERATOR_TOKEN_MAP.put(">>", TokenType.ArAr);
        OPERATOR_TOKEN_MAP.put("<<", TokenType.AlAl);
        OPERATOR_TOKEN_MAP.put("##", TokenType.NsNs);
        OPERATOR_TOKEN_MAP.put(">>>", TokenType.ArArAr);
        OPERATOR_TOKEN_MAP.put("=?", TokenType.EqQm);
        OPERATOR_TOKEN_MAP.put(":", TokenType.Cl);
        OPERATOR_TOKEN_MAP.put(".", TokenType.Dt);
        OPERATOR_TOKEN_MAP.put(",", TokenType.Cm);
        OPERATOR_TOKEN_MAP.put(";", TokenType.Sc);
    }

    //Keyword initialization:
    static {
        KEYWORD_MAP.put("true", TokenType.True);
        KEYWORD_MAP.put("false", TokenType.False);
        KEYWORD_MAP.put("null", TokenType.Null);
        KEYWORD_MAP.put("nan", TokenType.Nan);
        KEYWORD_MAP.put("let", TokenType.Let);
        KEYWORD_MAP.put("const", TokenType.Const);
        KEYWORD_MAP.put("if", TokenType.If);
        KEYWORD_MAP.put("else", TokenType.Else);
        KEYWORD_MAP.put("for", TokenType.For);
        KEYWORD_MAP.put("do", TokenType.Do);
        KEYWORD_MAP.put("while", TokenType.While);
        KEYWORD_MAP.put("break", TokenType.Break);
        KEYWORD_MAP.put("continue", TokenType.Continue);
        KEYWORD_MAP.put("block", TokenType.Block);
        KEYWORD_MAP.put("require", TokenType.Require);
        KEYWORD_MAP.put("function", TokenType.Function);
        KEYWORD_MAP.put("return", TokenType.Return);
        KEYWORD_MAP.put("foreach", TokenType.Foreach);
        KEYWORD_MAP.put("in", TokenType.In);
        KEYWORD_MAP.put("switch", TokenType.Switch);
        KEYWORD_MAP.put("case", TokenType.Case);
        KEYWORD_MAP.put("type", TokenType.Type);
        KEYWORD_MAP.put("typeof", TokenType.Typeof);
        KEYWORD_MAP.put("is", TokenType.Is);
        KEYWORD_MAP.put("extends", TokenType.Extends);
        KEYWORD_MAP.put("exports", TokenType.Exports);
        KEYWORD_MAP.put("as", TokenType.As);
        KEYWORD_MAP.put("nameof", TokenType.Nameof);
        KEYWORD_MAP.put("import", TokenType.Import);
        KEYWORD_MAP.put("new", TokenType.New);
        KEYWORD_MAP.put("this", TokenType.This);
        KEYWORD_MAP.put("locked", TokenType.Locked);
        KEYWORD_MAP.put("try", TokenType.Try);
        KEYWORD_MAP.put("catch", TokenType.Catch);
        KEYWORD_MAP.put("finally", TokenType.Finally);
        KEYWORD_MAP.put("throw", TokenType.Throw);
        KEYWORD_MAP.put("class", TokenType.Class);
        KEYWORD_MAP.put("static", TokenType.Static);
        KEYWORD_MAP.put("undefined", TokenType.Undefined);
        KEYWORD_MAP.put("that", TokenType.That);
    }

    public Lexer(String source) {
        super(source);
    }

    private static boolean isHexNumber(char current) {
        return "abcdef".indexOf(Character.toLowerCase(current)) != -1;
    }

    @Override
    public List<Token> tokenize() {
        while (position < sourceLength) {
            final Character current = peek(0);
            if (current == '#') {
                if (Character.isDigit(peek(1)) || isHexNumber(peek(1)))
                    tokenizeHexNumber();
                else tokenizeOperator();
            } else if (current == '0') {
                if (peek(1) == 'x')
                    tokenizeBinaryNumber();
                else tokenizeNumber();
            } else if (Character.isDigit(current))
                tokenizeNumber();
            else if (Character.isLetter(current) || current == '$' || current == '_')
                tokenizeWord();
            else if (QUOTES.indexOf(current) != -1 || (current == '@' && QUOTES.indexOf(peek(1)) != -1))
                tokenizeString();
            else if (OPERATOR_CHARS.contains(current.toString()))
                tokenizeOperator();
            else {
                if ("\n\t\r\b\f ".indexOf(current) == -1)
                    throw new UnknownCharacterException(current, calculatePosition());
                else next();
            }
        }
        return tokens;
    }

    private void tokenizeOperator() {
        StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        if (current == '/') {
            final char next = peek(1);
            if (next == '/') {
                skipComment();
                return;
            } else if (next == '*') {
                skipMultilineComment();
                return;
            }
        }
        while (true) {
            final String operator = buffer.toString();
            if (!operator.isEmpty() && !OPERATOR_TOKEN_MAP.containsKey(operator + current)) {
                addToken(OPERATOR_TOKEN_MAP.get(operator));
                return;
            }
            buffer.append(current);
            current = next();
        }
    }

    private void skipComment() {
        next();
        next();
        char current = peek(0);
        while ("\t\n\0".indexOf(current) == -1)
            current = next();
    }

    private void skipMultilineComment() {
        next();
        next();
        char current = peek(0);
        while (true) {
            if (current == '*')
                if (peek(1) == '/')
                    break;
            current = next();
        }
        next();
        next();
    }

    private void tokenizeHexNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = next();
        while (Character.isDigit(current) || isHexNumber(current) || current == '_') {
            if (current != '_')
                buffer.append(current);
            current = next();
        }
        final Long number = Long.parseLong(buffer.toString(), 16);
        addToken(TokenType.Number, number.toString());
    }

    private void tokenizeBinaryNumber() {
        next();
        final StringBuilder buffer = new StringBuilder();
        char current = next();
        while (current == '0' || current == '1' || current == '_') {
            if (current != '_')
                buffer.append(current);
            current = next();
        }
        final Long number = Long.parseLong(buffer.toString(), 2);
        addToken(TokenType.Number, number.toString());
    }

    private void tokenizeNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (Character.isDigit(current) || current == '.' || current == '_') {
            if (current == '.' && buffer.indexOf(".") != -1)
                throw new InvalidTokenDefinitionException(TokenType.Number, calculatePosition());
            if (current != '_')
                buffer.append(current);
            current = next();
        }
        if (buffer.charAt(0) == '.')
            throw new InvalidTokenDefinitionException(TokenType.Number, calculatePosition());
        final Double number = Double.parseDouble(buffer.toString());
        addToken(TokenType.Number, number.toString());
    }

    private void tokenizeWord() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (Character.isLetter(current) || Character.isDigit(current) || current == '$' || current == '_') {
            buffer.append(current);
            current = next();
        }
        final String word = buffer.toString();
        if (KEYWORD_MAP.containsKey(word))
            addToken(KEYWORD_MAP.get(word), word);
        else addToken(TokenType.Word, buffer.toString());
    }

    private void tokenizeString() {
        final boolean isPureString;
        if (peek(0) == '@') {
            isPureString = true;
            next();
        } else isPureString = false;
        char quote = peek(0);
        if (peek(1) == quote) {
            next();
            next();
            addToken(quote == '`' ? TokenType.InterpolatedString : TokenType.String);
            return;
        }
        if (QUOTES.indexOf(quote) == -1)
            throw new UnknownCharacterException(quote, calculatePosition());
        final StringBuilder buffer = new StringBuilder();
        char current = next();
        while (true) {
            if (current == '\0')
                throw new InvalidTokenDefinitionException(
                        quote == '`' ? TokenType.InterpolatedString : TokenType.String,
                        calculatePosition());
            if (current == quote) break;
            buffer.append(current);
            current = next();
        }
        next();

        final String result = isPureString ? buffer.toString() : escape(buffer.toString());
        addToken(quote == '`' ? TokenType.InterpolatedString : TokenType.String, result);
    }

    private String escape(String string) {
        final StringBuilder buffer = new StringBuilder();
        int position = 0;
        char current = string.charAt(position);
        while (true) {
            if (current == '\0')
                break;
            if (current == '\\') {
                current = peekLocal(++position, string);
                switch (current) {
                    case 'n':
                        buffer.append('\n');
                        current = peekLocal(++position, string);
                        continue;
                    case 't':
                        buffer.append('\t');
                        current = peekLocal(++position, string);
                        continue;
                    case 'r':
                        buffer.append('\r');
                        current = peekLocal(++position, string);
                        continue;
                    case 'f':
                        buffer.append('\f');
                        current = peekLocal(++position, string);
                        continue;
                    case 'b':
                        buffer.append('\b');
                        current = peekLocal(++position, string);
                        continue;
                    case '0':
                        buffer.append('\0');
                        current = peekLocal(++position, string);
                        continue;
                    case '\'':
                        buffer.append('\'');
                        current = peekLocal(++position, string);
                        continue;
                    case '"':
                        buffer.append('"');
                        current = peekLocal(++position, string);
                        continue;
                    case '`':
                        buffer.append('`');
                        current = peekLocal(++position, string);
                        continue;
                    default:
                        buffer.append('\\');
                        continue;
                }
            }
            buffer.append(current);
            current = peekLocal(++position, string);
        }
        return buffer.toString();
    }
}