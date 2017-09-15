package com.accenture.xgen.parser;

import com.accenture.xgen.model.CSVData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CSVDataParser {
    private int batchCount = 1000;
    private Iterator<CSVRecord> records;
    private String[] columns;

    public CSVDataParser(String filepath, int batchCount) throws IOException {
        this(filepath);
        this.batchCount = batchCount;
    }

    public CSVDataParser(String filepath) throws IOException {
        Reader in = new FileReader(filepath);
        records = CSVFormat.DEFAULT.parse(in).iterator();
    }

    public void parseByBatch(ParseBatch parseBatch) {
        new Thread(parseBatch.iterator(new CSVDataIterator(records))).start();
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
            int colCountAve = 0;
            batches = new LinkedList<CSVData>();
            while (csvRecordIterator.hasNext() && count < batchCount) {
                CSVRecord record = csvRecordIterator.next();
                String[] colValues = record.iterator().next().split(";");
                CSVData.Builder csvDataBuilder = CSVData.Builder.create();
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
            return batches;
        }
    }

    public static abstract class ParseBatch implements Runnable {
        private CSVDataIterator csvDataIterator;

        private ParseBatch iterator(CSVDataIterator csvDataIterator) {
            this.csvDataIterator = csvDataIterator;
            return this;
        }

        @Override
        public void run() {
            if (csvDataIterator.getCsvRecordIterator().hasNext()) {
                callback(csvDataIterator.next(), new Thread(this));
            }
        }

        public abstract void callback(List<CSVData> result, Thread nextThread);
    }
}
