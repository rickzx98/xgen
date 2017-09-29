# Project XGEN
CSV to XML generator

## Getting Started 

### Basic Usage 

```java
  
import com.accenture.xgen.XGen;
import com.accenture.xgen.generator.XMLGenerator;
import com.accenture.xgen.model.CSVFilePath;
import com.accenture.xgen.model.DestinationPath;
import com.accenture.xgen.model.XSDFilePath;
import org.apache.ws.commons.schema.XmlSchemaSerializer;

import java.io.IOException;

public class Main {

    public static void main(String... args) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        String xsdFilePath = "C:\\Users\\jerico.g.de.guzman\\generated-data\\Create_Position_v1.xsd";
        String csvFilePath = "C:\\Users\\jerico.g.de.guzman\\generated-data\\create_position.csv";
        String destinationPath = "C:\\Users\\jerico.g.de.guzman\\generated-data";

        XMLGenerator xmlGenerator = XGen.generateXMLFiles(
                new CSVFilePath(csvFilePath),
                new XSDFilePath(xsdFilePath),
                new DestinationPath(destinationPath));
        
        xmlGenerator.generate();

    }
}

```

Note: To run the generator on a single thread application invoke the method xmlGenerator.generate().waitAround() to wait for the process to finish before moving on to the next line.
By default the method will wait for 5 minutes beyond that it will throw an XMLGeneratorTimeoutException. 


### With batch count, max thread and timeout.

```java
  
import com.accenture.xgen.XGen;
import com.accenture.xgen.generator.XMLGenerator;
import com.accenture.xgen.model.CSVFilePath;
import com.accenture.xgen.model.DestinationPath;
import com.accenture.xgen.model.XSDFilePath;
import org.apache.ws.commons.schema.XmlSchemaSerializer;

import java.io.IOException;

public class Main {

    public static void main(String... args) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        String xsdFilePath = "C:\\Users\\jerico.g.de.guzman\\generated-data\\Create_Position_v1.xsd";
        String csvFilePath = "C:\\Users\\jerico.g.de.guzman\\generated-data\\create_position.csv";
        String destinationPath = "C:\\Users\\jerico.g.de.guzman\\generated-data";

        XMLGenerator xmlGenerator = XGen.generateXMLFiles(
                new CSVFilePath(csvFilePath),
                new XSDFilePath(xsdFilePath),
                new DestinationPath(destinationPath), batchcount: 1000, maxThreadCount: 10, timeout: 300000);
        
        xmlGenerator.generate();

    }
}

```


