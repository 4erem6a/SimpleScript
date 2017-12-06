package com.evg.ss;

import com.evg.ss.parser.ast.Statement;
import com.evg.ss.exceptions.SSExecutionException;
import com.evg.ss.exceptions.SSLexerException;
import com.evg.ss.exceptions.SSParserException;
import com.evg.ss.lexer.Lexer;
import com.evg.ss.lexer.Token;
import com.evg.ss.parser.Parser;
import com.evg.ss.parser.visitors.FunctionAdder;
import com.evg.ss.parser.visitors.Visitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class SimpleScript {

    private static final List<Visitor> DEFAULT_VISITORS = new ArrayList<>();
    static {
        DEFAULT_VISITORS.add(new FunctionAdder());
    }

    private Statement program;

    private SimpleScript(Statement program) {
        this.program = program;
    }

    public void execute() throws SSExecutionException {
        DEFAULT_VISITORS.forEach(program::accept);
        program.execute();
    }

    public void accept(Visitor visitor) {
        program.accept(visitor);
    }

    public static SimpleScript fromSource(String source) throws SSParserException, SSLexerException {
        return new SimpleScript(new Parser(new Lexer(source).tokenize()).parse());
    }

    public static SimpleScript fromTokenList(List<Token> tokens) throws SSParserException {
        return new SimpleScript(new Parser(tokens).parse());
    }

    public static SimpleScript fromProgram(Statement program) {
        return new SimpleScript(program);
    }

    public static SimpleScript fromFile(File file) throws IOException, SSParserException, SSLexerException{
        return fromFile(file.toPath());
    }

    public static SimpleScript fromFile(Path path) throws IOException, SSParserException, SSLexerException{
        return fromFile(path.toString());
    }

    public static SimpleScript fromFile(String path) throws IOException, SSParserException, SSLexerException{
        final String source = Files.readAllLines(Paths.get(path)).stream().reduce(String::concat).get();
        return fromSource(source);
    }

}