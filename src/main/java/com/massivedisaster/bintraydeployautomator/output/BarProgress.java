package com.massivedisaster.bintraydeployautomator.output;

import me.tongfei.progressbar.ProgressBar;

public class BarProgress implements IProgress {

    private ProgressBar mPb = null;

    @Override
    public void setup(String name, int steps) {
        mPb = new ProgressBar(name, steps);
    }

    private void validateSetupCalled() {
        if (mPb == null) {
            throw new ExceptionInInitializerError("It's necessary to call BarProgress.setup first");
        }
    }

    @Override
    public void logMessage(String message) {
        validateSetupCalled();
        mPb.setExtraMessage(message);
    }

    @Override
    public void start() {
        validateSetupCalled();
        mPb.start();
    }

    @Override
    public void step() {
        validateSetupCalled();
        mPb.step();
    }

    @Override
    public void stop() {
        validateSetupCalled();
        mPb.stop();
    }
}
