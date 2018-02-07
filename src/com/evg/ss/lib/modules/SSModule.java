package com.evg.ss.lib.modules;

import com.evg.ss.Environment;
import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.SSException;
import com.evg.ss.exceptions.execution.ModuleLoadingException;
import com.evg.ss.exceptions.execution.ModuleNotFoundException;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class SSModule {

    private static final String MODULE_PACKAGE = "com.evg.ss.lib.modules.%s.%s";
    private static final Map<String, SimpleScript.CompiledScript> LOADED_MODULES = new HashMap<>();

    public static String getModuleClassName(String name) {
        return String.format(MODULE_PACKAGE, name, name);
    }

    public static boolean isModuleExists(String name) {
        try {
            require(name);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Value require(String name) {
        final String className = String.format(MODULE_PACKAGE, name, name);
        try {
            return ((SSModule) Class.forName(className).getDeclaredConstructor().newInstance()).require();
        } catch (SSException e) {
            throw new ModuleLoadingException(name, e);
        } catch (Exception ignored) {}
        if (LOADED_MODULES.containsKey(name))
            return LOADED_MODULES.get(name).require();
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
        return script.require();
    }

    public static void loadModulesByPath(String path) {
        final File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory())
            return;
        final File[] files = dir.listFiles();
        if (files == null)
            return;
        for (File file : files) {
            final SimpleScript.CompiledScript script = tryLoadModule(file.getPath());
            if (script == null)
                continue;
            LOADED_MODULES.put(getPureFilename(file), script);
        }
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
        }
        catch (ModuleLoadingException e) { throw e; }
        catch (SSException e) {
            throw new ModuleLoadingException(name, e);
        }
        catch (Exception ignored) {}
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