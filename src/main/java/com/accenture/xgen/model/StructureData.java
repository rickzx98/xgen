package com.accenture.xgen.model;

public interface StructureData {
    String getField();

    String getValue();

    void setValue(String value);

    StructureData getParent();

    StructureData findByValue(String value);

    StructureData findByField(String field);

    boolean equals(Object o);

    int hashCode();

    String toFormattedString();

    void clear();
}
