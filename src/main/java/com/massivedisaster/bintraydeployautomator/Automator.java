package com.massivedisaster.bintraydeployautomator;

import com.massivedisaster.bintraydeployautomator.model.Arguments;
import com.massivedisaster.bintraydeployautomator.model.Configuration;
import com.massivedisaster.bintraydeployautomator.utils.CommandLineUtils;
import com.massivedisaster.bintraydeployautomator.utils.Config;
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
                    new FileWriter(outputFile).close();
                }
                pb = new ProgressBar(Config.NAME, modules.size() * 2);
            }

            gradleConnection = GradleConnector.newConnector().forProjectDirectory(new File(configuration.getBasePath()))
                    .connect();

            ConsoleUtils.DrawInConsoleBox("Start process");

            if (modules != null) {
                if (pb != null) pb.start();
                //clean and build
                for (String module : modules) {
                    if (pb != null) pb.setExtraMessage("Clean and building module " + module + "...");
                    GradleUtils.runGradle(gradleConnection, outputFile, new String[]{module + ":clean", module + ":build"},
                            configuration.getArguments());
                    if (pb != null) pb.step();
                }

                //clean and build
                for (String module : modules) {
                    if (pb != null) pb.setExtraMessage("Uploading to bintray module " + module + "...");
                    GradleUtils.runGradle(gradleConnection, outputFile, new String[]{module + ":bintrayUpload"}, configuration.getArguments());
                    if (pb != null) pb.step();
                }
                if (pb != null) pb.stop();
            } else {
                GradleUtils.runGradle(gradleConnection, outputFile, configuration.getTasks(), configuration.getArguments());
            }

            if (configuration.canRunExtraTasks()) {
                ConsoleUtils.DrawInConsoleBox("Executing extra tasks");
                GradleUtils.runGradle(gradleConnection, auth.getLogFile(), configuration.getExtraTasks(), configuration.getArguments());
            }

            ConsoleUtils.DrawInConsoleBox("End process");

        } catch (Exception e) {
            if (pb != null) {
                pb.stop();
            }
            if (outputFile == null) {
                System.out.println("Automator Error: " + e.toString());
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
