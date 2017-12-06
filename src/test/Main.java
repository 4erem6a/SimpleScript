package test;

import com.evg.ss.SimpleScript;

import java.io.IOException;

public class Main {

    public static final String PROGRAM_PATH = "src\\test\\Program.ss";

    public static void main(String[] args) throws IOException {

        SimpleScript.fromFile(PROGRAM_PATH).execute();

    }

}