package com.massivedisaster.bintraydeployautomator.output;

public interface IProgress {

    void setup(String name, int steps);

    void logMessage(String message);

    void start();

    void step();

    void stop();

}
