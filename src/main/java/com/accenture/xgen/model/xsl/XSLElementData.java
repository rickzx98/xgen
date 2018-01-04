package com.accenture.xgen.model.xsl;

import com.accenture.xgen.model.StructureData;
import com.accenture.xgen.model.XSDData;

import java.util.ArrayList;
import java.util.List;

public class XSLElementData extends XSLData {
    protected List<XSLElementData> children;
    private String envelopNamespace;

    public XSLElementData(String field, String value, String prefix, String namespaceUri, StructureData structureData) {
        super(field, value, prefix, namespaceUri, structureData, XSLData.TYPE_ELEMENT);
        attributes = new ArrayList<XSLAttributeData>();
    }

    protected List<XSLAttributeData> attributes;

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
        XSLElementData foundXSD = null;
        if (hasFieldOf(field)) {
            foundXSD = this;
        } else if (children != null && !children.isEmpty()) {
            foundXSD = findFieldInChildren(field);
        }
        return foundXSD;
    }

    public XSLElementData findByFieldContains(String field) {
        XSLElementData foundXSL = null;
        if (containsFieldOf(field)) {
            foundXSL = this;
        } else if (children != null && !children.isEmpty()) {
            foundXSL = findFieldInChildrenContains(field);
        }
        return foundXSL;
    }

    private boolean hasFieldOf(String field) {
        return this.field.equals(field);
    }

    private boolean containsFieldOf(String field) {
        return this.field != null && this.field.toLowerCase().contains(field);
    }

    private XSLElementData findFieldInChildrenContains(String field) {
        XSLElementData foundXSLData = null;
        if (children != null) {
            for (XSLElementData xslData : children) {
                if (foundXSLData == null) {
                    foundXSLData = xslData.findByFieldContains(field);
                } else {
                    break;
                }
            }
        }
        return foundXSLData;
    }

    private XSLElementData findFieldInChildren(String field) {
        XSLElementData foundXSLData = null;
        if (children != null) {
            for (XSLElementData xslElementData : children) {
                if (foundXSLData == null) {
                    foundXSLData = (XSLElementData) xslElementData.findByField(field);
                } else {
                    break;
                }
            }
        }
        return foundXSLData;
    }

    @Override
    public String toFormattedString() {
        StringBuilder builder = new StringBuilder(String.format("<%s", field));
        if (attributes != null) {
            for (XSLAttributeData attributeData : attributes) {
                attributeData.setConstructedValue(constructedValue);
                builder.append(" ");
                builder.append(attributeData.toFormattedString());
            }
        }
        builder.append(">");
        if (children != null && !children.isEmpty()) {
            for (XSLElementData xslElementData : children) {
                xslElementData.setConstructedValue(constructedValue);
                builder.append("\n");
                builder.append(xslElementData.toFormattedString());
            }
        } else {
            builder.append(getValueFromConstructedMap());
        }
        builder.append(String.format("</%s>", field));
        return builder.toString();
    }

    public void addAttribute(XSLAttributeData xslAttributeData) {
        attributes.add(xslAttributeData);
    }

    public void addChild(XSLElementData xslElementData) {
        if (children == null) {
            children = new ArrayList<XSLElementData>();
        }
        children.add(xslElementData);
    }

    private boolean isEnvelope() {
        boolean hasEnvelope = Boolean.FALSE;
        if (attributes != null) {
            for (XSLAttributeData xslAttributeData : attributes) {
                hasEnvelope = xslAttributeData.getValue().contains("envelope");
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
                for (XSLAttributeData xslAttributeData : attributes) {
                    if (xslAttributeData.getValue().contains("envelope")) {
                        namespace = xslAttributeData.getField().split(":")[1];
                        break;
                    }
                }
            }
            envelopNamespace = namespace + ":Envelope";
        }
        return envelopNamespace;
    }

    public String getXmlStartTag() {
        StringBuilder startTag = new StringBuilder("<%s>");
        if (isEnvelope()) {
            startTag = new StringBuilder(String.format(startTag.toString(), getEnvelopeNamespace()));
        } else {
            startTag = new StringBuilder(String.format(startTag.toString(), this.field));
        }

        if (attributes != null && !attributes.isEmpty()) {
            startTag = new StringBuilder(startTag.substring(0, startTag.length() - 1));
            for (XSLAttributeData xslAttributeData : attributes) {
                startTag.append(String.format(" %s=\"%s\"", xslAttributeData.getField(), xslAttributeData.getValueFromConstructedMap()));
            }
            startTag.append(">");
        }
        return startTag.toString();
    }

    public String getXmlEndTag() {
        String endTag = "</%s>";
        if (isEnvelope()) {
            endTag = String.format(endTag, getEnvelopeNamespace());
        } else {
            endTag = String.format(endTag, this.field);
        }
        return endTag;
    }

    public List<XSLElementData> getChildren() {
        return children;
    }
}
