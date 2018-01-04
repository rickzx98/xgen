package com.accenture.xgen.model.xsl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class XSLElementDataTest {

    private XSLElementData xslElementData;


    @Test
    public void testNormalXSLElementData() {
        xslElementData = new XSLElementData("normal", "hello", null, null, null);
        addNormalAttributes(xslElementData);
        addChildNodes(xslElementData);
        String formattedString = xslElementData.toFormattedString();
        System.out.println(formattedString);
    }

    public void addNormalAttributes(XSLElementData xslElementData) {
        xslElementData.addAttribute(new XSLAttributeData("normal1", "value1", null, null, null));
        xslElementData.addAttribute(new XSLAttributeData("normal2", "value2", null, null, null));
        xslElementData.addAttribute(new XSLAttributeData("normal3", "value3", null, null, null));
    }

    public void addChildNodes(XSLElementData xslElementData){
        xslElementData.addChild(new XSLElementData("child1", "value1", null, null, xslElementData));
        xslElementData.addChild(new XSLElementData("child2", "value2", null, null, xslElementData));
        xslElementData.addChild(new XSLElementData("child3", "value3", null, null, xslElementData));
    }

}
