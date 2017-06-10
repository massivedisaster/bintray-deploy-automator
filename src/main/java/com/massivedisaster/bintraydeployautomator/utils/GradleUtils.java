package com.massivedisaster.bintraydeployautomator.utils;

import com.sun.istack.internal.NotNull;

import org.gradle.tooling.ProjectConnection;

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
    public static void runGradle(@NotNull ProjectConnection gradleConnection, @NotNull String[] tasks, @NotNull String... arguments) {
        gradleConnection.newBuild()
                .forTasks(tasks)
                .withArguments(arguments)
                .setStandardOutput(System.out)
                .setStandardError(System.err)
                .run();
    }
}
