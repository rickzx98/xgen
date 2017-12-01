package com.accenture.xgen.generator;

import com.accenture.xgen.model.CSVData;
import com.accenture.xgen.model.Structure;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RunWith(JUnit4.class)
public class XMLStructureTest {
    private String xsdFilePath;
    private String csvFilePath;

    @Before
    public void setUp() {
        xsdFilePath = getClass().getClassLoader().getResource("Create_Position_v1.xsd").getFile();
        csvFilePath = getClass().getClassLoader().getResource("sample-data-single.csv").getFile();
    }

    @Test
    public void testXmlStructure() throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        final XSDData xsdRoot = new XSDParser(xsdFilePath).getRoot();
        CSVDataParser csvDataParser = new CSVDataParser(csvFilePath,null);
        final List<String> xmls = new ArrayList<String>();
        csvDataParser.parseByBatch(new CSVDataParser.ParseBatch() {
            @Override
            public void done() {
                System.out.println("completed");
            }

            @Override
            public void callback(List<CSVData> result, CSVDataParser.ParseBatch nextBatch) {
                System.out.println("running batch");
                for (CSVData csvData : result) {
                    try {
                        xmls.add(csvData.construct(new XMLStructure(), xsdRoot));
                    } catch (XMLStructure.IncompatibleSchemaException e) {
                        e.printStackTrace();
                    } catch (Structure.StructureException e) {
                        e.printStackTrace();
                    }
                }
                nextBatch.start();
            }
        });
        xsdRoot.clear();
    }
}
