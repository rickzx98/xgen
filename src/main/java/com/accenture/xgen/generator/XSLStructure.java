package com.accenture.xgen.generator;

import com.accenture.xgen.model.Structure;
import com.accenture.xgen.model.StructureData;
import com.accenture.xgen.model.xsl.XSLElementData;

import java.util.Map;

public class XSLStructure extends Structure {

    @Override
    protected String construct(Map<String, String> constructedValue, StructureData structureData) throws StructureException {
        XSLElementData xslElementData = (XSLElementData) structureData;
        XSLElementData body = xslElementData.findByFieldContains("body");
        body.setConstructedValue(constructedValue);
        StringBuilder xslStructure = new StringBuilder();
        for (XSLElementData xslE : body.getChildren()) {
            xslE.setConstructedValue(constructedValue);
            xslStructure.append("\n");
            xslStructure.append(xslE.toFormattedString());
        }
        return xslStructure.toString();
    }
}
