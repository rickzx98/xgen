package com.accenture.xgen.generator;

import com.accenture.xgen.model.*;
import com.accenture.xgen.parser.CSVDataParser;
import com.accenture.xgen.parser.XSDParser;
import com.jamesmurty.utils.XMLBuilder2;
import org.apache.ws.commons.schema.XmlSchemaSerializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XMLGenerator {
    public XMLGenerator(CSVFilePath csvFilePath, XSDFilePath xsdFilePath, DestinationPath destinationPath) {
        CSVDataParser csvDataParser = new CSVDataParser(csvFilePath.toString());
        final XSDData xsdData = new XSDParser(xsdFilePath.toString()).getRoot();
        final File destinationFolder = new File(destinationPath.toFile(), csvDataParser.getNameHeader());
        if (!destinationFolder.exists()) {
            destinationFolder.mkdir();
        }

        csvDataParser.parseByBatch(new CSVDataParser.ParseBatch() {
            @Override
            public void callback(List<CSVData> result, CSVDataParser.ParseBatch nextBatch) {
                for (CSVData csvData : result) {
                    File innerFileXml = new File(destinationFolder, String.format("%d.xml", csvData.recordNumber()));
                    BufferedWriter bw = null;
                    FileWriter fw = null;
                    try {
                        fw = new FileWriter(innerFileXml);
                        bw = new BufferedWriter(fw);
                        bw.write(csvData.construct(new XMLStructure(), xsdData));
                    } catch (IOException e) {
                        throw new XMLGeneratorException(e.getMessage());
                    } finally {
                        try {
                            if (bw != null) {
                                bw.close();
                            }
                            if (fw != null) {
                                fw.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                nextBatch.start();
            }
        });
        xsdData.clear();
    }

    public static class XMLGeneratorException extends RuntimeException {
        private XMLGeneratorException(String msg) {
            super(msg);
        }
    }
}
