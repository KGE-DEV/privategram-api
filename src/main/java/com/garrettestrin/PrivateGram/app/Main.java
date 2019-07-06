package com.garrettestrin.PrivateGram.app;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationPropertyLoader.loadProperties();

        if (args == null || args.length == 0) {
            args = new String[] { "server", "app.yml"};
        }

        new PrivateGramApplication().run(args);
    }
}
