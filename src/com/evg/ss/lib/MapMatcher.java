package com.evg.ss.lib;

import com.evg.ss.values.MapValue;
import com.evg.ss.values.Value;

import java.util.ArrayList;
import java.util.List;

public final class MapMatcher {

    private List<Value> keys = new ArrayList<>();

    public MapMatcher(MapValue matcher) {
        keys.addAll(matcher.getMap().keySet());
    }

    public boolean match(MapValue target) {
        return keys.stream().allMatch(target.getMap()::containsKey);
    }

}