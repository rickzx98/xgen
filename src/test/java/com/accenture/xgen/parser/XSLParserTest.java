package com.accenture.xgen.parser;

import com.accenture.xgen.model.xsl.XSLElementData;
import org.apache.ws.commons.schema.XmlSchemaSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class XSLParserTest {

    private String filepath;

    @Before
    public void setup() {
        filepath = getClass().getClassLoader().getResource("Create_Position_v1.xsd").getFile();
    }

    @Test
    public void testXLSParser() throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        XSLParser xslParser = new XSLParser(filepath);
        XSLElementData root = (XSLElementData) xslParser.getRoot();

        System.out.println(root.toFormattedString());
    }

    public void testXSModelParser() throws InstantiationException, IllegalAccessException, ClassNotFoundException {

    }
}
