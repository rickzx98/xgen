package com.accenture.xgen.model;

import org.apache.commons.text.StringEscapeUtils;

public class Separator {

    private String separator = ";";

    public Separator(String separator) {
        //this.separator = separator.replaceAll("\\\\","\\\\\\\\");
        //if (separator != null) {
        //  this.separator = StringEscapeUtils.escapeJava(separator);
        //}
      this.separator = separator;
    }

    @Override
    public String toString() {
        return separator;
    }
}
