package com.massivedisaster.bintraydeployautomator;

import com.massivedisaster.bintraydeployautomator.model.Arguments;
import com.massivedisaster.bintraydeployautomator.model.Configuration;
import com.massivedisaster.bintraydeployautomator.utils.CommandLineUtils;
import com.massivedisaster.bintraydeployautomator.utils.ConsoleUtils;
import com.massivedisaster.bintraydeployautomator.utils.GradleUtils;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import java.io.File;

/**
 * Bintray deploy automator.
 */
public class Automator {

    /**
     * Main method.
     *
     * @param args command line arguments.
     */
    public static void main(String args[]) {

        ProjectConnection gradleConnection = null;
        try {
            Arguments auth = CommandLineUtils.commandLineArgs(args);

            // Get configuration from .json.
            Configuration configuration = Configuration.parseConfiguration("configuration.json");

            configuration.setBintrayUsername(auth.getUser());
            configuration.setBintrayKey(auth.getKey());
            configuration.setVerbose(auth.isVerbose());

            gradleConnection = GradleConnector.newConnector()
                    .forProjectDirectory(new File(configuration.getBasePath()))
                    .connect();

            ConsoleUtils.DrawInConsoleBox("Start process");

            // Clean build and deploy all projects.
            rebuildAndBintrayDeploy(gradleConnection, configuration);

            if (configuration.canRunExtraTasks()) {
                runExtraTasks(gradleConnection, configuration);
            }

        } catch (Exception e) {
            System.out.println("Automator Error: " + e.toString());
        } finally {
            if (gradleConnection != null) {
                gradleConnection.close();
            }
        }
    }


    /**
     * Run build and upload to bintray.
     *
     * @param gradleConnection the gradle connection.
     * @param configuration    the configuration model.
     */
    private static void rebuildAndBintrayDeploy(ProjectConnection gradleConnection, Configuration configuration) {
        GradleUtils.runGradle(gradleConnection, configuration.getTasks(), configuration.getArguments());
    }

    /**
     * Run extra tasks from project.
     *
     * @param gradleConnection the gradle connection.
     * @param configuration    the configuration model.
     */
    private static void runExtraTasks(ProjectConnection gradleConnection, Configuration configuration) {
        GradleUtils.runGradle(gradleConnection, configuration.getExtraTasks(), configuration.getArguments());
    }
}
