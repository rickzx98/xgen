package com.accenture.xgen.generator;

import com.accenture.xgen.model.CSVFilePath;
import com.accenture.xgen.model.DestinationPath;
import com.accenture.xgen.model.XSDFilePath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Map;
import java.util.HashMap;


@RunWith(JUnit4.class)
public class XSLGeneratorTest {
    private String xsdFilePath;
    private String csvFilePath;
    private String destinationPath;

    @Before
    public void setUp() {
        xsdFilePath = getClass().getClassLoader().getResource("Create_Position_v1.xsd").getFile();
        csvFilePath = getClass().getClassLoader().getResource("sample-data-single-pipe.csv").getFile();
        //csvFilePath = getClass().getClassLoader().getResource("create_position.csv").getFile();
        destinationPath = "C:\\Users\\jerico.g.de.guzman\\generated-data";
    }

    public void testGenerator(){
        // given
        Map<String, String> placeHolderDefaultsMap = new HashMap<String, String>();
        placeHolderDefaultsMap.put("version", "1.1.0");
        placeHolderDefaultsMap.put("WORKER_TYPE", "DEFAULT_ID");
      
        XSLGenerator xslGenerator  = new XSLGenerator(new CSVFilePath(csvFilePath),
                new XSDFilePath(xsdFilePath),
                new DestinationPath(destinationPath), 100, 100);
        System.out.println("HERE");
        xslGenerator.setPlaceHolderValueMap(placeHolderDefaultsMap);
      System.out.println("THERE");
      
        // when
        System.out.println("Generating");
        xslGenerator.generate();
      System.out.println("Done generating");
      
        // then
    }

}
