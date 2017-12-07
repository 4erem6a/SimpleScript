package test;

import com.evg.ss.Environment;
import com.evg.ss.SimpleScript;
import com.evg.ss.values.Value;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public static final String PROGRAM_PATH = "src\\test\\Program.ss";
    public static final String PROGRAM_DIR = "src\\test\\.";

    public static void main(String[] args) throws IOException {

        Environment.putEnvVariable("EXECUTABLE_PATH", Value.of(PROGRAM_PATH), true);
        Environment.putEnvVariable("EXECUTABLE_DIR", Value.of(Paths.get(PROGRAM_DIR).getParent().toString()), true);
        SimpleScript.fromFile(PROGRAM_PATH).execute();

    }

}