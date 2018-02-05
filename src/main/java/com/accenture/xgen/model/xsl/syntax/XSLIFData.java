package com.accenture.xgen.model.xsl.syntax;

import com.accenture.xgen.model.StructureData;
import com.accenture.xgen.model.xsl.XSLAttributeData;
import com.accenture.xgen.model.xsl.XSLElementData;

public class XSLIFData extends XSLElementData {
    public static final String KEYWORD = "if";

    public XSLIFData(String field, String value, String prefix, String namespaceUri, StructureData structureData) {
        super(field, value, prefix, namespaceUri, structureData);
    }

    @Override
    public String toFormattedString() {
        if (test()) {
            StringBuilder builder = new StringBuilder();
            if (children != null && !children.isEmpty()) {
                for (XSLElementData xslElementData : children) {
                    xslElementData.setConstructedValue(constructedValue);
                    builder.append("\n");
                    builder.append(xslElementData.toFormattedString());
                }
            } else {
                builder.append(getValueFromConstructedMap());
            }
            return builder.toString();
        }
        return "";
    }

    public boolean test() {
        XSLTestAttributeData xslTestAttributeData = null;
        for (XSLAttributeData xslAttributeData : attributes) {
            if (XSLTestAttributeData.KEYWORD.equals(xslAttributeData.getField())) {
                xslTestAttributeData = (XSLTestAttributeData) xslAttributeData;
                break;
            }
        }
        if (xslTestAttributeData == null) {
            throw new XSLTestAttributeData.TestAttributeNotFoundException(this.field);
        }
        xslTestAttributeData.setConstructedValue(this.constructedValue);
        return xslTestAttributeData.evaluate();
    }

}
