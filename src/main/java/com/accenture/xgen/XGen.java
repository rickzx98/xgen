package com.accenture.xgen;

import com.accenture.xgen.generator.XMLGenerator;
import com.accenture.xgen.model.CSVFilePath;
import com.accenture.xgen.model.DestinationPath;
import com.accenture.xgen.model.XSDFilePath;
import org.apache.ws.commons.schema.XmlSchemaSerializer;

import java.io.IOException;

public class XGen {
    public static XMLGenerator generateXMLFiles(CSVFilePath csvFilePath, XSDFilePath xsdFilePath, DestinationPath destinationPath) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        return new XMLGenerator(csvFilePath, xsdFilePath, destinationPath).generate();
    }

    public static XMLGenerator generateXMLFiles(
            CSVFilePath csvFilePath,
            XSDFilePath xsdFilePath,
            DestinationPath destinationPath, int batchCount) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        return new XMLGenerator(csvFilePath, xsdFilePath, destinationPath, batchCount).generate();
    }

    public static XMLGenerator generateXMLFiles(
            CSVFilePath csvFilePath,
            XSDFilePath xsdFilePath,
            DestinationPath destinationPath, int batchCount, int maxThreadCount) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        return new XMLGenerator(csvFilePath, xsdFilePath, destinationPath, batchCount, maxThreadCount).generate();
    }

    public static XMLGenerator generateXMLFiles(
            CSVFilePath csvFilePath,
            XSDFilePath xsdFilePath,
            DestinationPath destinationPath, int batchCount, int maxThreadCount, int timeout) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        return new XMLGenerator(csvFilePath, xsdFilePath, destinationPath, batchCount, maxThreadCount, timeout).generate();
    }


}
