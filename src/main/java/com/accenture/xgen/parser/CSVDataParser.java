package com.accenture.xgen.parser;

import com.accenture.xgen.model.CSVData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

public class CSVDataParser {
    private int batchCount = 1000;
    private Iterator<CSVRecord> records;
    private String[] columns;
    private int colCountAve = 0;
    private String nameHeader;
    private static final int EMPTY_THREAD = 0;
    private static final String SPLITTER = ";";
    private String splitter;

    public CSVDataParser(String filepath, int batchCount, String splitter) {
        this(filepath, splitter);
        this.batchCount = batchCount;
    }

    public CSVDataParser(String filepath, String splitter) {
        this.splitter = splitter != null ? splitter : SPLITTER;
        Reader in = null;
        try {
            InputStream inputStream = new FileInputStream(filepath);
            in = new InputStreamReader(inputStream, "UTF-8");
            CSVParser parser = CSVFormat.EXCEL.newFormat(';').withQuote('"').parse(in);
            List<CSVRecord> list = parser.getRecords();
            list.remove(list.size() - 1);
            for(CSVRecord r : list) {
              String[] colValues = r.iterator().next().split(splitter);
            }
            records = list.iterator();
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
          synchronized (csvRecordIterator) {
            this.csvRecordIterator = csvRecordIterator;
          }
        }

        private Iterator<CSVRecord> getCsvRecordIterator() {
          synchronized (csvRecordIterator) {
            return csvRecordIterator;
          }
        }

        private List<CSVData> next() {
            int count = 0;
            batches = new LinkedList<CSVData>();
            while (csvRecordIterator.hasNext() && count < batchCount) {
                CSVRecord record = csvRecordIterator.next();
                String[] colValues = record.iterator().next().split(splitter);
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
        public int threadCount;
        private int maxThreadCount = 5;
        private boolean stopped = Boolean.FALSE;
        private String errorMessage;

        private ParseBatch iterator(CSVDataIterator csvDataIterator, int maxThreadCount) {
            this.csvDataIterator = csvDataIterator;
            this.maxThreadCount = maxThreadCount;
            return this;
        }

        private ParseBatch iterator(CSVDataIterator csvDataIterator) {
            this.csvDataIterator = csvDataIterator;
            return this;
        }

        private void startBatch() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    start();
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
            startBatch();
        }

        private void stop() {
            this.stopped = Boolean.TRUE;
        }

        private void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public void start() {
            try {
                final ParseBatch thisClass = this;
                if (!thisClass.stopped) {
                    if (thisClass.threadCount < thisClass.maxThreadCount) {
                        System.out.println("Thread Created");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (csvDataIterator.getCsvRecordIterator().hasNext()) {
                                        thisClass.threadCount++;
                                        callback(csvDataIterator.next(), thisClass);
                                        thisClass.threadCount--;
                                        System.out.println("Wait Around");
                                        thisClass.waitAround();
                                    } else if (thisClass.threadCount == EMPTY_THREAD) {
                                        System.out.println("Done");
                                        thisClass.done();
                                    }
                                } catch (Exception e) {
                                    thisClass.stop();
                                    thisClass.threadCount--;
                                    thisClass.setErrorMessage(e.getMessage());
                                }
                            }
                        }).start();
                    } else {
                        thisClass.waitAround();
                    }
                } else if (thisClass.threadCount > 0) {
                    thisClass.waitAround();
                } else {
                    failed(new ParseBatchException(this.errorMessage));
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
        nameHeader = record.iterator().next().split(splitter)[0];
    }

    public String getNameHeader() {
        return nameHeader;
    }
}
