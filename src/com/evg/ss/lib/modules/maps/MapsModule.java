package com.evg.ss.lib.modules.maps;

import com.evg.ss.lib.SS;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.lib.modules.SSModuleInfo;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.*;

public final class MapsModule implements SSModule {

    private static final String NAME = "Maps";
    private static final String IMPORT_NAME = "maps";
    private static final String AUTHOR = "4erem6a";

    @Override
    public SSModuleInfo getModuleInfo() {
        return new SSModuleInfo(NAME, IMPORT_NAME, AUTHOR);
    }

    public static final MapValue MAP_EMPTY = new MapValue();

    @Override
    public void init() {

        final SSMapBuilder module = SSMapBuilder.create();

        module.setField("MAP_EMPTY", MAP_EMPTY);
        module.setMethod("toArray", MapsModule::ssToArray);
        module.setMethod("size", MapsModule::ssSize);

        SS.Variables.put(IMPORT_NAME, module.build(), true);
    }

    private static Value ssToArray(Value... args) {
        Arguments.checkArgTypesOrDie(args, Type.Map);
        return ((MapValue) args[0]).toArray();
    }

    private static Value ssSize(Value... args) {
        Arguments.checkArgTypesOrDie(args, Type.Map);
        return new NumberValue(((MapValue) args[0]).size());
    }
}