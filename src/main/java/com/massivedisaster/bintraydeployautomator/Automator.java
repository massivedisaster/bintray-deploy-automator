package com.massivedisaster.bintraydeployautomator;

import com.massivedisaster.bintraydeployautomator.model.Configuration;
import com.massivedisaster.bintraydeployautomator.utils.CommandLineUtils;
import com.massivedisaster.bintraydeployautomator.utils.FileUtils;
import com.massivedisaster.bintraydeployautomator.utils.GradleUtils;
import javafx.util.Pair;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import java.io.File;

/**
 * Bintray deploy automator.
 */
public class Automator extends FileUtils {

    /**
     * Main method.
     *
     * @param args command line arguments.
     */
    public static void main(String args[]) {

        ProjectConnection gradleConnection = null;
        try {
            Pair<String, String> auth = CommandLineUtils.commandLineArgs(args);

            // Get configuration from .json.
            Configuration configuration = Configuration.parseConfiguration("configuration.json");

            configuration.setBintrayUsername(auth.getKey());
            configuration.setBintrayKey(auth.getValue());

            gradleConnection = GradleConnector.newConnector()
                    .forProjectDirectory(new File(configuration.getBasePath()))
                    .connect();

            // Clean build and deploy all projects.
            rebuildAndBintrayDeploy(gradleConnection, configuration);

            // Replace version in readme if needed.
            if (configuration.canUpdateReadmeWithVersion()) {
                FileUtils.replaceAllSemVerInFile(configuration.getVersion(), configuration.getReadmePath());
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
}
