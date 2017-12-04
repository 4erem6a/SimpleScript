package test;

import com.evg.ss.ast.Statement;
import com.evg.ss.lexer.Lexer;
import com.evg.ss.lexer.Token;
import com.evg.ss.lib.SS;
import com.evg.ss.parser.Parser;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        String input = new String(Files.readAllBytes(Paths.get("src\\test\\Program.ss")), "UTF-8");

        List<Token> tokens = new Lexer(input).tokenize();
        Statement program = new Parser(tokens).parse();

        program.execute();

    }

}