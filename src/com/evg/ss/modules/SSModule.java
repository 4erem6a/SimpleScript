package com.evg.ss.modules;

import com.evg.ss.Environment;
import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.SSException;
import com.evg.ss.exceptions.execution.ModuleLoadingException;
import com.evg.ss.exceptions.execution.ModuleNotFoundException;
import com.evg.ss.lib.Function;
import com.evg.ss.lib.Requirable;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.UndefinedValue;
import com.evg.ss.values.Value;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class SSModule implements Requirable {

    private static final String MODULES_PACKAGE = "com.evg.ss.modules.%s.%s";
    private static final String MODULES_PATH = "src/com/evg/ss/modules";
    private static final Map<String, Requirable> LOADED_MODULES = new HashMap<>();

    public static String getModuleClassName(String name) {
        return String.format(MODULES_PACKAGE, name, name);
    }

    public static Map<String, Requirable> getLoadedModules() {
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
        try {
            final Class<?> _class = Class.forName(getModuleClassName(name));
            if (_class.getSuperclass().equals(SSModule.class))
                return ((SSModule) _class.getDeclaredConstructor().newInstance());
            else return loadStatic(_class);
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

    private static Requirable loadStatic(Class<?> moduleClass) {
        final List<Method> exportMethods = Arrays.stream(moduleClass.getMethods())
                .filter(m -> m.getParameterCount() == 1)
                .filter(m -> m.getReturnType().equals(Value.class))
                .filter(m -> m.getParameterTypes()[0].equals(Value[].class))
                .filter(Method::isVarArgs)
                .filter(m -> m.isAnnotationPresent(SSExports.class))
                .collect(Collectors.toList());
        final List<Field> exportFields = Arrays.stream(moduleClass.getFields())
                .filter(f -> f.getType().equals(Value.class))
                .filter(f -> f.isAnnotationPresent(SSExports.class))
                .filter(f -> Modifier.isStatic(f.getModifiers()))
                .filter(f -> Modifier.isFinal(f.getModifiers()))
                .collect(Collectors.toList());
        final SSMapBuilder builder = new SSMapBuilder();
        for (Method method : exportMethods) {
            final String methodExportName = method.getAnnotation(SSExports.class).value();
            final Function function = args -> {
                try {
                    return ((Value) method.invoke(null, (Object) args));
                } catch (Exception ignored) {
                    return new UndefinedValue();
                }
            };
            builder.setMethod(methodExportName, function);
        }
        for (Field field : exportFields) {
            try {
                final String fieldExportName = field.getAnnotation(SSExports.class).value();
                final Value value = ((Value) field.get(null));
                builder.setField(fieldExportName, value);
            } catch (IllegalAccessException ignored) {
            }
        }
        return builder::build;
    }

    public abstract MapValue require();
}