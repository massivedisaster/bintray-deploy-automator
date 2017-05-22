package com.massivedisaster.bintraydeployautomator;

import com.massivedisaster.bintraydeployautomator.model.Configuration;
import com.massivedisaster.bintraydeployautomator.utils.FileUtils;
import com.massivedisaster.bintraydeployautomator.utils.GradleUtils;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import java.io.File;

public class BintrayDeployAutomator extends FileUtils {

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

            // Bintrayupload.
            bintrayUpload(gradleConnection, configuration);

            // Replace version if needed.
            if (configuration.UpdateReadmeVersion()) {
                FileUtils.replaceSemVerInFile(configuration.getVersion(), configuration.getReadmePath());
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
        GradleUtils.runGradle(gradleConnection, configuration.getRebuildTasks(), configuration.getRebuildArguments());
    }

    private static void bintrayUpload(ProjectConnection gradleConnection, Configuration configuration) {
        for (String task: configuration.getBintrayTasks()) {
            GradleUtils.runGradle(gradleConnection, new String[] {task}, configuration.getBintrayArguments());
        }
    }
}
