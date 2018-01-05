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
    DEBUG("-d", 0);
    private final String name;
    private final int argc;

    SSExecutionFlags(String name, int argc) {
        this.name = name;
        this.argc = argc;
    }

    public static SSExecutionFlags getFlag(String strFlag) {
        switch (strFlag) {
            case "-t":
                return TOKENIZE;
            case "-p":
                return RECOMPILE;
            case "-e":
                return EXECUTE;
            case "-f":
                return FILE;
            case "-m":
                return MODE;
            case "-l":
                return LOG;
            case "-li":
                return LINT;
            case "-a":
                return ARGS;
            case "-d":
                return DEBUG;
            case "-s":
                return SOURCE;
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