package com.accenture.xgen.generator;

import com.accenture.xgen.model.*;
import com.accenture.xgen.parser.CSVDataParser;
import com.accenture.xgen.parser.XSDParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class XMLGenerator {
    private int batchCount;
    private CSVFilePath csvFilePath;
    private XSDFilePath xsdFilePath;
    private DestinationPath destinationPath;
    private boolean isDone = Boolean.FALSE;
    private int maxThreadCount = 5;
    private int timeout = 300000;

    private XMLGenerator() {
        this.batchCount = 1000;
    }

    public XMLGenerator(CSVFilePath csvFilePath, XSDFilePath xsdFilePath, DestinationPath destinationPath, int batchCount, int maxThreadCount, int timeout) {
        this(csvFilePath, xsdFilePath, destinationPath);
        this.batchCount = batchCount;
        this.maxThreadCount = maxThreadCount;
        this.timeout = timeout;
    }

    public XMLGenerator(CSVFilePath csvFilePath, XSDFilePath xsdFilePath, DestinationPath destinationPath, int batchCount, int maxThreadCount) {
        this(csvFilePath, xsdFilePath, destinationPath);
        this.batchCount = batchCount;
        this.maxThreadCount = maxThreadCount;
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

    public void waitAround() {
        int mili = 0;
        while (!isDone) {
            try {
                if (mili < timeout) {
                    mili++;
                    Thread.sleep(100);
                } else {
                    throw new XMLGeneratorTimeoutException();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFormattedFolderName(String nameHeader) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return String.format("%s_%s_%s_%s", nameHeader, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }

    public XMLGenerator generate() {
        final CSVDataParser csvDataParser = new CSVDataParser(csvFilePath.toString(), batchCount);
        final XSDData xsdTemplate = new XSDParser(xsdFilePath.toString()).getRoot();
        final XSDData schema = xsdTemplate.findByField("schema");
        final XSDData body = xsdTemplate.findByFieldContains("body");

        final File destinationFolder = new File(destinationPath.toFile(), getFormattedFolderName(csvDataParser.getNameHeader()));
        if (!destinationFolder.exists()) {
            destinationFolder.mkdir();
        }
        csvDataParser.parseByBatch(new CSVDataParser.ParseBatch() {
            private int count = 0;

            @Override
            public void failed(CSVDataParser.ParseBatchException parseBatchException) {
                isDone = Boolean.TRUE;
                if (destinationFolder.exists()) {
                    destinationFolder.delete();
                }
                throw parseBatchException;
            }

            public void done() {
                isDone = Boolean.TRUE;
            }

            @Override
            public void callback(final List<CSVData> result, final CSVDataParser.ParseBatch nextBatch) {
                nextBatch.start();
                File innerFileXml = new File(destinationFolder, String.format("%s_%d.xml", csvDataParser.getNameHeader(), ++count));
                BufferedWriter bw = null;
                FileWriter fw = null;
                boolean toBeDeleted = Boolean.FALSE;
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
                } catch (XMLStructure.IncompatibleSchemaException c) {
                    toBeDeleted = Boolean.TRUE;
                    throw c;
                } catch (IOException e) {
                    isDone = Boolean.TRUE;
                    throw new XMLGeneratorException(e.getMessage());
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                        if (fw != null) {
                            fw.close();
                        }
                        if (toBeDeleted) {
                            innerFileXml.delete();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, maxThreadCount);
        return this;
    }

    private class XMLGeneratorException extends RuntimeException {
        private XMLGeneratorException(String msg) {
            super(msg);
        }
    }

    private class XMLGeneratorTimeoutException extends RuntimeException {
        private XMLGeneratorTimeoutException() {
            super();
        }
    }
}
