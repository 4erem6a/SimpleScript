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
    private static final Map<String, TokenTypes> OPERATOR_TOKEN_MAP = new HashMap<>();
    private static final Map<String, TokenTypes> KEYWORD_MAP = new HashMap<>();

    //Operator initialization:
    static {
        OPERATOR_TOKEN_MAP.put("(", TokenTypes.Lp);
        OPERATOR_TOKEN_MAP.put(")", TokenTypes.Rp);
        OPERATOR_TOKEN_MAP.put("{", TokenTypes.Lb);
        OPERATOR_TOKEN_MAP.put("}", TokenTypes.Rb);
        OPERATOR_TOKEN_MAP.put("[", TokenTypes.Lc);
        OPERATOR_TOKEN_MAP.put("]", TokenTypes.Rc);
        OPERATOR_TOKEN_MAP.put("/", TokenTypes.Sl);
        OPERATOR_TOKEN_MAP.put("*", TokenTypes.St);
        OPERATOR_TOKEN_MAP.put("%", TokenTypes.Pr);
        OPERATOR_TOKEN_MAP.put("-", TokenTypes.Mn);
        OPERATOR_TOKEN_MAP.put("+", TokenTypes.Pl);
        OPERATOR_TOKEN_MAP.put("=", TokenTypes.Eq);
        OPERATOR_TOKEN_MAP.put("&", TokenTypes.Am);
        OPERATOR_TOKEN_MAP.put("|", TokenTypes.Vb);
        OPERATOR_TOKEN_MAP.put("^", TokenTypes.Cr);
        OPERATOR_TOKEN_MAP.put("~", TokenTypes.Tl);
        OPERATOR_TOKEN_MAP.put("!", TokenTypes.Ex);
        OPERATOR_TOKEN_MAP.put("?", TokenTypes.Qm);
        OPERATOR_TOKEN_MAP.put("<", TokenTypes.Al);
        OPERATOR_TOKEN_MAP.put(">", TokenTypes.Ar);
        OPERATOR_TOKEN_MAP.put("@", TokenTypes.At);
        OPERATOR_TOKEN_MAP.put("#", TokenTypes.Ns);
        OPERATOR_TOKEN_MAP.put("||", TokenTypes.VbVb);
        OPERATOR_TOKEN_MAP.put("&&", TokenTypes.AmAm);
        OPERATOR_TOKEN_MAP.put("<=", TokenTypes.AlEq);
        OPERATOR_TOKEN_MAP.put(">=", TokenTypes.ArEq);
        OPERATOR_TOKEN_MAP.put("==", TokenTypes.EqEq);
        OPERATOR_TOKEN_MAP.put("++", TokenTypes.PlPl);
        OPERATOR_TOKEN_MAP.put("--", TokenTypes.MnMn);
        OPERATOR_TOKEN_MAP.put("!=", TokenTypes.ExEq);
        OPERATOR_TOKEN_MAP.put("->", TokenTypes.MnAr);
        OPERATOR_TOKEN_MAP.put("::", TokenTypes.ClCl);
        OPERATOR_TOKEN_MAP.put(">>", TokenTypes.ArAr);
        OPERATOR_TOKEN_MAP.put("<<", TokenTypes.AlAl);
        OPERATOR_TOKEN_MAP.put("##", TokenTypes.NsNs);
        OPERATOR_TOKEN_MAP.put("..", TokenTypes.DtDt);
        OPERATOR_TOKEN_MAP.put("+=", TokenTypes.PlEq);
        OPERATOR_TOKEN_MAP.put("-=", TokenTypes.MnEq);
        OPERATOR_TOKEN_MAP.put("*=", TokenTypes.StEq);
        OPERATOR_TOKEN_MAP.put("/=", TokenTypes.SlEq);
        OPERATOR_TOKEN_MAP.put("%=", TokenTypes.PrEq);
        OPERATOR_TOKEN_MAP.put("&=", TokenTypes.AmEq);
        OPERATOR_TOKEN_MAP.put("|=", TokenTypes.VbEq);
        OPERATOR_TOKEN_MAP.put("^=", TokenTypes.CrEq);
        OPERATOR_TOKEN_MAP.put("~=", TokenTypes.TlEq);
        OPERATOR_TOKEN_MAP.put("@=", TokenTypes.AtEq);
        OPERATOR_TOKEN_MAP.put("|>", TokenTypes.VbAr);
        OPERATOR_TOKEN_MAP.put(">>=", TokenTypes.ArArEq);
        OPERATOR_TOKEN_MAP.put("<<=", TokenTypes.AlAlEq);
        OPERATOR_TOKEN_MAP.put("&&=", TokenTypes.AmAmEq);
        OPERATOR_TOKEN_MAP.put("||=", TokenTypes.VbVbEq);
        OPERATOR_TOKEN_MAP.put(">>>", TokenTypes.ArArAr);
        OPERATOR_TOKEN_MAP.put("...", TokenTypes.DtDtDt);
        OPERATOR_TOKEN_MAP.put(">>>=", TokenTypes.ArArArEq);
        OPERATOR_TOKEN_MAP.put("=?", TokenTypes.EqQm);
        OPERATOR_TOKEN_MAP.put(":", TokenTypes.Cl);
        OPERATOR_TOKEN_MAP.put(".", TokenTypes.Dt);
        OPERATOR_TOKEN_MAP.put(",", TokenTypes.Cm);
        OPERATOR_TOKEN_MAP.put(";", TokenTypes.Sc);
    }

    //Keyword initialization:
    static {
        KEYWORD_MAP.put("true", TokenTypes.True);
        KEYWORD_MAP.put("false", TokenTypes.False);
        KEYWORD_MAP.put("null", TokenTypes.Null);
        KEYWORD_MAP.put("nan", TokenTypes.Nan);
        KEYWORD_MAP.put("let", TokenTypes.Let);
        KEYWORD_MAP.put("const", TokenTypes.Const);
        KEYWORD_MAP.put("if", TokenTypes.If);
        KEYWORD_MAP.put("else", TokenTypes.Else);
        KEYWORD_MAP.put("for", TokenTypes.For);
        KEYWORD_MAP.put("do", TokenTypes.Do);
        KEYWORD_MAP.put("while", TokenTypes.While);
        KEYWORD_MAP.put("break", TokenTypes.Break);
        KEYWORD_MAP.put("continue", TokenTypes.Continue);
        KEYWORD_MAP.put("block", TokenTypes.Block);
        KEYWORD_MAP.put("require", TokenTypes.Require);
        KEYWORD_MAP.put("function", TokenTypes.Function);
        KEYWORD_MAP.put("return", TokenTypes.Return);
        KEYWORD_MAP.put("foreach", TokenTypes.Foreach);
        KEYWORD_MAP.put("in", TokenTypes.In);
        KEYWORD_MAP.put("switch", TokenTypes.Switch);
        KEYWORD_MAP.put("case", TokenTypes.Case);
        KEYWORD_MAP.put("type", TokenTypes.Type);
        KEYWORD_MAP.put("typeof", TokenTypes.Typeof);
        KEYWORD_MAP.put("is", TokenTypes.Is);
        KEYWORD_MAP.put("extends", TokenTypes.Extends);
        KEYWORD_MAP.put("exports", TokenTypes.Exports);
        KEYWORD_MAP.put("as", TokenTypes.As);
        KEYWORD_MAP.put("nameof", TokenTypes.Nameof);
        KEYWORD_MAP.put("import", TokenTypes.Import);
        KEYWORD_MAP.put("new", TokenTypes.New);
        KEYWORD_MAP.put("this", TokenTypes.This);
        KEYWORD_MAP.put("locked", TokenTypes.Locked);
        KEYWORD_MAP.put("try", TokenTypes.Try);
        KEYWORD_MAP.put("catch", TokenTypes.Catch);
        KEYWORD_MAP.put("finally", TokenTypes.Finally);
        KEYWORD_MAP.put("throw", TokenTypes.Throw);
        KEYWORD_MAP.put("class", TokenTypes.Class);
        KEYWORD_MAP.put("static", TokenTypes.Static);
        KEYWORD_MAP.put("undefined", TokenTypes.Undefined);
        KEYWORD_MAP.put("that", TokenTypes.That);
        KEYWORD_MAP.put("super", TokenTypes.Super);
        KEYWORD_MAP.put("delete", TokenTypes.Delete);
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
        addToken(TokenTypes.Number, number.toString());
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
        addToken(TokenTypes.Number, number.toString());
    }

    private void tokenizeNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (Character.isDigit(current) || current == '.' || current == '_') {
            if (current == '.' && peek(1) == '.')
                break;
            if (current != '_')
                buffer.append(current);
            current = next();
        }
        if (buffer.charAt(0) == '.')
            throw new InvalidTokenDefinitionException(TokenTypes.Number, calculatePosition());
        final Double number = Double.parseDouble(buffer.toString());
        addToken(TokenTypes.Number, number.toString());
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
        else addToken(TokenTypes.Word, buffer.toString());
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
            addToken(quote == '`' ? TokenTypes.InterpolatedString : TokenTypes.String);
            return;
        }
        if (QUOTES.indexOf(quote) == -1)
            throw new UnknownCharacterException(quote, calculatePosition());
        final StringBuilder buffer = new StringBuilder();
        char current = next();
        while (true) {
            if (current == '\0')
                throw new InvalidTokenDefinitionException(
                        quote == '`' ? TokenTypes.InterpolatedString : TokenTypes.String,
                        calculatePosition());
            if (current == quote) break;
            buffer.append(current);
            current = next();
        }
        next();

        final String result = isPureString ? buffer.toString() : escape(buffer.toString());
        addToken(quote == '`' ? TokenTypes.InterpolatedString : TokenTypes.String, result);
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