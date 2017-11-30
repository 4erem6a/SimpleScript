package com.evg.ss.lexer;

import com.evg.ss.containers.Functions;
import com.evg.ss.containers.Variables;

public final class Scopes {

    private Scopes() {}

    public static void up() {
        Variables.up();
        Functions.up();
    }

    public static void down() {
        Functions.down();
        Variables.down();
    }

}