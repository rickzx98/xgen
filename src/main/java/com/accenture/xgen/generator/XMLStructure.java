package com.accenture.xgen.generator;

import com.accenture.xgen.model.Structure;
import com.accenture.xgen.model.StructureData;
import com.accenture.xgen.model.XSDData;

import java.util.Map;

public class XMLStructure extends Structure {
    protected String construct(Map<String, String> constructedValue, StructureData structureData) throws StructureException {
        ((XSDData) structureData).clearNewAttributes();
        for (String field : constructedValue.keySet()) {
            String keyword = String.format("${%s}", field);
            ((XSDData) structureData).addAttributeValue(keyword, constructedValue.get(field));
            StructureData xsdDoc = structureData.findByValue(keyword);
            if (xsdDoc == null) {
                throw new IncompatibleSchemaException(String.format("Cannot find %s.", field));
            }
            xsdDoc.setValue(constructedValue.get(field));
        }
        return ((XSDData) structureData).findByFieldContains("body").toFormattedString();
    }

    public static class IncompatibleSchemaException extends StructureException {
        private IncompatibleSchemaException(String msg) {
            super("IncompatibleSchema", msg);
        }
    }

}
