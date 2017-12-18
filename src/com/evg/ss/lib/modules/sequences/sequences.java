package com.evg.ss.lib.modules.sequences;

import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.execution.ModuleLoadingException;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.values.MapValue;

import java.io.IOException;

public final class sequences extends SSModule {

    private static final String LIBRARY_PATH = "src\\com\\evg\\ss\\lib\\modules\\sequences\\sequences.ss";

    @Override
    public MapValue require() {
        try {
            return SimpleScript.fromFile(LIBRARY_PATH).compile().require();
        } catch (IOException e) {
            throw new ModuleLoadingException("sequences", e);
        }
    }

}