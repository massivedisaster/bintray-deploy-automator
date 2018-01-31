package com.massivedisaster.bintraydeployautomator.model;

import com.google.gson.Gson;
import com.massivedisaster.bintraydeployautomator.utils.ArrayUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.massivedisaster.bintraydeployautomator.utils.FileUtils.readFileAsString;

/**
 * Model representation of configuration.
 */
public class Configuration {
    private String basePath = "./";
    private String version = "0.0.0-deploy_automator";
    private List<String> modules;
    private List<String> extraTasks;
    private String bintrayUsername;
    private String bintrayKey;
    private String[] bintrayTasks;
    private String[] buildTasks;
    private boolean isVerbose;

    /**
     * Parses the configuration file.
     *
     * @param path path to the configuration file.
     * @return returns an instance of {@link Configuration}.
     * @throws IOException if the file don't exist.
     */
    public static Configuration parseConfiguration(String path) throws IOException {
        return new Gson().fromJson(readFileAsString(path), Configuration.class);
    }

    /**
     * Gets the base path of execution.
     *
     * @return the base path of execution.
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * Gets the version.
     *
     * @return the version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the modules.
     *
     * @return the modules.
     */
    public List<String> getModules() {
        return modules;
    }

    public void setBintrayUsername(String bintrayUsername) {
        this.bintrayUsername = bintrayUsername;
    }

    public void setBintrayKey(String bintrayKey) {
        this.bintrayKey = bintrayKey;
    }

    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
    }

    /**
     * Gets the arguments.
     *
     * @return list of arguments.
     */
    public String[] getArguments() {
        List<String> args = new ArrayList<>();
        args.add(String.format("-PbintrayUser=%s", bintrayUsername));
        args.add(String.format("-PbintrayKey=%s", bintrayKey));
        args.add(String.format("-PlibraryVersionName=%s", version));
        args.add("-PdryRun=false");
        args.add("-Pskippasswordprompts");

        if (isVerbose) {
            args.add("--stacktrace");
            args.add("--info");
            args.add("--debug");
        }

        return args.toArray(new String[args.size()]);
    }

    /**
     * Gets task to run.
     *
     * @return tasks to run.
     */
    public String[] getTasks() {
        return ArrayUtils.addAll(getBuildTasks(), getBintrayTasks());
    }

    /**
     * get extra tasks from json.
     *
     * @return extra tasks to run.
     */
    public String[] getExtraTasks() {
        return extraTasks.toArray(new String[0]);
    }

    private String[] getBuildTasks() {
        if (modules == null) {
            return new String[]{"clean", "build"};
        }

        if (buildTasks == null) {
            int size = modules.size();
            buildTasks = new String[size * 2];
            for (int i = 0; i < size; i = i + 2) {
                buildTasks[i] = modules.get(i) + ":clean";
                buildTasks[i + 1] = modules.get(i) + ":build";
            }
        }

        return buildTasks;
    }

    /**
     * Get the bintray tasks from modules.
     *
     * @return list of bintray upload tasks.
     */
    private String[] getBintrayTasks() {
        if (modules == null) {
            return new String[]{"bintrayUpload"};
        }

        if (bintrayTasks == null) {
            int size = modules.size();
            bintrayTasks = new String[size];
            for (int i = 0; i < size; i++) {
                bintrayTasks[i] = modules.get(i) + ":bintrayUpload";
            }
        }

        return bintrayTasks;
    }

    /**
     * Tells if exists extra task to be executed.
     *
     * @return true if readme needs to be updated, else false.
     */
    public boolean canRunExtraTasks() {
        return extraTasks != null;
    }
}
