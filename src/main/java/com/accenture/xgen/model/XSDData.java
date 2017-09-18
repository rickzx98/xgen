package com.accenture.xgen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XSDData implements StructureData {
    private String field;
    private String value;
    private String parent;
    private List<XSDData> children;
    private Map<String, XSDData> cache;

    private XSDData() {
        cache = new HashMap<String, XSDData>();
    }

    public XSDData(String field, String value) {
        this();
        this.field = field;
        this.value = value;
    }

    public void addChild(XSDData xsdData) {
        if (children == null) {
            children = new ArrayList<XSDData>();
        }
        children.add(xsdData);
        xsdData.setParent(field);
    }

    private void setParent(String parent) {
        this.parent = parent;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public String getParent() {
        return parent;
    }

    public XSDData findByValue(String value) {
        XSDData foundXSD = null;
        if (cache.containsKey(value)) {
            foundXSD = cache.get(value);
        } else if (hasValueOf(value)) {
            foundXSD = this;
        } else if (children != null && !children.isEmpty()) {
            foundXSD = findValueInChildren(value);
        }
        if (foundXSD != null) {
            cache.put(value, foundXSD);
        }
        return foundXSD;
    }

    private XSDData findValueInChildren(String value) {
        XSDData foundXSDData = null;
        if (children != null) {
            for (XSDData xsdData : children) {
                if (foundXSDData == null) {
                    foundXSDData = xsdData.findByValue(value);
                } else {
                    break;
                }
            }
        }
        return foundXSDData;
    }

    public boolean hasValueOf(String value) {
        if (this.value != null) {
            return this.value.equals(value);
        }
        return Boolean.FALSE;
    }

    public boolean hasFieldOf(String field) {
        return this.field.equals(field);
    }

    @Override
    public String toString() {
        return "field:\t" +
                field +
                "\n" +
                "value:\t" +
                value +
                "\n" +
                "parent:\t"
                + parent + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XSDData xsdData = (XSDData) o;

        if (field != null ? !field.equals(xsdData.field) : xsdData.field != null) return false;
        if (value != null ? !value.equals(xsdData.value) : xsdData.value != null) return false;
        return parent != null ? parent.equals(xsdData.parent) : xsdData.parent == null;
    }

    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }
}
