package com.massivedisaster.bintraydeployautomator;

import com.massivedisaster.bintraydeployautomator.model.Arguments;
import com.massivedisaster.bintraydeployautomator.model.Configuration;
import com.massivedisaster.bintraydeployautomator.utils.CommandLineUtils;
import com.massivedisaster.bintraydeployautomator.utils.ConsoleUtils;
import com.massivedisaster.bintraydeployautomator.utils.GradleUtils;
import me.tongfei.progressbar.ProgressBar;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import java.io.*;
import java.util.List;

/**
 * Bintray deploy automator.
 */
public class Automator {

    /**
     * Main method.
     *
     * @param args command line arguments.
     */
    public static void main(String args[]) throws IOException {

        ProjectConnection gradleConnection = null;
        ProgressBar pb = null;
        String outputFile = null;
        try {
            Arguments auth = CommandLineUtils.commandLineArgs(args);

            // Get configuration from .json.
            Configuration configuration = Configuration.parseConfiguration("configuration.json");

            List<String> modules = configuration.getModules();

            configuration.setBintrayUsername(auth.getUser());
            configuration.setBintrayKey(auth.getKey());
            configuration.setVerbose(auth.isVerbose());
            outputFile = auth.getLogFile();
            if (outputFile != null) {
                File output = new File(outputFile);
                if (output.exists()) {
                    output.delete();
                }
                pb = new ProgressBar("BintrayDeployAutomator-" + com.massivedisaster.bintraydeployautomator.utils.Version.VERSION, modules.size() * 2);
            }

            gradleConnection = GradleConnector.newConnector()
                    .forProjectDirectory(new File(configuration.getBasePath()))
                    .connect();

            ConsoleUtils.DrawInConsoleBox("Start process");

            if (pb != null) pb.start();
            //clean and build
            for (String module : modules) {
                if (pb != null) pb.setExtraMessage("Clean and building module " + module + "...");
                GradleUtils.runGradle(gradleConnection, outputFile, new String[]{module + ":clean", module + ":build"}, configuration.getArguments());
                if (pb != null) pb.step();
            }

            //clean and build
            for (String module : modules) {
                if (pb != null) pb.setExtraMessage("Uploading to bintray module " + module + "...");
                GradleUtils.runGradle(gradleConnection, outputFile, new String[]{module + ":bintrayUpload"}, configuration.getArguments());
                if (pb != null) pb.step();
            }
            if (pb != null) pb.stop();

            if (configuration.canRunExtraTasks()) {
                ConsoleUtils.DrawInConsoleBox("Executing extra tasks");
                runExtraTasks(auth.getLogFile(), gradleConnection, configuration);
            }

            ConsoleUtils.DrawInConsoleBox("End process");

        } catch (Exception e) {
            if (pb != null) {
                pb.stop();
            }
            if (outputFile == null) {
                System.out.println("Automator Error: " + e.toString());
            } else {
                Writer output = new BufferedWriter(new FileWriter(outputFile));  //clears file every time
                output.append("Automator Error: " + e.toString());
                output.close();
            }
        } finally {
            if (gradleConnection != null) {
                gradleConnection.close();
            }
        }
    }

    /**
     * Run extra tasks from project.
     *
     * @param gradleConnection the gradle connection.
     * @param configuration    the configuration model.
     */
    private static void runExtraTasks(String output, ProjectConnection gradleConnection, Configuration configuration) {
        GradleUtils.runGradle(gradleConnection, output, configuration.getExtraTasks(), configuration.getArguments());
    }
}
