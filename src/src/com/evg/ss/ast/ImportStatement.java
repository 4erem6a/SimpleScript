package com.evg.ss.ast;

import com.evg.ss.lib.modules.SSModuleManager;

public final class ImportStatement implements Statement {

    private String name;

    public ImportStatement(String name) {
        this.name = name;
    }

    @Override
    public void execute() {
        SSModuleManager.importModule(name);
    }

    @Override
    public String toString() {
        return String.format("import <%s>\n", name);
    }
}
