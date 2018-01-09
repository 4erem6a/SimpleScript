package com.evg.ss.parser.ast;

public interface Lockable {

    boolean isLocked();

    void setLocked(boolean locked);

    void lock();

    void unlock();

}