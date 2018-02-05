package com.accenture.xgen.parser;

import java.io.IOException;
import java.util.List;

import com.accenture.xgen.model.CSVData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CSVDataParserTest {
    private String filepath;
    private int threadRan;

    @Before
    public void setup() {
        filepath = getClass().getClassLoader().getResource("create_position.csv").getFile();
        threadRan = 0;
    }
  
    public void testCSVDataParser() throws IOException {
        CSVDataParser csvDataParser = new CSVDataParser(filepath, 3, null);
        csvDataParser.parseByBatch(new CSVDataParser.ParseBatch() {
            public void done() {
            }

            @Override
            public void callback(final List<CSVData> result, final CSVDataParser.ParseBatch nextBatch) {
                new Thread(new Runnable() {
                    public void run() {
                        threadRan++;
                        System.out.println(result);
                        nextBatch.start();
                    }
                }).start();
                while (threadRan < 3) {
                    System.out.println("waiting for complete threads: " + threadRan);
                }
                Assert.assertEquals(3, threadRan);
            }
        });
    }
}
