package com.evg.ss.lexer;

import com.evg.ss.exceptions.InvalidTokenDefinitionException;
import com.evg.ss.exceptions.UnknownCharacterException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 4erem6a
 */
public final class Lexer {
    //Operator characters:
    private static final String OperatorChars = "(){}[]/*-+=&|^~!?<>|:,;";
    private static final String Quotes = "'\"`";
    private static final Map<String, TokenType> OperatorTokenMap = new HashMap<>();

    //Operator initialization:
    static {
        OperatorTokenMap.put("(", TokenType.Lp);
        OperatorTokenMap.put(")", TokenType.Rp);
        OperatorTokenMap.put("{", TokenType.Lb);
        OperatorTokenMap.put("}", TokenType.Rb);
        OperatorTokenMap.put("[", TokenType.Lc);
        OperatorTokenMap.put("]", TokenType.Rc);
        OperatorTokenMap.put("/", TokenType.Sl);
        OperatorTokenMap.put("*", TokenType.St);
        OperatorTokenMap.put("-", TokenType.Mn);
        OperatorTokenMap.put("+", TokenType.Pl);
        OperatorTokenMap.put("=", TokenType.Eq);
        OperatorTokenMap.put("&", TokenType.Am);
        OperatorTokenMap.put(",", TokenType.Cm);
        OperatorTokenMap.put(";", TokenType.Sc);
        OperatorTokenMap.put("|", TokenType.Vb);
        OperatorTokenMap.put("^", TokenType.Cr);
        OperatorTokenMap.put("~", TokenType.Tl);
        OperatorTokenMap.put("!", TokenType.Ex);
        OperatorTokenMap.put("?", TokenType.Qm);
        OperatorTokenMap.put("<", TokenType.Al);
        OperatorTokenMap.put(">", TokenType.Ar);
        OperatorTokenMap.put("||", TokenType.VbVb);
        OperatorTokenMap.put("&&", TokenType.AmAm);
        OperatorTokenMap.put("<=", TokenType.AlEq);
        OperatorTokenMap.put(">=", TokenType.ArEq);
        OperatorTokenMap.put("==", TokenType.EqEq);
        OperatorTokenMap.put("!=", TokenType.ExEq);
        OperatorTokenMap.put(":", TokenType.Cl);
    }

    private static final Map<String, TokenType> KeyWordMap = new HashMap<>();

    //Keyword initialization:
    static {
        KeyWordMap.put("true", TokenType.True);
        KeyWordMap.put("false", TokenType.False);
        KeyWordMap.put("null", TokenType.Null);
        KeyWordMap.put("nan", TokenType.Nan);
        KeyWordMap.put("let", TokenType.Let);
        KeyWordMap.put("const", TokenType.Const);
        KeyWordMap.put("if", TokenType.If);
        KeyWordMap.put("else", TokenType.Else);
        KeyWordMap.put("for", TokenType.For);
        KeyWordMap.put("do", TokenType.Do);
        KeyWordMap.put("while", TokenType.While);
        KeyWordMap.put("break", TokenType.Break);
        KeyWordMap.put("continue", TokenType.Continue);
        KeyWordMap.put("block", TokenType.Block);
        KeyWordMap.put("import", TokenType.Import);
        KeyWordMap.put("function", TokenType.Function);
        KeyWordMap.put("return", TokenType.Return);
        KeyWordMap.put("foreach", TokenType.Foreach);
        KeyWordMap.put("in", TokenType.In);
        KeyWordMap.put("switch", TokenType.Switch);
        KeyWordMap.put("case", TokenType.Case);
        KeyWordMap.put("type", TokenType.Type);
        KeyWordMap.put("typeof", TokenType.Typeof);
        KeyWordMap.put("is", TokenType.Is);
    }

    private final String source;
    private final int sourceLength;

    private final List<Token> tokens;

    private int position;

    public Lexer(String source) {
        this.source = source;
        sourceLength = source.length();

        tokens = new ArrayList<>();
    }

    public List<Token> tokenize() {
        while (position < sourceLength) {
            final Character current = peek(0);
            if (current == '#')
                tokenizeHexNumber();
            else if (current == '0') {
                if (peek(1) == 'x')
                    tokenizeBinaryNumber();
                else tokenizeNumber();
            } else if (Character.isDigit(current))
                tokenizeNumber();
            else if (Character.isLetter(current) || current == '$' || current == '_')
                tokenizeWord();
            else if (Quotes.indexOf(current) != -1 || current == '@')
                tokenizeString();
            else if (OperatorChars.contains(current.toString()))
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
            }
            else if (next == '*') {
                skipMultilineComment();
                return;
            }
        }
        while (true) {
            final String operator = buffer.toString();
            if (!operator.isEmpty() && !OperatorTokenMap.containsKey(operator + current)) {
                addToken(OperatorTokenMap.get(operator));
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
        while (Character.isDigit(current) || isHexNumber(current)) {
            buffer.append(current);
            current = next();
        }
        final Long number = Long.parseLong(buffer.toString(), 16);
        addToken(TokenType.Number, number.toString());
    }

    private static boolean isHexNumber(char current) {
        return "abcdef".indexOf(Character.toLowerCase(current)) != -1;
    }

    private void tokenizeBinaryNumber() {
        next();
        final StringBuilder buffer = new StringBuilder();
        char current = next();
        while (current == '0' || current == '1') {
            buffer.append(current);
            current = next();
        }
        final Long number = Long.parseLong(buffer.toString(), 2);
        addToken(TokenType.Number, number.toString());
    }

    private void tokenizeNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (Character.isDigit(current) || current == '.') {
            if (current == '.')
                throw new InvalidTokenDefinitionException(TokenType.Number, calculatePosition());
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
        while (Character.isLetter(current) || current == '$' || current == '_') {
            buffer.append(current);
            current = next();
        }
        final String word = buffer.toString();
        if (KeyWordMap.containsKey(word))
            addToken(KeyWordMap.get(word));
        else addToken(TokenType.Word, buffer.toString());
    }

    private void tokenizeString() {
        final boolean isPureString;
        if (peek(0) == '@') {
            isPureString = true;
            next();
        } else isPureString = false;
        char quote = peek(0);
        if (Quotes.indexOf(quote) == -1)
            throw new UnknownCharacterException(quote, calculatePosition());
        final StringBuilder buffer = new StringBuilder();
        char current = next();
        while (true) {
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

    private char peekLocal(int position, String string) {
        if (position >= string.length()) return '\0';
        return string.charAt(position);
    }

    private char next() {
        position++;
        return peek(0);
    }

    private char peek(int relativePosition) {
        final int position = this.position + relativePosition;
        if (position >= sourceLength) return '\0';
        return source.charAt(position);
    }

    private Position calculatePosition() {
        String substring = source.substring(0, position);
        final int line = (int)substring.chars().filter(ch -> ch == '\n').count() + 1;
        if (source.indexOf('\n') > position)
            substring = source.substring(0, position);
        else substring = source.substring(substring.lastIndexOf('\n') + 1, position);
        final int sym = substring.length() + 1;
        return new Position(line, sym);
    }

    private void addToken(TokenType type) {
        addToken(type, "");
    }

    private void addToken(TokenType type, String text) {
        tokens.add(new Token(type, text, calculatePosition()));
    }
}