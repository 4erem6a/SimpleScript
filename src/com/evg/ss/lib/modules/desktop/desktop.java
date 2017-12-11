package com.evg.ss.lib.modules.desktop;

import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;
import desktop.Desktop;

public class desktop extends SSModule {
    @Override
    public MapValue require() {
        final SSMapBuilder desktop = SSMapBuilder.create();
        desktop.setField("args", Value.of(Desktop.PROGRAM_ARGS.toArray(new Value[0])));
        return desktop.build();
    }
}
