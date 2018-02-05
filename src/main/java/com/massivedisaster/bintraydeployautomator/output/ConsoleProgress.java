package com.massivedisaster.bintraydeployautomator.output;

import com.massivedisaster.bintraydeployautomator.utils.ConsoleUtils;

public class ConsoleProgress implements IProgress {

    private long mCurrent = 0;
    private long mMax = 0;
    private boolean mSetup = false;

    @Override
    public void setup(String name, int steps) {
        mMax = steps;
        System.out.println(name);
        mSetup = true;
    }

    private void validateSetupCalled() {
        if (!mSetup) {
            throw new ExceptionInInitializerError("It's necessary to call BarProgress.setup first");
        }
    }

    @Override
    public void logMessage(String message) {
        validateSetupCalled();
        ConsoleUtils.DrawInConsoleBox(message);
    }

    @Override
    public void start() {
    }

    @Override
    public void step() {
        validateSetupCalled();
        mCurrent++;
        if (mCurrent > mMax) mMax = mCurrent;
        ConsoleUtils.DrawInConsoleBox("Step: " + mCurrent + "/" + mMax);
    }

    @Override
    public void stop() {
    }
}
