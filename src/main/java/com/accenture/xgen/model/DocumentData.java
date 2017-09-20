package com.accenture.xgen.model;

public interface DocumentData {
    public String construct(Structure structure, StructureData structureData) throws Structure.StructureException;
    public long recordNumber();
}
