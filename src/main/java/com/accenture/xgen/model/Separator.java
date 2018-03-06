package com.accenture.xgen.model;

public class Separator {

    private String separator = ";";

    public Separator(String separator) {
        this.separator = separator.replaceAll("\\\\","\\\\\\\\");
    }

    @Override
    public String toString() {
        return separator;
    }
}
