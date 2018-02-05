package com.accenture.xgen.model;
import java.util.Map;

public interface DocumentData {
    public String construct(Structure structure, StructureData structureData, Map<String, String> placeHolderDefaultMap) throws Structure.StructureException;
    public long recordNumber();
}
