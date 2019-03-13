package org.ues21.tests;

public class Profiler {

    public static void profileTiming(String tag, Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        System.out.println(String.format("Time (s) to %s: %s", tag, (System.currentTimeMillis() - startTime) / 1000F));
    }
}
