package com.massivedisaster.bintraydeployautomator.model;

public class Arguments {

    private String mUser;
    private String mKey;
    private boolean isVerbose;
    private String mLogFile;

    public Arguments(String user, String key, boolean verbose, String logFile) {
        mUser = user;
        mKey = key;
        isVerbose = verbose;
        mLogFile = logFile;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        this.mUser = user;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public boolean isVerbose() {
        return isVerbose;
    }

    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
    }

    public String getLogFile() {
        return mLogFile;
    }

    public void setLogFile(String logFile) {
        this.mLogFile = logFile;
    }

}
