package com.massivedisaster.bintraydeployautomator.utils;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    public static String readFile(@NotNull String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, Charset.defaultCharset());
    }

    public static void replaceSemVerInFile(@NotNull String newVersion, @NotNull String fileName) throws IOException {
        String readme = readFile(fileName);
        String readmeVersionReplaced = readme.replaceAll("(\\d+(\\.\\d+){2})(\\-[\\w\\d\\.\\-]*)?(\\+[\\w\\d\\.\\-]*)?", newVersion);

        try(PrintWriter output = new PrintWriter(fileName)){
            output.print(readmeVersionReplaced);
        }
    }
}
