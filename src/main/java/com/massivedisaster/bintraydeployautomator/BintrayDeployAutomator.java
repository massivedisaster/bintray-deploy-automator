package com.massivedisaster.bintraydeployautomator;

import com.massivedisaster.bintraydeployautomator.model.Configuration;
import com.massivedisaster.bintraydeployautomator.utils.Utils;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import java.io.File;

public class BintrayDeployAutomator extends Utils {

    public static void main(String args[]) {

        ProjectConnection gradleConnection = null;
        try {

            // Get configuration from .json.
            Configuration configuration = Configuration.parseConfiguration("configuration.json");

            gradleConnection = GradleConnector.newConnector()
                    .forProjectDirectory(new File(configuration.getBasePath()))
                    .connect();

            // Clean and build all projects.
            rebuild(gradleConnection, configuration);

            // Bintrayupload
            bintrayUpload(gradleConnection, configuration);

            // Replace version if needed.
            if (configuration.UpdateReadmeVersion()) {
                Utils.replaceSemVerInFile(configuration.getVersion(), configuration.getReadmePath());
            }

        } catch (Exception e) {
            System.out.println("BintrayDeployAutomator Error: " + e.toString());
        } finally {
            if (gradleConnection != null) {
                gradleConnection.close();
            }
        }
    }

    private static void rebuild(ProjectConnection gradleConnection, Configuration configuration) {
        gradleConnection.newBuild()
                .forTasks("clean", "build")
                .withArguments(configuration.getBuildArguments())
                .setStandardOutput(System.out)
                .setStandardError(System.err)
                .run();
    }

    private static void bintrayUpload(ProjectConnection gradleConnection, Configuration configuration) {
        gradleConnection.newBuild()
                .forTasks(configuration.getBintrayTasks())
                .withArguments(configuration.getBintrayArguments())
                .setStandardOutput(System.out)
                .setStandardError(System.err)
                .run();
    }
}
