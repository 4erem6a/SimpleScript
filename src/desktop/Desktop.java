package desktop;

import com.evg.ss.Environment;
import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.SSLintException;
import com.evg.ss.exceptions.SSThrownException;
import com.evg.ss.exceptions.execution.SSExecutionException;
import com.evg.ss.exceptions.inner.SSInnerException;
import com.evg.ss.exceptions.lexer.SSLexerException;
import com.evg.ss.lib.CallStack;
import com.evg.ss.lib.SS;
import com.evg.ss.lib.msc.MSCGenerator;
import com.evg.ss.linter.Linter;
import com.evg.ss.parser.ast.Statement;
import com.evg.ss.parser.visitors.FunctionAdder;
import com.evg.ss.values.FunctionValue;
import com.evg.ss.values.Value;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Desktop {

    private static List<ExecutionFlag> EXECUTION_FLAGS = new ArrayList<>();
    private static boolean LOG = false, DEBUG = false;
    private static SSExecutionModes MODE = SSExecutionModes.FILE;

    private static boolean hasFlag(String name) {
        return EXECUTION_FLAGS.stream().anyMatch(flag -> flag.getType().getName().equals(name));
    }

    private static ExecutionFlag getFlag(String name) {
        if (hasFlag(name))
            return EXECUTION_FLAGS.stream().filter(flag -> flag.getType().getName().equals(name)).findFirst().get();
        else return null;
    }


    public static void main(String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            final SSExecutionFlags flagType = SSExecutionFlags.getFlag(args[i]);
            if (flagType == null)
                exitWithMessage("Invalid execution flag: '%s'.", args[i]);
            final ExecutionFlag flag = new ExecutionFlag(flagType);
            if (flag.getType().getArgc() != 0) {
                if (flag.getType().getArgc() == -1)
                    while (i + 1 < args.length || (i + 1 < args.length && SSExecutionFlags.getFlag(args[i + 1]) != null))
                        flag.addArg(args[++i]);
                else for (int j = 0; j < flag.getType().getArgc(); j++, ++i) {
                    if (i + 1 == args.length)
                        exitWithMessage("Missing arguments: %d.", flag.getType().getArgc() - j + 1);
                    flag.addArg(args[i + 1]);
                }
            }
            if (hasFlag(flag.getType().getName()))
                exitWithMessage("Duplicate execution flags: '%s'.", flag.getType().getName());
            EXECUTION_FLAGS.add(flag);
        }
        LOG = hasFlag("-l");
        DEBUG = hasFlag("-d");
        if (!hasFlag("-f") && !hasFlag("-s"))
            exitWithMessage("Missing source ('-f'/'-s').");
        if (hasFlag("-f") && hasFlag("-s"))
            exitWithMessage("Duplicate source flags ('-f'/'-s').");
        final String source;
        if (hasFlag("-f")) {
            final Path path = Paths.get(getFlag("-f").getSingleArg()).toAbsolutePath();
            if (!Files.exists(path))
                exitWithMessage("File does not exists.");
            final String charset;
            if (hasFlag("-c"))
                charset = getFlag("-c").getSingleArg();
            else charset = "UTF-8";
            if (!Charset.isSupported(charset))
                exitWithMessage("Charset not supported.");
            source = new String(Files.readAllBytes(path), charset);
        } else source = getFlag("-s").getSingleArg();
        if (hasFlag("-m")) {
            final String arg = getFlag("-m").getSingleArg();
            if (arg.toLowerCase().equals("main"))
                MODE = SSExecutionModes.MAIN;
            else if (arg.toLowerCase().equals("file"))
                MODE = SSExecutionModes.FILE;
            else if (arg.toLowerCase().equals("expression"))
                MODE = SSExecutionModes.EXPRESSION;
            else exitWithMessage("Invalid execution mode.");
        }
        log("Generating tokens ... ");
        final SimpleScript script;
        try {
            script = SimpleScript.fromSource(source);
        } catch (SSLexerException e) {
            except(e);
            return;
        }
        log("Complete.\n");
        log("Parsing tokens ... ");
        if (MODE == SSExecutionModes.EXPRESSION) {
            if (!script.isExpressible())
                except(script.tryExpress());
        } else if (!script.isCompilable())
            except(script.tryCompile());
        final SimpleScript.CompiledScript compiledScript = script.compile();
        log("Complete.\n");
        log("Setting environment variables ... ");
        if (hasFlag("-f")) {
            final Path path = Paths.get(getFlag("-f").getSingleArg()).toAbsolutePath();
            Environment.putEnvVariable(Environment.EXECUTABLE_PATH, Value.of(path.toString()), true);
            Environment.putEnvVariable(Environment.EXECUTABLE_DIR, Value.of(path.getParent().toString()), true);
        }
        Environment.putEnvVariable(Environment.CURRENT_LANG_VERSION, Value.of(SimpleScript.VERSION.toString()), true);
        log("Complete.\n");
        if (hasFlag("-lt")) {
            log("Running lint ... ");
            try {
                new Linter(compiledScript).lint();
            } catch (SSLintException e) {
                except(e);
                return;
            }
            log(" Complete.\n");
        }
        if (hasFlag("-r"))
            System.out.printf("PROGRAM:\n%s\nEND.\n", new MSCGenerator(compiledScript).generate());
        if (hasFlag("-t"))
            System.out.printf("TOKENS:\n%sEND.\n", script.getTokens().stream()
                    .map(token -> '\t' + token.toString() + '\n')
                    .reduce(String::concat).get());
        if (hasFlag("-p"))
            System.out.printf("PROGRAM:\n%sEND.\n",
                    new MSCGenerator(compiledScript.getProgram()).generate());
        if (hasFlag("-e")) {
            if (MODE == SSExecutionModes.FILE) {
                try {
                    compiledScript.execute();
                } catch (SSThrownException e) {
                    exitWithMessage("Uncaught thrown value: %s", e.getValue().asString());
                } catch (Exception e) {
                    except(e);
                }
            }
            if (MODE == SSExecutionModes.EXPRESSION) {
                try {
                    System.out.println("> " + script.express().eval().asString());
                } catch (Exception e) {
                    except(e);
                }
            }
            if (MODE == SSExecutionModes.MAIN) {
                final Statement program = compiledScript.getProgram();
                program.accept(new FunctionAdder());
                if (!SS.Identifiers.exists("main") || !(SS.Identifiers.get("main").getValue() instanceof FunctionValue))
                    exitWithMessage("Missing function 'main'.");
                final Value programArgs =
                        Value.of(
                                (hasFlag("-a") ? getFlag("-a").getArgs() : new ArrayList<String>())
                                        .stream()
                                        .map(Value::of)
                                        .toArray(Value[]::new));
                try {
                    ((FunctionValue) SS.Identifiers.get("main").getValue()).getValue().execute(programArgs);
                } catch (SSThrownException e) {
                    exitWithMessage("Uncaught thrown value: %s", e.getValue().asString());
                } catch (Exception e) {
                    except(e);
                }
            }
        }
    }

    private static void log(String message, Object... format) {
        if (LOG)
            System.out.printf(message, format);
    }

    private static void except(Exception e) {
        final Optional<String> stackTrace = CallStack.getCalls().stream()
                .map(call -> call.toString() + "\n")
                .reduce(String::concat);
        if (DEBUG)
            e.printStackTrace();
        if (e instanceof SSExecutionException)
            exitWithMessage("\nError: \n\t%s\n\t%s\nStackTrace: \n%s\n",
                    e.getClass().getSimpleName(),
                    e.getMessage(),
                    stackTrace.orElse("--- no calls ---"));
        else if (e instanceof SSInnerException) {
            exitWithMessage("\nError: \n\t%s\n\t%s\n",
                    e.getClass().getSimpleName(),
                    ((SSInnerException) e).onInvalidUsage());
        } else exitWithMessage("\nError: \n\t%s\n\t%s\n", e.getClass().getSimpleName(), e.getMessage());
    }

    private static void exitWithMessage(String message, Object... format) {
        System.err.println(String.format(message, format));
        System.exit(0);
    }
}