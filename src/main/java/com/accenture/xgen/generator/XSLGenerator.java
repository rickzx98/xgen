package com.accenture.xgen.generator;

import com.accenture.xgen.model.CSVData;
import com.accenture.xgen.model.CSVFilePath;
import com.accenture.xgen.model.DestinationPath;
import com.accenture.xgen.model.XSDFilePath;
import com.accenture.xgen.model.xsl.XSLElementData;
import com.accenture.xgen.parser.CSVDataParser;
import com.accenture.xgen.parser.XSLParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class XSLGenerator {
    private int batchCount;
    private CSVFilePath csvFilePath;
    private XSDFilePath xsdFilePath;
    private DestinationPath destinationPath;
    private boolean isDone = Boolean.FALSE;
    private int maxThreadCount = 5;
    private int timeout = 300000;
    private String splitter;
    private Map<String, String> placeHolderDefaultValueMap = null;

    private XSLGenerator() {
        this.batchCount = 1000;
    }

    public XSLGenerator(CSVFilePath csvFilePath, XSDFilePath xsdFilePath, DestinationPath destinationPath, int batchCount, int maxThreadCount, int timeout) {
        this(csvFilePath, xsdFilePath, destinationPath);
        this.batchCount = batchCount;
        this.maxThreadCount = maxThreadCount;
        this.timeout = timeout;
    }

    public XSLGenerator(CSVFilePath csvFilePath, XSDFilePath xsdFilePath, DestinationPath destinationPath, int batchCount, int maxThreadCount) {
        this(csvFilePath, xsdFilePath, destinationPath);
        this.batchCount = batchCount;
        this.maxThreadCount = maxThreadCount;
    }

    public XSLGenerator(CSVFilePath csvFilePath, XSDFilePath xsdFilePath, DestinationPath destinationPath, int batchCount) {
        this(csvFilePath, xsdFilePath, destinationPath);
        this.batchCount = batchCount;
    }

    public XSLGenerator(CSVFilePath csvFilePath, XSDFilePath xsdFilePath, DestinationPath destinationPath) {
        this();
        this.csvFilePath = csvFilePath;
        this.xsdFilePath = xsdFilePath;
        this.destinationPath = destinationPath;
    }
  
    public XSLGenerator setPlaceHolderValueMap(Map<String, String> placeHolderDefaultValueMap) {
      this.placeHolderDefaultValueMap = placeHolderDefaultValueMap;
      return this;
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
        return String.format("%s_%s_%s_%s_%s_%s_%s", nameHeader,
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }

    public XSLGenerator generate() {
        final CSVDataParser csvDataParser = new CSVDataParser(csvFilePath.toString(), batchCount, this.splitter);
        final XSLElementData xsdTemplate = (XSLElementData) new XSLParser(xsdFilePath.toString()).getRoot();
        final XSLElementData schema = (XSLElementData) xsdTemplate.findByField("schema");
        final XSLElementData body = xsdTemplate.findByFieldContains("body");
        final XSLElementData header = xsdTemplate.findByFieldContains("header");
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
                    bw.write(header.toFormattedString());
                    bw.write(body.getXmlStartTag());
                    for (CSVData csvData : result) {
                        bw.write(csvData.construct(new XSLStructure(), xsdTemplate, placeHolderDefaultValueMap));
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

    public XSLGenerator separator(String splitter) {
        this.splitter = splitter;
        return this;
    }
}
