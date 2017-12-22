package desktop;

import com.evg.ss.Environment;
import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.execution.SSExecutionException;
import com.evg.ss.lib.CallStack;
import com.evg.ss.lib.msc.MSCVisitor;
import com.evg.ss.linter.LintException;
import com.evg.ss.values.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public final class Desktop {

    private static final Map<String, SSExecutionFlags> EXECUTION_FLAGS_MAP = new HashMap<>();
    private static List<Value> PROGRAM_ARGS = new ArrayList<>();
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
                PROGRAM_ARGS.add(Value.of(arg));
                continue;
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
        log("Complete\n");

        try {
            log("Generating tokens ... ");
            final SimpleScript script = SimpleScript.fromFile(programPath);
            log("Complete\n");
            parse(script);
        } catch (Exception e) {
            except(e);
        }
    }

    private static void parse(SimpleScript script) {
        log("Parsing source code ... ");
        if (!script.isCompilable())
            except(script.tryCompile());
        log("Complete\n");
        lint(script);
    }

    private static void lint(SimpleScript script) {
        log("Running lint ... ");
        try {
            script.compile().lint();
        } catch (LintException e) {
            exitWithMessage("Lint error: \n\t%s", e.getMessage());
        }
        log("Complete\n");
        execute(script);
    }

    public static void execute(SimpleScript script) {
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
        System.out.println(script.compile().getProgram().accept(new MSCVisitor()));
        System.out.println("END.");
    }

    private static void log(String message, Object... format) {
        if (LOG)
            System.out.printf(message, format);
    }

    private static void except(Exception e) {
        if (DEBUG)
            e.printStackTrace();
        if (e instanceof SSExecutionException)
            exitWithMessage("Error: \n\t%s\n\t%s\nStackTrace: \n%s",
                    e.getClass().getSimpleName(),
                    e.getMessage(),
                    CallStack.getCalls().stream().map(call -> call.toString() + "\n").reduce(String::concat).get());
        else exitWithMessage("Error: \n\t%s\n\t%s\n", e.getClass().getSimpleName(), e.getMessage());
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