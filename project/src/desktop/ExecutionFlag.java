package desktop;

import java.util.ArrayList;
import java.util.List;

public final class ExecutionFlag {

    private final SSExecutionFlags type;
    private List<String> args = new ArrayList<>();

    public ExecutionFlag(SSExecutionFlags type) {
        this.type = type;
    }

    public SSExecutionFlags getType() {
        return type;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public void addArg(String arg) {
        this.args.add(arg);
    }

    public String getSingleArg() {
        if (args.size() == 0)
            return null;
        return args.stream().reduce(String::concat).get();
    }

    @Override
    public int hashCode() {
        return type.hashCode() ^ args.hashCode();
    }
}