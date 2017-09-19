package com.accenture.xgen.generator;

import com.accenture.xgen.model.CSVData;
import com.accenture.xgen.model.CSVFilePath;
import com.accenture.xgen.model.XSDData;
import com.accenture.xgen.model.XSDFilePath;
import com.accenture.xgen.parser.CSVDataParser;
import com.accenture.xgen.parser.XSDParser;
import org.apache.ws.commons.schema.XmlSchemaSerializer;

import java.io.IOException;
import java.util.List;

public class XMLGenerator {
    public XMLGenerator(CSVFilePath csvFilePath, XSDFilePath xsdFilePath) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        CSVDataParser csvDataParser = new CSVDataParser(csvFilePath.toString());
        final XSDData xsdData = new XSDParser(xsdFilePath.toString()).getRoot();
        csvDataParser.parseByBatch(new CSVDataParser.ParseBatch() {
            @Override
            public void callback(List<CSVData> result, CSVDataParser.ParseBatch nextBatch) {
                for (CSVData csvData : result) {

                }
            }
        });
    }
}
