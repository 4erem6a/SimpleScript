package com.evg.ss;

import com.evg.ss.lib.Variable;
import com.evg.ss.values.Value;

import java.util.HashMap;
import java.util.Map;

public final class Environment {

    public static final String EXECUTABLE_PATH = "EXECUTABLE_PATH";
    public static final String EXECUTABLE_DIR = "EXECUTABLE_DIR";
    public static final String CURRENT_LANG_VERSION = "CURRENT_LANG_VERSION";
    public static final String PROGRAM_ARGS = "PROGRAM_ARGS";

    private static final Map<String, Variable> VARIABLES = new HashMap<>();

    public static boolean setEnvVariable(String name, Value value) {
        if (VARIABLES.containsKey(name)) {
            final Variable variable = VARIABLES.get(name);
            if (variable.isConst())
                return false;
            VARIABLES.put(name, new Variable(value, false));
            return true;
        }
        return false;
    }

    public static Value getEnvVariable(String name) {
        if (VARIABLES.containsKey(name))
            return VARIABLES.get(name).getValue();
        return null;
    }

    public static void putEnvVariable(String name, Value value, boolean isConst) {
        VARIABLES.put(name, new Variable(value, isConst));
    }

    public static boolean envVariableExists(String name) {
        return VARIABLES.containsKey(name);
    }

}