package com.massivedisaster.bintraydeployautomator.utils;


import com.massivedisaster.bintraydeployautomator.annotations.NotNull;
import org.gradle.tooling.ProjectConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Gradle utilities.
 */
public class GradleUtils {
    /**
     * Runs gradle with specified parameters.
     *
     * @param gradleConnection the gradle connection.
     * @param tasks            gradle tasks to execute.
     * @param arguments        arguments.
     */
    public static void runGradle(@NotNull ProjectConnection gradleConnection, @NotNull String output, @NotNull String[] tasks, @NotNull String... arguments) {
        try {
            OutputStream os;
            OutputStream osErr;
            if (output != null) {
                File outFile = new File(output);
                File parent = outFile.getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                }
                outFile.createNewFile();
                os = new FileOutputStream(outFile);
                osErr = os;
            } else {
                os = System.out;
                osErr = System.err;
            }
            gradleConnection.newBuild()
                    .forTasks(tasks)
                    .withArguments(arguments)
                    .setStandardOutput(os)
                    .setStandardError(osErr)
                    .run();

            os.close();
            osErr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
