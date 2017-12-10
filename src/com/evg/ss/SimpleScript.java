package com.evg.ss;

import com.evg.ss.exceptions.SSException;
import com.evg.ss.exceptions.execution.SSExecutionException;
import com.evg.ss.exceptions.lexer.SSLexerException;
import com.evg.ss.exceptions.parser.SSParserException;
import com.evg.ss.lexer.Lexer;
import com.evg.ss.lexer.Token;
import com.evg.ss.parser.Parser;
import com.evg.ss.parser.ast.Expression;
import com.evg.ss.parser.ast.Statement;
import com.evg.ss.parser.visitors.FunctionAdder;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;
import com.evg.ss.values.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class SimpleScript {

    public static class Version {
        private int release;
        private int major;
        private int minor;
        private int build;

        public Version(int release, int major, int minor, int build) {
            this.release = release;
            this.major = major;
            this.minor = minor;
            this.build = build;
        }

        @Override
        public String toString() {
            return String.format("%d.%d.%d.%d", release, major, minor, build);
        }
    }

    public static Version VERSION = new Version(1, 5, 4, 3);

    private List<Token> tokens;

    private SimpleScript(List<Token> tokens) {
        this.tokens = tokens;
    }

    public static SimpleScript fromSource(String source) throws SSLexerException {
        return new SimpleScript(new Lexer(source).tokenize());
    }

    public static SimpleScript fromTokenList(List<Token> tokens) {
        return new SimpleScript(tokens);
    }

    public static CompiledScript fromProgram(Statement program) {
        return new CompiledScript(program);
    }

    public static SimpleScript fromFile(File file) throws IOException, SSLexerException {
        return fromFile(file.toPath());
    }

    public static SimpleScript fromFile(Path path) throws IOException, SSParserException, SSLexerException {
        return fromFile(path.toString());
    }

    public static SimpleScript fromFile(String path, String charset) throws IOException, SSLexerException {
        final String source = new String(Files.readAllBytes(Paths.get(path)), charset);
        return fromSource(source);
    }

    public static SimpleScript fromFile(File file, String charset) throws IOException, SSLexerException {
        return fromFile(file.toPath(), charset);
    }

    public static SimpleScript fromFile(Path path, String charset) throws IOException, SSLexerException {
        return fromFile(path.toString(), charset);
    }

    public static SimpleScript fromFile(String path) throws IOException, SSLexerException {
        final String source = new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
        return fromSource(source);
    }

    public CompiledScript compile() throws SSParserException {
        return new CompiledScript(new Parser(tokens).parse());
    }

    public SSException tryCompile() {
        try {
            new Parser(tokens).parse();
        } catch (SSException e) {
            return e;
        }
        return null;
    }

    public boolean isCompilabe() {
        try {
            new Parser(tokens).parse();
        } catch (SSException e) {
            return false;
        }
        return true;
    }

    public Expression express() throws SSParserException {
        return new Parser(tokens).express();
    }

    public SSException tryExpress() {
        try {
            new Parser(tokens).express();
        } catch (SSException e) {
            return e;
        }
        return null;
    }

    public boolean isExpressible() {
        try {
            new Parser(tokens).express();
        } catch (SSException e) {
            return false;
        }
        return true;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public static class CompiledScript {

        private static final List<Visitor> DEFAULT_VISITORS = new ArrayList<>();

        static {
            Environment.putEnvVariable(Environment.CURRENT_LANG_VERSION, Value.of(VERSION.toString()), true);
            DEFAULT_VISITORS.add(new FunctionAdder());
        }

        private Statement program;

        private CompiledScript(Statement program) {
            this.program = program;
        }

        public void execute() throws SSExecutionException {
            DEFAULT_VISITORS.forEach(program::accept);
            program.execute();
        }

        public Statement getProgram() {
            return program;
        }

        public void acceptVisitor(Visitor visitor) {
            program.accept(visitor);
        }

        public <TResult> TResult acceptResultVisitor(ResultVisitor<TResult> visitor) {
            return program.accept(visitor);
        }

    }

}