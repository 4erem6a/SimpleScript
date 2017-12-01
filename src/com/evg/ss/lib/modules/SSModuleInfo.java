package com.evg.ss.lib.modules;

public final class SSModuleInfo {

    private String name;
    private String importName;
    private String author;

    public SSModuleInfo(String name) {
        this(name, name);
    }

    public SSModuleInfo(String name, String importName) {
        this(name, importName, "unknown");
    }

    public SSModuleInfo(String name, String importName, String author) {
        this.name = name;
        this.importName = importName;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public String getImportName() {
        return importName;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return String.format("<%s>%s:%s", importName, name, author);
    }
}