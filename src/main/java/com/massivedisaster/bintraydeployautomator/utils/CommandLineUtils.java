package com.massivedisaster.bintraydeployautomator.utils;

import javafx.util.Pair;
import org.apache.commons.cli.*;

public class CommandLineUtils {

    public static Pair<String, String> commandLineArgs(String[] args) {
        Options options = new Options();

        Option input = new Option("u", "user", true, "Bintray Username");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("k", "key", true, "Bintray Key");
        output.setRequired(true);
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Java -jar BintrayDeployAutomator-0.0.2.jar", options);

            System.exit(1);
            return null;
        }

        String user = cmd.getOptionValue("user");
        String key = cmd.getOptionValue("key");

        return new Pair<>(user, key);
    }

}
