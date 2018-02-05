package com.massivedisaster.bintraydeployautomator;

import com.massivedisaster.bintraydeployautomator.model.Arguments;
import com.massivedisaster.bintraydeployautomator.model.Configuration;
import com.massivedisaster.bintraydeployautomator.output.BarProgress;
import com.massivedisaster.bintraydeployautomator.output.ConsoleProgress;
import com.massivedisaster.bintraydeployautomator.output.ProgressManager;
import com.massivedisaster.bintraydeployautomator.utils.CommandLineUtils;
import com.massivedisaster.bintraydeployautomator.utils.Config;
import com.massivedisaster.bintraydeployautomator.utils.ConsoleUtils;
import com.massivedisaster.bintraydeployautomator.utils.GradleUtils;
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
        ProgressManager progressManager = null;
        String outputFile = null;
        try {
            Arguments auth = CommandLineUtils.commandLineArgs(args);

            // Get configuration from .json.
            Configuration configuration = Configuration.parseConfiguration("configuration.json");

            List<String> modules = configuration.getModules();
            boolean hasModules = modules != null;
            int steps = hasModules ? modules.size() * 2 : 1;

            configuration.setBintrayUsername(auth.getUser());
            configuration.setBintrayKey(auth.getKey());
            configuration.setVerbose(auth.isVerbose());
            outputFile = auth.getLogFile();
            if (outputFile != null) {
                File output = new File(outputFile);
                if (output.exists()) {
                    new FileWriter(outputFile).close();
                }
                progressManager = new ProgressManager(new BarProgress(), Config.NAME, steps);
            } else {
                progressManager = new ProgressManager(new ConsoleProgress(), Config.NAME, steps);
            }

            gradleConnection = GradleConnector.newConnector().forProjectDirectory(new File(configuration.getBasePath()))
                    .connect();

            ConsoleUtils.DrawInConsoleBox("Start process");

            if (hasModules) {
                progressManager.start();
                //clean and build
                for (String module : modules) {
                    progressManager.logMessage("Clean and building module " + module + "...");
                    GradleUtils.runGradle(gradleConnection, outputFile, new String[]{module + ":clean", module + ":build"},
                            configuration.getArguments());
                    progressManager.step();
                }

                //clean and build
                for (String module : modules) {
                    progressManager.logMessage("Uploading to bintray module " + module + "...");
                    GradleUtils.runGradle(gradleConnection, outputFile, new String[]{module + ":bintrayUpload"}, configuration.getArguments());
                    progressManager.step();
                }
                progressManager.stop();
            } else {
                GradleUtils.runGradle(gradleConnection, outputFile, configuration.getTasks(), configuration.getArguments());
            }

            if (configuration.canRunExtraTasks()) {
                ConsoleUtils.DrawInConsoleBox("Executing extra tasks");
                GradleUtils.runGradle(gradleConnection, auth.getLogFile(), configuration.getExtraTasks(), configuration.getArguments());
            }

            ConsoleUtils.DrawInConsoleBox("End process");

        } catch (Exception e) {
            progressManager.stop();
            if (outputFile == null) {
                progressManager.logMessage("Automator Error: " + e.toString());
            } else {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, true)));
                out.append("Automator Error: ");
                out.append(e.toString());
                out.append("\n");
                out.close();
            }
        } finally {
            if (gradleConnection != null) {
                gradleConnection.close();
            }
        }
    }
}
