package com.accenture.xgen;

import com.accenture.xgen.generator.XMLGenerator;
import com.accenture.xgen.model.CSVFilePath;
import com.accenture.xgen.model.DestinationPath;
import com.accenture.xgen.model.XSDFilePath;
import org.apache.ws.commons.schema.XmlSchemaSerializer;

import java.io.IOException;

public class XGen {
    public static void generateXMLFiles(CSVFilePath csvFilePath, XSDFilePath xsdFilePath, DestinationPath destinationPath) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        new XMLGenerator(csvFilePath, xsdFilePath, destinationPath).generate();
    }

    public static void generateXMLFiles(
            CSVFilePath csvFilePath,
            XSDFilePath xsdFilePath,
            DestinationPath destinationPath, int batchCount) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        new XMLGenerator(csvFilePath, xsdFilePath, destinationPath, batchCount).generate();
    }
}
