package com.accenture.xgen.model.xsl;

import com.accenture.xgen.model.StructureData;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class XSLData implements StructureData {

    private static final String VAR_REG = "\\$\\{.*}";

    public static final String TYPE_RESERVED = "XSLData.type.RESERVED";
    public static final String TYPE_ELEMENT = "XSLData.type.ELEMENT";
    public static final String TYPE_ATTRIBUTE = "XSLData.type.ATTRIBUTE";
    private String type;
    protected String value;
    protected String field;
    protected String prefix;
    protected String namespaceUri;
    protected String id;
    protected StructureData parent;
    protected Map<String, String> constructedValue;

    protected XSLData(String field, String value, String prefix, String namespaceUri, StructureData parent, String type) {
        this.field = field;
        this.value = value;
        this.prefix = prefix;
        this.namespaceUri = namespaceUri;
        this.type = type;
        this.parent = parent;
        this.id = new UID().toString();
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XSLData xslData = (XSLData) o;

        return id != null ? id.equals(xslData.id) : xslData.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public StructureData getParent() {
        return parent;
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
    public String getField() {
        return field;
    }

    public void setConstructedValue(Map<String, String> constructedValue) {
        this.constructedValue = constructedValue;
    }

    protected String getValueFromConstructedMap() {
        String newValue = value;
        if (constructedValue != null) {
            if (value != null && value.matches(VAR_REG)) {
                String fieldVar = value.replace("$", "").replace("{", "").replace("}", "");
                newValue = constructedValue.get(fieldVar);
            }
        }
        if (newValue == null) {
            newValue = "";
        }
        return newValue;
    }

    @Override
    public void clear() {
        field = null;
        value = null;
        constructedValue = null;
        parent = null;
        prefix = null;
        namespaceUri = null;
    }
}
