package com.evg.ss.lib.modules.maps;

import com.evg.ss.lib.SS;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.lib.modules.SSModuleInfo;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.MapValue;

public final class MapsModule implements SSModule {

    private static final String NAME = "Maps";
    private static final String IMPORT_NAME = "maps";
    private static final String AUTHOR = "4erem6a";

    @Override
    public SSModuleInfo getModuleInfo() {
        return new SSModuleInfo(NAME, IMPORT_NAME, AUTHOR);
    }

    @Override
    public void init() {

        final MapValue module = SSMapBuilder.create()
                .setField("MAP_EMPTY", MapValue.MAP_EMPTY)
                .setField("MAP_BASE", MapValue.MAP_BASE)
                .build();

        SS.Variables.put(IMPORT_NAME, module, true);

    }
}