package com.accenture.xgen.generator;

import com.accenture.xgen.model.CSVData;
import com.accenture.xgen.model.XSDData;
import com.accenture.xgen.parser.CSVDataParser;
import com.accenture.xgen.parser.XSDParser;
import org.apache.ws.commons.schema.XmlSchemaSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RunWith(JUnit4.class)
public class XMLStructureTest {
    private String xsdFilePath;
    private String csvFilePath;

    @Before
    public void setUp() {
        xsdFilePath = getClass().getClassLoader().getResource("Create_Position_v1.xsd").getFile();
        csvFilePath = getClass().getClassLoader().getResource("create_position.csv").getFile();
    }

    @Test
    public void testXmlStructure() throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        XSDData xsdRoot = new XSDParser(xsdFilePath.toString()).getRoot();
        CSVDataParser csvDataParser = new CSVDataParser(csvFilePath.toString());
        csvDataParser.parseByBatch(new CSVDataParser.ParseBatch() {
            @Override
            public void callback(List<CSVData> result, CSVDataParser.ParseBatch nextBatch) {

                nextBatch.start();
            }
        });
    }
}
