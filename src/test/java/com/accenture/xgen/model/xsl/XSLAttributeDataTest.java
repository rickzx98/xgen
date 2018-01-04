package com.accenture.xgen.model.xsl;

import com.accenture.xgen.model.xsl.syntax.XSLTestAttributeData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class XSLAttributeDataTest {
    private XSLAttributeData xslAttributeData;

    @Test
    public void testNormalAttributeValue() {
        xslAttributeData = new XSLTestAttributeData("normal", "hello", null, null, null);
        String formatted = xslAttributeData.toFormattedString();
        xslAttributeData.clear();
        Assert.assertEquals("normal=\"hello\"", formatted);
    }

    @Test
    public void testVarAttributeValue() {
        xslAttributeData = new XSLTestAttributeData("variable", "${hello}", null, null, null);
        Map<String, String> constructedMap = new HashMap<String, String>();
        constructedMap.put("hello", "hi");
        xslAttributeData.setConstructedValue(constructedMap);
        String formatted = xslAttributeData.toFormattedString();
        xslAttributeData.clear();
        Assert.assertEquals("variable=\"hi\"", formatted);
    }

}
