package com.massivedisaster.bintraydeployautomator.utils;

import com.sun.istack.internal.NotNull;

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

    /**
     * Replace every semantic version occurrence in  specified file.
     *
     * @param newVersion version to replace.
     * @param fileName   the file to search.
     * @throws IOException if file don't exist.
     */
    public static void replaceAllSemVerInFile(@NotNull String newVersion, @NotNull String fileName) throws IOException {
        String readme = readFileAsString(fileName);
        String readmeVersionReplaced = readme.replaceAll("(\\d+(\\.\\d+){2})(\\-[\\w\\d\\.\\-]*)?(\\+[\\w\\d\\.\\-]*)?", newVersion);

        try (PrintWriter output = new PrintWriter(fileName)) {
            output.print(readmeVersionReplaced);
        }
    }
}
