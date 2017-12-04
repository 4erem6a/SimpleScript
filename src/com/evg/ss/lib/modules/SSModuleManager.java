package com.evg.ss.lib.modules;

import com.evg.ss.exceptions.ModuleNotFoundException;
import com.evg.ss.lib.modules.arrays.ArraysModule;
import com.evg.ss.lib.modules.io.IOModule;
import com.evg.ss.lib.modules.maps.MapsModule;
import com.evg.ss.lib.modules.utils.UtilityModule;

import java.util.HashMap;
import java.util.Map;

public final class SSModuleManager {

    private SSModuleManager() {}

    private static Map<String, SSModule> modules = new HashMap<>();

    static {
        registerModule(new IOModule());
        registerModule(new ArraysModule());
        registerModule(new MapsModule());
        registerModule(new UtilityModule());
    }

    public static void registerModule(SSModule module) {
        unregisterModule(module);
        modules.put(module.getModuleInfo().getImportName(), module);
    }

    public static void unregisterModule(String name) {
        if (modules.containsKey(name))
            modules.remove(name);
    }

    public static void unregisterModule(SSModule module) {
        unregisterModule(module.getModuleInfo().getImportName());
    }

    public static void importModule(String name) {
        if (!modules.containsKey(name))
            throw new ModuleNotFoundException(name);
        modules.get(name).init();
    }

}