package com.evg.ss.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Utils {

    public static String istream2string(InputStream istream) {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = istream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        } catch (IOException e) {
            return null;
        }
    }

    public static String loadSource(Path path) {
        try {
            return istream2string(Files.newInputStream(path));
        } catch (IOException e) {
            return null;
        }
    }

}