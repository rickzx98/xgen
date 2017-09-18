package com.accenture.xgen.model;

public interface StructureData {
    String getField();

    String getValue();

    String getParent();

    StructureData findByValue(String value);

    boolean equals(Object o);

    int hashCode();
}
