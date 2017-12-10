package test;

import com.evg.ss.Environment;
import com.evg.ss.SimpleScript;
import com.evg.ss.values.Value;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public static final String PROGRAM_PATH = "src\\test\\program.ss";
    public static final String PROGRAM_DIR = "src\\test\\.";

    public static void main(String[] args) throws IOException {

        Environment.putEnvVariable(Environment.EXECUTABLE_PATH, Value.of(PROGRAM_PATH), true);
        Environment.putEnvVariable(Environment.EXECUTABLE_DIR, Value.of(Paths.get(PROGRAM_DIR).getParent().toString()), true);

        //new Lexer(new String(Files.readAllBytes(Paths.get(PROGRAM_PATH)), "UTF-8")).tokenize().forEach(System.out::println);

        SimpleScript.fromFile(PROGRAM_PATH).compile().execute();

    }

}