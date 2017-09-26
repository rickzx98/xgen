package com.accenture.xgen.generator;

import com.accenture.xgen.model.CSVFilePath;
import com.accenture.xgen.model.DestinationPath;
import com.accenture.xgen.model.XSDFilePath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class XMLGeneratorTest {
    private String xsdFilePath;
    private String csvFilePath;
    private String destinationPath;

    @Before
    public void setUp() {
        xsdFilePath = getClass().getClassLoader().getResource("Create_Position_v1.xsd").getFile();
        csvFilePath = getClass().getClassLoader().getResource("create_position.csv").getFile();
        destinationPath = "C:\\Users\\jerico.g.de.guzman\\generated-data";
    }

    @Test
    public void testXMLGenerator() {
        XMLGenerator xmlGenerator = new XMLGenerator(new CSVFilePath(csvFilePath),
                new XSDFilePath(xsdFilePath),
                new DestinationPath(destinationPath), 100, 100);
    }
}

