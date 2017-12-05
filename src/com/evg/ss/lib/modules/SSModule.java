package com.evg.ss.lib.modules;

import com.evg.ss.ast.Statement;
import com.evg.ss.exceptions.ModuleLoadingException;
import com.evg.ss.exceptions.ModuleNotFoundException;
import com.evg.ss.exceptions.inner.SSExportsException;
import com.evg.ss.lexer.Lexer;
import com.evg.ss.lib.SS;
import com.evg.ss.parser.Parser;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class SSModule {

    private static final String MODULE_PACKAGE = "com.evg.ss.lib.modules.%s.%s";

    public static Value require(String name) {
        final String className = String.format(MODULE_PACKAGE, name, name);
        try {
            return ((SSModule)Class.forName(className).getDeclaredConstructor().newInstance()).require();
        } catch (ClassNotFoundException e) {
            throw new ModuleNotFoundException(name);
        } catch (Exception e) {
            throw new ModuleLoadingException(name, e);
        }
    }

    public static Value requireExternal(String name) {
        try {
            final String externalSource = new String(Files.readAllBytes(Paths.get(name)), "UTF-8");
            final Statement externalProgram = new Parser(new Lexer(externalSource).tokenize()).parse();
            try {
                externalProgram.execute();
            } catch (SSExportsException e) {
                SS.Scopes.down();
                return e.getValue();
            }
        } catch (IOException e) {
            throw new ModuleNotFoundException(name);
        } catch (Exception e) {
            throw new ModuleLoadingException(name, e);
        }
        throw new ModuleLoadingException(name);
    }

    public abstract MapValue require();

}