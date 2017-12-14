package com.evg.ss.lib.mssp;

import com.evg.ss.SimpleScript;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class MSCGenerator {

    private SimpleScript.CompiledScript script;
    private Map<String, Path> links;

    public MSCGenerator(SimpleScript.CompiledScript script) {
        this.script = script;
        final LinkVisitor visitor = new LinkVisitor();
        script.acceptVisitor(visitor);
        this.links = visitor.getLinkedModules();
    }

    private String loadSource(Path path) throws IOException {
        return new String(Files.readAllBytes(path), "UTF-8");
    }

//    public String generate() {
//
//    }

}