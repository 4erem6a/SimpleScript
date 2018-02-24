package com.evg.ss.lib;

import java.util.*;
import java.util.function.Consumer;

public final class Arguments implements Iterable<Argument> {

    private final List<Argument> arguments = new ArrayList<>();

    public Arguments(Collection<? extends Argument> args) {
        arguments.addAll(args);
    }

    public Arguments(Argument... args) {
        arguments.addAll(Arrays.asList(args));
    }

    public Arguments() {
    }

    public Arguments add(Argument arg) {
        arguments.add(arg);
        return this;
    }

    public Arguments addAll(Collection<? extends Argument> collection) {
        arguments.addAll(collection);
        return this;
    }

    public Arguments addAll(Argument... args) {
        arguments.addAll(Arrays.asList(args));
        return this;
    }

    public List<Argument> toList() {
        return arguments;
    }

    public Argument[] toArray() {
        return arguments.toArray(new Argument[0]);
    }

    @Override
    public Iterator<Argument> iterator() {
        return arguments.iterator();
    }

    @Override
    public void forEach(Consumer<? super Argument> action) {
        arguments.forEach(action);
    }

    @Override
    public Spliterator<Argument> spliterator() {
        return arguments.spliterator();
    }

    public int size() {
        return arguments.size();
    }

    public Argument get(int index) {
        return arguments.get(index);
    }

    public boolean isVariadic() {
        return arguments.stream().anyMatch(Argument::isVariadic);
    }

    private boolean isRemainderDefault(int index) {
        for (int i = index; i < size(); i++)
            if (!get(i).hasValue() && !get(i).isVariadic())
                return false;
        return true;
    }

    public int minArgc() {
        for (int i = 0; i < size(); i++)
            if (isRemainderDefault(i))
                return i;
        return 0;
    }

    public int maxArgc() {
        return isVariadic() ? -1 : size();
    }

    @Override
    public int hashCode() {
        return arguments.hashCode();
    }
}