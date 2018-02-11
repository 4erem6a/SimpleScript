package com.evg.ss.lib.modules;

import com.evg.ss.Environment;
import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.SSException;
import com.evg.ss.exceptions.execution.ModuleLoadingException;
import com.evg.ss.exceptions.execution.ModuleNotFoundException;
import com.evg.ss.lib.Requirable;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class SSModule implements Requirable {

    private static final String MODULE_PACKAGE = "com.evg.ss.lib.modules.%s.%s";
    private static final Map<String, SimpleScript.CompiledScript> LOADED_MODULES = new HashMap<>();

    public static String getModuleClassName(String name) {
        return String.format(MODULE_PACKAGE, name, name);
    }

    public static Map<String, SimpleScript.CompiledScript> getLoadedModules() {
        return LOADED_MODULES;
    }

    public static boolean isModuleExists(String name) {
        try {
            _require(name);
        } catch (Exception e) {
            return LOADED_MODULES.containsKey(name);
        }
        return true;
    }

    public static Value require(String name) {
        return _require(name).require();
    }

    private static Requirable _require(String name) {
        final String className = getModuleClassName(name);
        try {
            return ((SSModule) Class.forName(className).getDeclaredConstructor().newInstance());
        } catch (SSException e) {
            throw new ModuleLoadingException(name, e);
        } catch (Exception ignored) {
        }
        if (LOADED_MODULES.containsKey(name))
            return LOADED_MODULES.get(name);
        final String path;
        if (name.startsWith("/")) {
            if (!Environment.envVariableExists(Environment.EXECUTABLE_PATH))
                throw new ModuleNotFoundException(name);
            final String execPath = Environment.getEnvVariable(Environment.EXECUTABLE_PATH).asString();
            path = execPath + name;
        } else path = name;
        final SimpleScript.CompiledScript script = tryLoadModule(path);
        if (script == null)
            throw new ModuleNotFoundException(name);
        return script;
    }

    public static void loadModulesByPath(String path) {
        final File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory())
            return;
        final File[] files = dir.listFiles();
        if (files == null)
            return;
        for (File file : files)
            loadModuleByPath(file.getPath());
    }

    public static void loadModuleByPath(String path) {
        final File file = new File(path);
        if (!file.exists() || file.isDirectory())
            return;
        final SimpleScript.CompiledScript module = tryLoadModule(file.getPath());
        if (module == null)
            return;
        LOADED_MODULES.put(getPureFilename(file), module);
    }

    public static SimpleScript.CompiledScript tryLoadModule(String path) {
        final File file = new File(path);
        if (!file.exists() || !getExtension(path).equals(".ss"))
            return null;
        final String name = getPureFilename(file);
        try {
            final SimpleScript script = SimpleScript.fromFile(file);
            final Exception exception = script.tryCompile();
            if (exception != null)
                throw new ModuleLoadingException(name, exception);
            return script.compile();
        } catch (ModuleLoadingException e) {
            throw e;
        } catch (SSException e) {
            throw new ModuleLoadingException(name, e);
        } catch (Exception ignored) {
        }
        return null;
    }

    public static SimpleScript.CompiledScript loadModule(String path) {
        try {
            return tryLoadModule(path);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static boolean isNative(String name) {
        try {
            tryLoadModule(name);
        } catch (Exception e) {
            return isModuleExists(name);
        }
        return false;
    }

    private static String getPureFilename(File file) {
        return file.getName().substring(0, file.getName().length() - getExtension(file.getName()).length());
    }

    private static String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.'));
    }

    public abstract MapValue require();
}