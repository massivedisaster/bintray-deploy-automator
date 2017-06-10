package com.massivedisaster.bintraydeployautomator.model;

import com.google.gson.Gson;

import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;

import static com.massivedisaster.bintraydeployautomator.utils.FileUtils.readFileAsString;

/**
 * Model representation of configuration.
 */
public class Configuration {
    private String basePath = "./";
    private String readmePath;
    private String version = "0.0.0-deploy_automator";
    private List<String> modules;
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
     * Gets de readme path.
     *
     * @return the readme path.
     */
    public String getReadmePath() {
        return readmePath;
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

    /**
     * Gets bintray username.
     *
     * @return bintray username.
     */
    public String getBintrayUsername() {
        return bintrayUsername;
    }

    /**
     * Gets bintray key.
     *
     * @return bintray key.
     */
    public String getBintrayKey() {
        return bintrayKey;
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
        return new String[]{"clean", "build", "bintrayUpload"};
    }

    /**
     * Tells if readme needs to be updated with version.
     *
     * @return true if readme needs to be updated, else false.
     */
    public boolean canUpdateReadmeWithVersion() {
        return !StringUtils.isEmpty(readmePath);
    }
}
