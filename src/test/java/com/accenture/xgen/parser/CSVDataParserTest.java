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

    @Before
    public void setup() {
        filepath = getClass().getClassLoader().getResource("create_position.csv").getFile();
    }

    @Test
    public void testCSVDataParser() throws IOException {
        CSVDataParser csvDataParser = new CSVDataParser(filepath, 2);
        csvDataParser.parseByBatch(new CSVDataParser.ParseBatch() {
            @Override
            public void callback(List<CSVData> result, Thread nextThread) {
                System.out.println(result);
                nextThread.start();
            }
        });
    }
}
