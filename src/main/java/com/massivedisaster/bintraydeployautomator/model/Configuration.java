package com.massivedisaster.bintraydeployautomator.model;

import com.google.gson.Gson;
import com.massivedisaster.bintraydeployautomator.utils.ArrayUtils;
import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

import java.io.IOException;
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

    /**
     * Gets the arguments.
     *
     * @return list of arguments.
     */
    public String[] getArguments() {
        return new String[]{
                String.format("-PbintrayUser=%s", bintrayUsername),
                String.format("-PbintrayKey=%s", bintrayKey),
                String.format("-PlibraryVersionName=%s", version),
                "-PdryRun=false",
                "-Pskippasswordprompts"
        };
    }

    /**
     * Gets task to run.
     *
     * @return tasks to run.
     */
    public String[] getTasks() {
        return ArrayUtils.addAll(new String[]{"clean", "build"}, getBintrayTasks());
    }

    /**
     * get extra tasks from json.
     *
     * @return extra tasks to run.
     */
    public String[] getExtraTasks() {
        return extraTasks.toArray(new String[0]);
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
     * Tells if readme needs to be updated with version.
     *
     * @return true if readme needs to be updated, else false.
     */
    public boolean canRunExtraTasks() {
        return extraTasks != null;
    }
}
