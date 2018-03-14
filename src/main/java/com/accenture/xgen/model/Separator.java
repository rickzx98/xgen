package com.accenture.xgen.model;

public class Separator {

    private String separator = "\\u25B2";

    public Separator(char unicode) {
        this.separator = String.valueOf(unicode);
    }

    public Separator(String separator) {
        if (separator != null) {
            this.separator = separator.replaceAll("\\\\", "\\\\\\\\");
        /*if (separator != null) {
            this.separator = StringEscapeUtils.escapeJava(separator);
        }*/
            this.separator = separator;
        }
    }

    @Override
    public String toString() {
        return separator;
    }
}
