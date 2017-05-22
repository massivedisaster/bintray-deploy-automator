package com.massivedisaster.bintraydeployautomator.utils;

import com.sun.istack.internal.NotNull;
import org.gradle.tooling.ProjectConnection;

public class GradleUtils {
    public static void runGradle(@NotNull ProjectConnection gradleConnection, @NotNull String[] tasks, @NotNull String... arguments) {
        gradleConnection.newBuild()
                .forTasks(tasks)
                .withArguments(arguments)
                .setStandardOutput(System.out)
                .setStandardError(System.err)
                .run();
    }
}
