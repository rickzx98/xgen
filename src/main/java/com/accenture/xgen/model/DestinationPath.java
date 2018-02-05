package com.accenture.xgen.model;

import java.io.File;

public class DestinationPath {
    private String value;

    public DestinationPath(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public File toFile() {
        File file = new File(value);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }
}
