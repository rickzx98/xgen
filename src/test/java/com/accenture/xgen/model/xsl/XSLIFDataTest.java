package com.accenture.xgen.model.xsl;

import com.accenture.xgen.model.xsl.syntax.XSLIFData;
import com.accenture.xgen.model.xsl.syntax.XSLTestAttributeData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class XSLIFDataTest {

    private XSLIFData xslifData;
    private XSLTestAttributeData xslTestAttributeData;

    @Before
    public void setup() {
        xslifData = new XSLIFData("if", null, null, null, null);
        xslTestAttributeData = new XSLTestAttributeData("test", "${hello!='hi'}", null, null, xslifData);
        xslifData.addAttribute(xslTestAttributeData);
    }

    @Test
    public void testEvaluate(){
        Map<String, String> constructedValue = new HashMap<String, String>();
        constructedValue.put("hello","hello");
        xslifData.setConstructedValue(constructedValue);
        Assert.assertTrue(xslifData.test());
    }


    @Test
    public void testEvaluateFalse(){
        Map<String, String> constructedValue = new HashMap<String, String>();
        constructedValue.put("hello","hi");
        xslifData.setConstructedValue(constructedValue);
        Assert.assertFalse(xslifData.test());
    }
}
