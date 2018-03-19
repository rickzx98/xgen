package com.accenture.xgen.model;


import org.apache.commons.lang.StringEscapeUtils;

public class Separator {

    private String separator = "\\u25B2";

    public Separator(String separator, String unicode) {
        if (unicode.equalsIgnoreCase("y")) {
            this.separator = StringEscapeUtils.escapeJava(separator);
        } else {
            this.separator = separator;
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
