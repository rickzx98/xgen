package com.accenture.xgen.generator;

import com.accenture.xgen.model.*;
import com.accenture.xgen.parser.CSVDataParser;
import com.accenture.xgen.parser.XSDParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XMLGenerator {
    private int batchCount;
    private CSVFilePath csvFilePath;
    private XSDFilePath xsdFilePath;
    private DestinationPath destinationPath;

    private XMLGenerator() {
        this.batchCount = 1000;
    }

    public XMLGenerator(CSVFilePath csvFilePath, XSDFilePath xsdFilePath, DestinationPath destinationPath, int batchCount) {
        this(csvFilePath, xsdFilePath, destinationPath);
        this.batchCount = batchCount;
    }

    public XMLGenerator(CSVFilePath csvFilePath, XSDFilePath xsdFilePath, DestinationPath destinationPath) {
        this();
        this.csvFilePath = csvFilePath;
        this.xsdFilePath = xsdFilePath;
        this.destinationPath = destinationPath;
    }

    public void generate() {
        CSVDataParser csvDataParser = new CSVDataParser(csvFilePath.toString(), batchCount);
        final XSDData xsdTemplate = new XSDParser(xsdFilePath.toString()).getRoot();
        final XSDData schema = xsdTemplate.findByField("schema");
        final XSDData body = xsdTemplate.findByFieldContains("body");

        final File destinationFolder = new File(destinationPath.toFile(), csvDataParser.getNameHeader());
        if (!destinationFolder.exists()) {
            destinationFolder.mkdir();
        }
        csvDataParser.parseByBatch(new CSVDataParser.ParseBatch() {
            private int count = 0;

            @Override
            public void callback(final List<CSVData> result, final CSVDataParser.ParseBatch nextBatch) {
                File innerFileXml = new File(destinationFolder, String.format("%d.xml", ++count));
                BufferedWriter bw = null;
                FileWriter fw = null;
                try {
                    fw = new FileWriter(innerFileXml);
                    bw = new BufferedWriter(fw);
                    bw.write(schema.getXmlStartTag());
                    bw.write(body.getXmlStartTag());
                    for (CSVData csvData : result) {
                        bw.write(csvData.construct(new XMLStructure(), xsdTemplate));
                    }
                    bw.write(body.getXmlEndTag());
                    bw.write(schema.getXmlEndTag());
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
                nextBatch.start();
            }
        });
    }

    private class XMLGeneratorException extends RuntimeException {
        private XMLGeneratorException(String msg) {
            super(msg);
        }
    }
}
