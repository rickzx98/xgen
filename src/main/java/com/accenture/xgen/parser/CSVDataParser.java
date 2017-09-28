package com.accenture.xgen.parser;

import com.accenture.xgen.model.CSVData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CSVDataParser {
    private int batchCount = 1000;
    private Iterator<CSVRecord> records;
    private String[] columns;
    private int colCountAve = 0;
    private String nameHeader;

    public CSVDataParser(String filepath, int batchCount) {
        this(filepath);
        this.batchCount = batchCount;
    }

    public CSVDataParser(String filepath) {
        Reader in = null;
        try {
            in = new FileReader(filepath);
            records = CSVFormat.DEFAULT.parse(in).iterator();
        } catch (IOException e) {
            throw new CSVDataParserException(e.getMessage());
        }
        CSVRecord firstRow = records.hasNext() ? records.next() : null;
        if (firstRow == null) {
            throw new HeaderDetailNotFoundException(filepath);
        }
        setNameHeader(firstRow);
    }

    public void parseByBatch(ParseBatch parseBatch) {
        parseBatch.iterator(new CSVDataIterator(records)).start();
    }

    public void parseByBatch(ParseBatch parseBatch, int maxThreadCount) {
        parseBatch.iterator(new CSVDataIterator(records), maxThreadCount).start();
    }

    private class CSVDataIterator {
        private Iterator<CSVRecord> csvRecordIterator;
        private LinkedList<CSVData> batches;

        private CSVDataIterator(Iterator<CSVRecord> csvRecordIterator) {
            this.csvRecordIterator = csvRecordIterator;
        }

        private Iterator<CSVRecord> getCsvRecordIterator() {
            return csvRecordIterator;
        }

        private List<CSVData> next() {
            int count = 0;
            batches = new LinkedList<CSVData>();
            while (csvRecordIterator.hasNext() && count < batchCount) {
                CSVRecord record = csvRecordIterator.next();
                String[] colValues = record.iterator().next().split(";");
                CSVData.Builder csvDataBuilder = CSVData.Builder.create(record.getRecordNumber() - 2);
                for (int columnFieldIndex = 0; columnFieldIndex < colCountAve; columnFieldIndex++) {
                    csvDataBuilder.setValue(columns[columnFieldIndex],
                            colValues.length > columnFieldIndex ? colValues[columnFieldIndex] : null);
                }
                batches.add(csvDataBuilder.build());
                int colCount = colValues.length;
                if (colCountAve < colCount) {
                    colCountAve = colCount;
                    columns = colValues;
                    batches.poll();
                }
                count++;
            }
            if (batches.isEmpty() && csvRecordIterator.hasNext()) {
                return next();
            }
            return batches;
        }
    }

    public static abstract class ParseBatch {
        private CSVDataIterator csvDataIterator;
        private int threadCount;
        private int maxThreadCount = 5;

        private ParseBatch iterator(CSVDataIterator csvDataIterator, int maxThreadCount) {
            this.csvDataIterator = csvDataIterator;
            this.maxThreadCount = maxThreadCount;
            return this;
        }

        private ParseBatch iterator(CSVDataIterator csvDataIterator) {
            this.csvDataIterator = csvDataIterator;
            return this;
        }

        private void startBatch(final ParseBatch parseBatch) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    parseBatch.start();
                }
            }).start();
        }

        private void waitAround() {
            while (threadCount > maxThreadCount) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            startBatch(this);
        }

        public void start() {
            try {
                final ParseBatch thisClass = this;
                if (thisClass.threadCount < thisClass.maxThreadCount) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (csvDataIterator.getCsvRecordIterator().hasNext()) {
                                thisClass.threadCount++;
                                final List<CSVData> csvDataList = new ArrayList<CSVData>(csvDataIterator.next());
                                callback(csvDataList, thisClass);
                                thisClass.threadCount--;
                            } else if (thisClass.threadCount == 0) {
                                done();
                            } else {
                                waitAround();
                            }
                        }
                    }).start();
                } else {
                    waitAround();
                }

            } catch (Exception e) {
                failed(new ParseBatchException(e.getMessage()));
            }
        }

        public void failed(ParseBatchException parseBatchException) {
            throw new ParseBatchException(parseBatchException.getMessage());
        }

        public abstract void done();

        public abstract void callback(List<CSVData> result, ParseBatch nextBatch);
    }

    public static class ParseBatchException extends RuntimeException {
        private ParseBatchException(String msg) {
            super(msg);
        }
    }

    public static class HeaderDetailNotFoundException extends RuntimeException {
        private HeaderDetailNotFoundException(String msg) {
            super(String.format("No header detail found in csv file %s.", msg));
        }
    }

    public static class CSVDataParserException extends RuntimeException {
        CSVDataParserException(String msg) {
            super(msg);
        }
    }

    private void setNameHeader(CSVRecord record) {
        nameHeader = "";
        String[] splitted = record.iterator().next().split(";");
        for (int i = 0; i < splitted.length; i++) {
            nameHeader += splitted[i];
            if (i < (splitted.length - 1)) {
                nameHeader += "_";
            }
        }
    }

    public String getNameHeader() {
        return nameHeader;
    }
}
