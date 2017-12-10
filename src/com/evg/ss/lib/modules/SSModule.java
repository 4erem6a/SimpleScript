package com.evg.ss.lib.modules;

import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.execution.ModuleLoadingException;
import com.evg.ss.exceptions.execution.ModuleNotFoundException;
import com.evg.ss.exceptions.inner.SSExportsException;
import com.evg.ss.lib.Linker;
import com.evg.ss.lib.SS;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

import java.io.IOException;
import java.util.Objects;

public abstract class SSModule {

    private static final String MODULE_PACKAGE = "com.evg.ss.lib.modules.%s.%s";

    public static Value require(String name) {
        final String className = String.format(MODULE_PACKAGE, name, name);
        try {
            return ((SSModule) Class.forName(className).getDeclaredConstructor().newInstance()).require();
        } catch (ClassNotFoundException e) {
            throw new ModuleNotFoundException(name);
        } catch (Exception e) {
            throw new ModuleLoadingException(name, e);
        }
    }

    public static Value requireLocal(String name) {
        return requireExternal(Objects.requireNonNull(Linker.getLink(name)).toString());
    }

    public static Value requireExternal(String name) {
        try {
            SimpleScript.fromFile(name).compile().execute();
        } catch (SSExportsException exports) {
            SS.Scopes.down();
            return exports.getValue();
        } catch (IOException e) {
            throw new ModuleNotFoundException(name);
        } catch (Exception e) {
            throw new ModuleLoadingException(name, e);
        }
        throw new ModuleLoadingException(name);
    }

    public abstract MapValue require();

}