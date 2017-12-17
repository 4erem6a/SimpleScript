package com.evg.ss.lib.modules.lists;

import com.evg.ss.SimpleScript;
import com.evg.ss.exceptions.execution.ModuleLoadingException;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.values.MapValue;

import java.io.IOException;

public final class lists extends SSModule {

    private static final String LIBRARY_PATH = "src\\com\\evg\\ss\\lib\\modules\\lists\\lists.ss";

    @Override
    public MapValue require() {
        try {
            return SimpleScript.fromFile(LIBRARY_PATH).compile().require();
        } catch (IOException e) {
            throw new ModuleLoadingException("lists", e);
        }
    }

}