package com.massivedisaster.bintraydeployautomator.output;

public class ProgressManager {

    private IProgress mProgress;

    public ProgressManager(IProgress progress, String name, int steps) {
        mProgress = progress;
        mProgress.setup(name, steps);
    }

    public void logMessage(String message) {
        mProgress.logMessage(message);
    }

    public void start() {
        mProgress.start();
    }

    public void step() {
        mProgress.step();
    }

    public void stop() {
        mProgress.stop();
    }
}
