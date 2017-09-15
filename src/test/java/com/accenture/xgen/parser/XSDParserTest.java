package com.accenture.xgen.parser;

import org.apache.ws.commons.schema.XmlSchemaSerializer;
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
    }
}
