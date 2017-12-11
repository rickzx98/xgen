package com.accenture.xgen;

import com.accenture.xgen.generator.XMLGenerator;
import com.accenture.xgen.model.CSVFilePath;
import com.accenture.xgen.model.DestinationPath;
import com.accenture.xgen.model.Separator;
import com.accenture.xgen.model.XSDFilePath;
import org.apache.ws.commons.schema.XmlSchemaSerializer;

import java.io.IOException;

public class XGen {
    public static XMLGenerator generateXMLFiles(CSVFilePath csvFilePath, XSDFilePath xsdFilePath,
                                                DestinationPath destinationPath, Separator separator) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        return new XMLGenerator(csvFilePath, xsdFilePath, destinationPath)
                .separator(separator.toString()).generate();
    }

    public static XMLGenerator generateXMLFiles(
            CSVFilePath csvFilePath,
            XSDFilePath xsdFilePath,
            DestinationPath destinationPath, int batchCount,
            Separator separator) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        return new XMLGenerator(csvFilePath, xsdFilePath, destinationPath, batchCount)
                .separator(separator.toString()).generate();
    }

    public static XMLGenerator generateXMLFiles(
            CSVFilePath csvFilePath,
            XSDFilePath xsdFilePath,
            DestinationPath destinationPath, int batchCount, int maxThreadCount, Separator separator) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        return new XMLGenerator(csvFilePath, xsdFilePath, destinationPath, batchCount, maxThreadCount)
                .separator(separator.toString()).generate();
    }

    public static XMLGenerator generateXMLFiles(
            CSVFilePath csvFilePath,
            XSDFilePath xsdFilePath,
            DestinationPath destinationPath, int batchCount, int maxThreadCount, int timeout, Separator separator) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        return new XMLGenerator(csvFilePath, xsdFilePath, destinationPath, batchCount, maxThreadCount, timeout)
                .separator(separator.toString()).generate();
    }


}
