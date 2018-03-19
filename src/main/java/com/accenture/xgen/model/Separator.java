package com.accenture.xgen.model;

import org.apache.commons.text.StringEscapeUtils;

public class Separator {

    private String separator = "\\u25B2";

    public Separator(String separator, String unicode) {
        this.separator = StringEscapeUtils.escapeJava(separator);
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
