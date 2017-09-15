package com.accenture.xgen.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CSVDataTest {
	
	@Test
	public void testCreateCSVData() {
		CSVData csvData = CSVData.Builder.create().setValue("sample", "sampleValue").build();
		String[] fields = { "sample" };
		String[] sampleValues = { "sampleValue" };
		Assert.assertNotNull(csvData);
		Assert.assertArrayEquals(fields, csvData.getFields());
		Assert.assertArrayEquals(sampleValues, csvData.getData());
		Assert.assertEquals("Field not found", "sampleValue", csvData.getValue("sample"));
	}

}
