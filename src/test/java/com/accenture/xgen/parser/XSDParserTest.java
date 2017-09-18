package com.accenture.xgen.parser;

import com.accenture.xgen.model.XSDData;
import org.apache.ws.commons.schema.XmlSchemaSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class XSDParserTest {

    private String filepath;

    @Before
    public void setup() {
        filepath = getClass().getClassLoader().getResource("Create_Position_v1.xsd").getFile();
    }

    @Test
    public void testXSDParser() throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        XSDParser xsdParser = new XSDParser(filepath);
        XSDData root = xsdParser.getRoot();
        Assert.assertNotNull(root);
        XSDData originalRef =  root.findByValue("${SUP_ORG_REF}");
        Assert.assertNotNull(originalRef);
        Assert.assertTrue(originalRef.hasFieldOf("bsvc:ID"));
        Assert.assertTrue(originalRef.hasValueOf("${SUP_ORG_REF}"));
    }
}
