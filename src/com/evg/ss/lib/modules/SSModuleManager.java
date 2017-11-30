package com.evg.ss.lib.modules;

import com.evg.ss.exceptions.ModuleNotFoundException;

import java.util.HashMap;
import java.util.Map;

public final class SSModuleManager {

    private SSModuleManager() {}

    private static Map<String, SSModule> modules = new HashMap<>();

    static {
        modules.put("std", new StdModule());
    }

    public static void registerModule(String name, SSModule module) {
        unregisterModule(name);
        modules.put(name, module);
    }

    public static void unregisterModule(String name) {
        if (modules.containsKey(name))
            modules.remove(name);
    }

    public static void importModule(String name) {
        if (!modules.containsKey(name))
            throw new ModuleNotFoundException(name);
        modules.get(name).init();
    }

}