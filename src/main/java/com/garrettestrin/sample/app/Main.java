package com.garrettestrin.sample.app;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationPropertyLoader.loadProperties();

        if (args == null || args.length == 0) {
            args = new String[] { "server", "sample.yml" };
        }

        new SampleApplication().run(args);
    }
}
