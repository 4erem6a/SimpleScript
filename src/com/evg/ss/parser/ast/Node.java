package com.evg.ss.parser.ast;

import com.evg.ss.exceptions.execution.ModifierAlreadySetException;
import com.evg.ss.parser.visitors.ResultVisitor;
import com.evg.ss.parser.visitors.Visitor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 4erem6a
 */
public abstract class Node {

    private Map<String, String> modifiers = new HashMap<>();

    public abstract void accept(Visitor visitor);

    public abstract <TResult> TResult accept(ResultVisitor<TResult> visitor);

    public final void modify(String modifier) throws ModifierAlreadySetException {
        if (modifiers.containsKey(modifier))
            throw new ModifierAlreadySetException(modifier);
        modifiers.put(modifier, modifier);
    }

    public final boolean isModifierPresent(String name) {
        return modifiers.containsKey(name);
    }

    public final Map<String, String> getModifiers() {
        return modifiers;
    }
}