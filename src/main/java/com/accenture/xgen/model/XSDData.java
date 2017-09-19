package com.accenture.xgen.model;

import com.jamesmurty.utils.XMLBuilder2;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.*;

public class XSDData implements StructureData {
    private String field;
    private String value;
    private XSDData parent;
    private List<XSDData> children;
    private Map<String, XSDData> cache;
    private Map<String, XSDData> fieldCache;
    private Map<String, String> attributes;
    private String envelopNamespace;

    private XSDData() {
        cache = new HashMap<String, XSDData>();
        fieldCache = new HashMap<String, XSDData>();
    }

    public XSDData(String field, String value, NamedNodeMap attr) {
        this();
        this.field = field;
        this.value = value;
        if (attr != null) {
            this.attributes = new HashMap<String, String>();
            for (int i = 0; i < attr.getLength(); i++) {
                Node node = attr.item(i);
                if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                    this.attributes.put(attr.item(i).getNodeName(), attr.item(i).getTextContent());
                }
            }
        }
    }

    public void addChild(XSDData xsdData) {
        if (children == null) {
            children = new ArrayList<XSDData>();
        }
        children.add(xsdData);
        xsdData.setParent(this);
    }

    private void setParent(XSDData parent) {
        this.parent = parent;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public XSDData getParent() {
        return parent;
    }

    public void setValue(String value) {
        this.value = value;
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

    public XSDData findByField(String field) {
        XSDData foundXSD = null;
        if (fieldCache.containsKey(field)) {
            foundXSD = cache.get(field);
        } else if (hasFieldOf(field)) {
            foundXSD = this;
        } else if (children != null && !children.isEmpty()) {
            foundXSD = findFieldInChildren(field);
        }
        if (foundXSD != null) {
            fieldCache.put(field, foundXSD);
        }
        return foundXSD;
    }

    private XSDData findFieldInChildren(String field) {
        XSDData foundXSDData = null;
        if (children != null) {
            for (XSDData xsdData : children) {
                if (foundXSDData == null) {
                    foundXSDData = xsdData.findByField(field);
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

    private boolean isEnvelope() {
        boolean hasEnvelope = Boolean.FALSE;
        if (attributes != null) {
            for (String value : attributes.values()) {
                hasEnvelope = value.contains("envelope");
                if (hasEnvelope) {
                    break;
                }
            }
        }
        return hasEnvelope;
    }

    private String getEnvelopeNamespace() {
        String namespace = null;
        if (envelopNamespace == null) {
            if (attributes != null) {
                List<String> values = new ArrayList<String>(attributes.values());
                for (int i = 0; i < values.size(); i++) {
                    if (values.get(i).contains("envelope")) {
                        namespace = new ArrayList<String>(attributes.keySet()).get(i).split("\\:")[1];
                        break;
                    }
                }
            }
            envelopNamespace = namespace + ":Envelope";
        }
        return envelopNamespace;
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

    public String toFormattedString() {
        XMLBuilder2 xmlBuilder2 = XMLBuilder2.create("xsd-data");
        xmlBuilder(xmlBuilder2, this, this.children);
        return xmlBuilder2.asString();
    }

    private void xmlBuilder(XMLBuilder2 xmlBuilder2, XSDData parent, List<XSDData> children) {
        XMLBuilder2 element = null;
        if (!parent.field.equals("document")) {
            if (parent.isEnvelope()) {
                element = xmlBuilder2.e(parent.getEnvelopeNamespace());
            } else {
                element = xmlBuilder2.e(parent.field);
            }
        }
        if (element != null && parent.attributes != null) {
            for (String field : parent.attributes.keySet()) {
                element.a(field, parent.attributes.get(field));
            }
        }
        if (children != null && !children.isEmpty()) {
            for (int i = children.size() - 1; i >= 0; i--) {
                XSDData xsdData = children.get(i);
                xmlBuilder(element != null ? element : xmlBuilder2, xsdData, xsdData.children);
                if (element != null) {
                    element.up();
                }
            }
        } else if (element != null) {
            element.t(parent.value != null ? parent.value : "");
        }
    }

    public void clear() {
        this.field = null;
        this.value = null;
        this.parent = null;
        cache.clear();
        fieldCache.clear();
        if (children != null && !children.isEmpty()) {
            for (XSDData xsdData : children) {
                xsdData.clear();
            }
        }
    }
}
