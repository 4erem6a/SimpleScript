package desktop;

import com.evg.ss.Environment;
import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.lexer.SSLexerException;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.values.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public final class Main {

    public enum SSExecutionFlags {
        GET_TOKENS,
        GET_PROGRAM,
        EXECUTE
    }

    public static final Map<String, SSExecutionFlags> EXECUTION_FLAGS_MAP = new HashMap<>();
    static {
        EXECUTION_FLAGS_MAP.put("-t", SSExecutionFlags.GET_TOKENS);
        EXECUTION_FLAGS_MAP.put("-p", SSExecutionFlags.GET_PROGRAM);
        EXECUTION_FLAGS_MAP.put("-e", SSExecutionFlags.EXECUTE);
    }


    public static String[] ARGS;
    public static boolean LOG;

    public static Path PROGRAM_PATH;
    public static List<SSExecutionFlags> EXECUTION_FLAGS;

    public static void main(String[] args) throws IOException {
        final int argc = Arguments.checkArgc(args, 1, 2, 3, 4, 5);
        if (argc == -1)
            exitWithMessage("Error: invalid argument count.");
        LOG = args[argc - 1].equals("-l");
        if (LOG)
            args = Arrays.stream(args).limit(argc - 2).toArray(String[]::new);
        Main.ARGS = args;
        validate();
        process();
    }

    public static void validate() {
        final Path path = Paths.get(ARGS[0]);
        if (!Files.exists(path))
            exitWithMessage("Error: file does not exists.");
        if (!path.toAbsolutePath().toString().endsWith(".ss"))
            exitWithMessage("Error: SimpleScript source file must have '.ss' extension.");
        final List<SSExecutionFlags> flags = new ArrayList<>();
        final List<String> tail = Arrays.stream(ARGS).skip(1).collect(Collectors.toList());
        for (String arg : tail) {
            if (!EXECUTION_FLAGS_MAP.containsKey(arg))
                exitWithMessage("Error: invalid execution flag '%s'.", arg);
            if (flags.contains(arg))
                exitWithMessage("Error: duplicate execution flags.");
            flags.add(EXECUTION_FLAGS_MAP.get(arg));
        }
        Main.PROGRAM_PATH = path;
        Main.EXECUTION_FLAGS = flags;
    }

    public static void process() throws IOException {
        final Path programPath = PROGRAM_PATH;
        final Path programDir = PROGRAM_PATH.getParent();
        log("Setting environment variables ...");
        Environment.putEnvVariable(Environment.EXECUTABLE_PATH, Value.of(programPath.toString()), true);
        Environment.putEnvVariable(Environment.EXECUTABLE_DIR, Value.of(programDir.toString()), true);
        try {
            log("Generating tokens ...");
            final SimpleScript script = SimpleScript.fromFile(programPath);
            execute(script);
        } catch (SSLexerException e) {
            exitWithMessage("Error: \n\t%s", e.getMessage());
        }
    }

    public static void execute(SimpleScript script) {
        log("Parsing source code ...");
        if (!script.isCompilabe())
            exitWithMessage("Error: \n\t%s", Objects.requireNonNull(script.tryCompile()).getMessage());
        if (ARGS.length > 1) {
            if (EXECUTION_FLAGS.contains(EXECUTION_FLAGS_MAP.get("-t")))
                getTokens(script);
            if (EXECUTION_FLAGS.contains(EXECUTION_FLAGS_MAP.get("-p")))
                getProgram(script);
            if (EXECUTION_FLAGS.contains(EXECUTION_FLAGS_MAP.get("-e")))
                run(script);
        } else run(script);
    }

    public static void run(SimpleScript script) {
        script.compile().execute();
    }

    public static void getTokens(SimpleScript script) {
        System.out.println("TOKENS: ");
        script.getTokens().forEach(token -> System.out.printf("\t%s\n", token));
        System.out.println("END.");
    }

    public static void getProgram(SimpleScript script) {
        System.out.println("PROGRAM: ");
        System.out.println(script.compile().getProgram().toString());
        System.out.println("END.");
    }

    public static void log(String message, Object... format) {
        if (LOG)
            System.out.println(String.format(message, format));
    }

    public static void exitWithMessage(String message, Object... format) {
        System.err.println(String.format(message, format));
        System.exit(0);
    }

}