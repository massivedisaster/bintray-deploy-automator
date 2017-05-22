package com.massivedisaster.bintraydeployautomator.model;

import com.google.gson.Gson;
import org.gradle.internal.impldep.org.apache.commons.lang.NullArgumentException;
import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;

import static com.massivedisaster.bintraydeployautomator.utils.FileUtils.readFile;

public class Configuration {
    private String basePath = "./";
    private String readmePath = "./";
    private String version = "0.0.0";
    private List<String> modules;
    private String bintrayUsername;
    private String bintrayKey;
    private String[] bintrayTasks;

    public static Configuration parseConfiguration(String path) throws IOException {
        return new Gson().fromJson(readFile(path), Configuration.class);
    }

    public String getBasePath() {
        return basePath;
    }

    public String getReadmePath() {
        return readmePath;
    }

    public String getVersion() {
        return version;
    }

    public List<String> getModules() {
        return modules;
    }

    public String getBintrayUsername() {
        return bintrayUsername;
    }

    public String getBintrayKey() {
        return bintrayKey;
    }

    public String[] getBintrayTasks() {
        if(modules == null){
            throw new NullArgumentException("modules can't be null");
        }

        if(bintrayTasks == null){
            int size = modules.size();
            bintrayTasks = new String[size];
            for (int i = 0; i <size ; i++) {
                bintrayTasks[i] = modules.get(i)+":bintrayUpload";
            }
        }
        return bintrayTasks;
    }

    public String[] getBintrayArguments() {
        return new String[] {
                String.format("-PbintrayUser=\"%s\"", bintrayUsername),
                String.format("-PbintrayKey=\"%s\"", bintrayKey),
                "-PdryRun=false",
                "-Pskippasswordprompts"          
        };
    }

    public String[] getRebuildTasks() {
        return new String[] {"clean", "build"};
    }

    public String getRebuildArguments() {
        return String.format("-PlibraryVersionName=%s", version);
    }

    public boolean UpdateReadmeVersion() {
        return !StringUtils.isEmpty(readmePath) && !StringUtils.isEmpty(version);
    }
}
