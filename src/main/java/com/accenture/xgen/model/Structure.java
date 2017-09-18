package com.accenture.xgen.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Structure {
    private Map<String, String> constructedValue;

    protected Structure() {
        constructedValue = new HashMap<String, String>();
    }

    public void put(String field, String value) {
        constructedValue.put(field, value);
    }

    public String build(StructureData structureData) {
        return construct(constructedValue, structureData);
    }

    protected abstract String construct(Map<String, String> constructedValue, StructureData structureData);


}

