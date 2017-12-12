package desktop;

import com.evg.ss.Environment;
import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.lexer.SSLexerException;
import com.evg.ss.values.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public final class Desktop {

    private static final Map<String, SSExecutionFlags> EXECUTION_FLAGS_MAP = new HashMap<>();
    private static List<Value> PROGRAM_ARGS;
    private static List<String> ARGS;
    private static boolean LOG, DEBUG;
    private static Path PROGRAM_PATH;
    private static List<SSExecutionFlags> EXECUTION_FLAGS;

    static {
        EXECUTION_FLAGS_MAP.put("-t", SSExecutionFlags.GET_TOKENS);
        EXECUTION_FLAGS_MAP.put("-p", SSExecutionFlags.GET_PROGRAM);
        EXECUTION_FLAGS_MAP.put("-e", SSExecutionFlags.EXECUTE);
        EXECUTION_FLAGS_MAP.put("-l", SSExecutionFlags.LOG);
        EXECUTION_FLAGS_MAP.put("-d", SSExecutionFlags.DEBUG);
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2)
            exitWithMessage("Error: invalid argument count.");
        ARGS = Arrays.asList(args);
        validate();
        process();
    }

    private static void validate() {
        final Path path = Paths.get(ARGS.get(0));
        if (!Files.exists(path))
            exitWithMessage("Error: file does not exists.");
        if (!path.toAbsolutePath().toString().endsWith(".ss"))
            exitWithMessage("Error: SimpleScript source file must have '.ss' extension.");
        final List<SSExecutionFlags> flags = new ArrayList<>();
        final List<String> tail = ARGS.stream().skip(1).collect(Collectors.toList());
        for (String arg : tail) {
            if (!EXECUTION_FLAGS_MAP.containsKey(arg)) {
                PROGRAM_ARGS = tail.stream().skip(tail.indexOf(arg)).map(Value::of).collect(Collectors.toList());
                break;
            }
            if (flags.contains(EXECUTION_FLAGS_MAP.get(arg)))
                exitWithMessage("Error: duplicate execution flags.");
            flags.add(EXECUTION_FLAGS_MAP.get(arg));
        }
        LOG = flags.contains(SSExecutionFlags.LOG);
        DEBUG = flags.contains(SSExecutionFlags.DEBUG);
        PROGRAM_PATH = path;
        EXECUTION_FLAGS = flags;
    }

    private static void process() throws IOException {
        final Path programPath = PROGRAM_PATH;
        final Path programDir = PROGRAM_PATH.getParent();
        log("Setting environment variables ...");
        Environment.putEnvVariable(Environment.EXECUTABLE_PATH, Value.of(programPath.toString()), true);
        Environment.putEnvVariable(Environment.EXECUTABLE_DIR, Value.of(programDir.toString()), true);
        Environment.putEnvVariable(Environment.PROGRAM_ARGS, Value.of(PROGRAM_ARGS.toArray(new Value[0])), true);

        try {
            log("Generating tokens ...");
            final SimpleScript script = SimpleScript.fromFile(programPath);
            execute(script);
        } catch (SSLexerException e) {
            except(e);
        }
    }

    public static void execute(SimpleScript script) {
        log("Parsing source code ...");
        if (!script.isCompilabe())
            except(script.tryCompile());
        if (EXECUTION_FLAGS.contains(EXECUTION_FLAGS_MAP.get("-t")))
            getTokens(script);
        if (EXECUTION_FLAGS.contains(EXECUTION_FLAGS_MAP.get("-p")))
            getProgram(script);
        if (EXECUTION_FLAGS.contains(EXECUTION_FLAGS_MAP.get("-e")))
            run(script);
    }

    private static void run(SimpleScript script) {
        script.compile().execute();
    }

    private static void getTokens(SimpleScript script) {
        System.out.println("TOKENS: ");
        script.getTokens().forEach(token -> System.out.printf("\t%s\n", token));
        System.out.println("END.");
    }

    private static void getProgram(SimpleScript script) {
        System.out.println("PROGRAM: ");
        System.out.println(script.compile().getProgram().toString());
        System.out.println("END.");
    }

    private static void log(String message, Object... format) {
        if (LOG)
            System.out.println(String.format(message, format));
    }

    private static void except(Exception e) {
        if (DEBUG)
            e.printStackTrace();
        exitWithMessage("Error: \n\t%s\n\t%s", e.getClass().getSimpleName(), e.getMessage());
    }

    private static void exitWithMessage(String message, Object... format) {
        System.err.println(String.format(message, format));
        System.exit(0);

    }

    public enum SSExecutionFlags {
        GET_TOKENS,
        GET_PROGRAM,
        EXECUTE,
        LOG,
        DEBUG
    }

}