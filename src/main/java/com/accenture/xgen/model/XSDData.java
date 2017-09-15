package com.accenture.xgen.model;

import java.util.ArrayList;
import java.util.List;

public class XSDData {
    private String field;
    private String value;
    private String parent;
    private List<XSDData> children;

    public XSDData(String field, String value) {
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

    public List<XSDData> childrenOf(String parent) {
        List<XSDData> childrenOf = null;
        for (XSDData xsdData : children) {
            if (childrenOf == null) {
                if (xsdData.field != null && xsdData.field.equals(parent)) {
                    childrenOf = xsdData.children;
                } else {
                    childrenOf = xsdData.childrenOf(parent);
                }
            }
        }
        return childrenOf;
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
}
