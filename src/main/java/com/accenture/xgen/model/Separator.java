package com.accenture.xgen.model;

public class Separator {

    private String separator = "\\u25B2";

    public Separator(String separator, String unicode) {
        this(separator, unicode.equalsIgnoreCase("y"));
    }

    public Separator(String separator, boolean unicode) {
        if (unicode) {
            this.separator = String.valueOf(separator.toCharArray()[0]);
        }
    }

    public Separator(char unicode) {
        this.separator = String.valueOf(unicode);
    }

    public Separator(String separator) {
        this.separator = separator;
    }

    @Override
    public String toString() {
        System.out.println("Separator: " + this.separator);
        return separator;
    }
}
