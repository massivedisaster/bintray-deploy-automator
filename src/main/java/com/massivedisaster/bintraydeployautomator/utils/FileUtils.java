package com.massivedisaster.bintraydeployautomator.utils;

import com.massivedisaster.bintraydeployautomator.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * File utilities.
 */
public class FileUtils {
    /**
     * Reads a file as string.
     *
     * @param path path to the file.
     * @return the string with the file contents.
     * @throws IOException if file don't exist.
     */
    public static String readFileAsString(@NotNull String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, Charset.defaultCharset());
    }
    
}
