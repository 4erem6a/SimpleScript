package com.evg.ss.lib.modules.lists;

import com.evg.ss.exceptions.execution.IndexOutOfBoundsException;
import com.evg.ss.lib.modules.SSModule;
import com.evg.ss.util.args.Arguments;
import com.evg.ss.util.builders.SSMapBuilder;
import com.evg.ss.values.ArrayValue;
import com.evg.ss.values.MapValue;
import com.evg.ss.values.NullValue;
import com.evg.ss.values.NumberValue;

import java.util.Arrays;

public final class lists extends SSModule {
    private static ListValue getListBase() {
        final ListValue list = new ListValue();
        final SSMapBuilder builder = SSMapBuilder.create();
        builder.setMethod("add", v -> {
            Arguments.checkArgcOrDie(v, 1);
            list.add(v[0]);
            return new NullValue();
        });
        builder.setMethod("remove", v -> {
            Arguments.checkArgcOrDie(v, 1);
            list.remove(v[0]);
            return new NullValue();
        });
        builder.setMethod("remove", v -> {
            Arguments.checkArgTypesOrDie(v, NumberValue.class);
            final int index = v[0].asNumber().intValue();
            if (index < 0 || index >= list.size())
                throw new IndexOutOfBoundsException(index);
            list.remove(index);
            return new NullValue();
        });
        builder.setMethod("addAll", v -> {
            Arguments.checkArgTypesOrDie(v, ListValue.class);
            list.addAll((ListValue) v[0]);
            return new NullValue();
        });
        builder.setMethod("addAll", v -> {
            Arguments.checkArgTypesOrDie(v, NumberValue.class, ListValue.class);
            final int index = v[0].asNumber().intValue();
            if (index < 0 || index >= list.size())
                throw new IndexOutOfBoundsException(index);
            list.addAll(index, (ListValue) v[0]);
            return new NullValue();
        });
        builder.setMethod("get", v -> {
            Arguments.checkArgTypesOrDie(v, NumberValue.class);
            final int index = v[0].asNumber().intValue();
            if (index < 0 || index >= list.size())
                throw new IndexOutOfBoundsException(index);
            return list.get(index);
        });
        builder.setMethod("toArray", v -> {
            Arguments.checkArgcOrDie(v, 0);
            return list.toArray();
        });
        return new ListValue(list, builder.build());
    }

    @Override
    public MapValue require() {
        final SSMapBuilder lists = SSMapBuilder.create();
        lists.setMethod("fromArray", v -> {
            Arguments.checkArgTypesOrDie(v, ArrayValue.class);
            final ListValue list = getListBase();
            Arrays.stream(((ArrayValue) v[0]).getValue()).forEach(list::add);
            return list;
        });
        lists.setMethod("of", v -> {
            final ListValue list = getListBase();
            Arrays.stream(v).forEach(list::add);
            return list;
        });
        lists.setMethod("new", v -> {
            Arguments.checkArgcOrDie(v, 0);
            return getListBase();
        });
        return lists.build();
    }
}