package com.evg.ss.lib;

import com.evg.ss.Environment;
import com.evg.ss.exceptions.execution.ModuleLoadingException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Linker {

    private static final Map<String, Path> links = new HashMap<>();

    public static void updateLinks() {
        try {
            final Path execDir = Paths.get(Objects.requireNonNull(Environment.getEnvVariable(Environment.EXECUTABLE_DIR)).asString());
            final File[] files = new File(execDir.toString()).listFiles();
            for (File file : Objects.requireNonNull(files)) {
                if (file.getName().endsWith(".ss")) {
                    final String fileName = file.getName();
                    final String name = fileName.substring(0, fileName.length() - 3);
                    links.put(name, file.toPath().toAbsolutePath());
                }
            }
        } catch (Exception e) {
            throw new ModuleLoadingException("$local_link", e);
        }
    }

    public static Path getLink(String name) {
        updateLinks();
        if (links.containsKey(name))
            return links.get(name);
        return null;
    }

}