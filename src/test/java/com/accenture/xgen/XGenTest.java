package com.accenture.xgen;

import com.accenture.xgen.model.CSVFilePath;
import com.accenture.xgen.model.DestinationPath;
import com.accenture.xgen.model.Separator;
import com.accenture.xgen.model.XSDFilePath;
import org.apache.ws.commons.schema.XmlSchemaSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class XGenTest {
    private String xsdFilePath;
    private String csvFilePath;
    private String destinationPath;

    @Before
    public void setUp() {
        xsdFilePath = getClass().getClassLoader().getResource("PERSONAL_INFO.XSD").getFile();
        csvFilePath = getClass().getClassLoader().getResource("PERSONAL_INFO_2018-01-12.csv").getFile();
        //csvFilePath = getClass().getClassLoader().getResource("create_position.csv").getFile();
        destinationPath = "C:\\Users\\jerico.g.de.guzman\\generated-data";
    }

    @Test
    public void testGenerateXMLFiles() throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        XGen.generateXMLFiles(new CSVFilePath(csvFilePath),
                new XSDFilePath(xsdFilePath),
                new DestinationPath(destinationPath), 1000, 10, new Separator("\\|")).waitAround();
    }
}
