package com.accenture.xgen.model.xsl;

import com.accenture.xgen.model.StructureData;

public class XSLAttributeData extends XSLData {
    public XSLAttributeData(String field, String value, String prefix, String namespaceUri,
                            StructureData structureData) {
        super(field, value, prefix, namespaceUri, structureData, XSLData.TYPE_ATTRIBUTE);
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public StructureData getParent() {
        return parent;
    }

    @Override
    public StructureData findByValue(String value) {
        return null;
    }

    @Override
    public StructureData findByField(String field) {
        return null;
    }

    @Override
    public String toFormattedString() {
        return String.format("%s=\"%s\"", field, getValueFromConstructedMap());
    }
}
