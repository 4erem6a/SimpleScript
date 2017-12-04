package com.evg.ss.lib.modules.utils;

import com.evg.ss.lib.SS;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.lib.modules.SSModuleInfo;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.NumberValue;
import com.evg.ss.values.Value;

public final class UtilityModule implements SSModule {

    private static final String NAME = "Utilities";
    private static final String IMPORT_NAME = "utils";
    private static final String AUTHOR = "4erem6a";

    @Override
    public SSModuleInfo getModuleInfo() {
        return new SSModuleInfo(NAME, IMPORT_NAME, AUTHOR);
    }

    @Override
    public void init() {

        final SSMapBuilder module = SSMapBuilder.create();

        module.setMethod("hashCode", UtilityModule::ssHashCode);

        SS.Variables.put(IMPORT_NAME, module.build(), true);
    }

    private static Value ssHashCode(Value... args) {
        Arguments.checkArgcOrDie(args, 1);
        return new NumberValue(args[0].hashCode());
    }
}