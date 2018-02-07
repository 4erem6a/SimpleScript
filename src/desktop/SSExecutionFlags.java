package desktop;

public enum SSExecutionFlags {
    TOKENIZE("-t", 0),
    RECOMPILE("-r", 0),
    EXECUTE("-e", 0),
    FILE("-f", 1),
    SOURCE("-s", 1),
    CHARSET("-c", 1),
    MODE("-m", 1),
    ARGS("-a", -1),
    LOG("-l", 0),
    LINT("-lt", 0),
    DEBUG("-d", 0),
    MODULE_PATH("-mp", 1);
    private final String name;
    private final int argc;

    SSExecutionFlags(String name, int argc) {
        this.name = name;
        this.argc = argc;
    }

    public static SSExecutionFlags getFlag(String strFlag) {
        switch (strFlag) {
            case "-t":
            case "--tokenize":
                return TOKENIZE;
            case "-e":
            case "--execute":
                return EXECUTE;
            case "-f":
            case "--file":
                return FILE;
            case "-m":
            case "--mode":
                return MODE;
            case "-l":
            case "--log":
                return LOG;
            case "-li":
            case "--lint":
                return LINT;
            case "-a":
            case "--args":
                return ARGS;
            case "-d":
            case "--debug":
                return DEBUG;
            case "-s":
            case "--source":
                return SOURCE;
            case "-r":
            case "--recompile":
                return RECOMPILE;
            case "-c":
            case "--charset":
                return CHARSET;
            case "-mp":
            case "--modulepath":
                return MODULE_PATH;
            default:
                return null;
        }
    }

    public String getName() {
        return name;
    }

    public int getArgc() {
        return argc;
    }
}