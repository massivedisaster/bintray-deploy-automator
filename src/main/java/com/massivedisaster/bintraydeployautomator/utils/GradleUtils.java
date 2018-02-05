package com.massivedisaster.bintraydeployautomator.utils;


import com.massivedisaster.bintraydeployautomator.annotations.NotNull;
import org.gradle.tooling.ProjectConnection;

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
    public static void runGradle(@NotNull ProjectConnection gradleConnection, @NotNull String output, @NotNull String[] tasks,
                                 @NotNull String... arguments) throws IOException {
        OutputStream os;
        OutputStream osErr;
        if (output != null) {
            os = new FileOutputStream(FileUtils.createFile(output));
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

        if (output != null) {
            os.close();
            osErr.close();
        }
    }
}
