package com.evg.ss.lib.modules.canvas;

import com.evg.ss.SimpleScript;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.Utils;
import com.evg.ss.values.MapValue;

public final class canvas extends SSModule {

    private static final String FILENAME = "canvas.ss";

    @Override
    public MapValue require() {
        final String source = Utils.istream2string(getClass().getResourceAsStream(FILENAME));
        return SimpleScript.fromSource(source).compile().require();
    }
}