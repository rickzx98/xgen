package com.accenture.xgen.generator;

import com.accenture.xgen.model.Structure;
import com.accenture.xgen.model.StructureData;

import java.util.Map;

public class XMLStructure extends Structure {
    protected String construct(Map<String, String> constructedValue, StructureData structureData) throws StructureException {
        for (String field : constructedValue.keySet()) {
            StructureData xsdDoc = structureData.findByValue(String.format("${%s}", field));
            if (xsdDoc == null) {
                throw new IncompatibleSchemaException(String.format("Cannot find %s.", field));
            }
            xsdDoc.setValue(constructedValue.get(field));
        }
        return structureData.toFormattedString();
    }

    public static class IncompatibleSchemaException extends StructureException {
        private IncompatibleSchemaException(String msg) {
            super("IncompatibleSchema", msg);
        }
    }

}
