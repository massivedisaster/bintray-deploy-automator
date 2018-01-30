package com.massivedisaster.bintraydeployautomator.utils;

import com.massivedisaster.bintraydeployautomator.model.Arguments;
import org.apache.commons.cli.*;

public class CommandLineUtils {

    public static Arguments commandLineArgs(String[] args) {
        Options options = new Options();

        Option user = new Option("u", "user", true, "Bintray Username (Required)");
        user.setRequired(true);
        options.addOption(user);

        Option key = new Option("k", "key", true, "Bintray Key (Required)");
        key.setRequired(true);
        options.addOption(key);

        Option verbose = new Option("v", "verbose", false, "Show more logs");
        verbose.setRequired(false);
        options.addOption(verbose);

        Option output = new Option("o", "output", true, "Log output file");
        output.setRequired(false);
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Java -jar BintrayDeployAutomator-" + Version.VERSION + ".jar", options);

            System.exit(1);
            return null;
        }

        String userArg = cmd.getOptionValue("user");
        String keyArg = cmd.getOptionValue("key");
        boolean isVerbose = cmd.hasOption("verbose");
        String outputArg = cmd.getOptionValue("output");

        return new Arguments(userArg, keyArg, isVerbose, outputArg);
    }

}
