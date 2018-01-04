# Project XGEN
CSV to XML generator

## Getting Started 

### Basic Usage 

```java

public class Main {

    public static void main(String... args) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        String xsdFilePath = "C:\\Users\\jerico.g.de.guzman\\generated-data\\Create_Position_v1.xsd";
        String csvFilePath = "C:\\Users\\jerico.g.de.guzman\\generated-data\\create_position.csv";
        String destinationPath = "C:\\Users\\jerico.g.de.guzman\\generated-data";

        XMLGenerator xmlGenerator = XGen.generateXMLFiles(
                new CSVFilePath(csvFilePath),
                new XSDFilePath(xsdFilePath),
                new DestinationPath(destinationPath), 100, new Separator("\\|");
        
        xmlGenerator.generate();

    }
}

```

Note: To run the generator on a single thread application invoke the method xmlGenerator.generate().waitAround() to wait for the process to finish before moving on to the next line.
By default the method will wait for 5 minutes beyond that it will throw an XMLGeneratorTimeoutException.

### XGen

#### Methods
Modifier and Type     | Method and Description
----------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
static XSLGenerator   | generateXMLFiles(CSVFilePath csvFilePath, XSDFilePath xsdFilePath, DestinationPath destinationPath, Separator separator) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException
static XSLGenerator   | generateXMLFiles(CSVFilePath csvFilePath, XSDFilePath xsdFilePath, DestinationPath destinationPath, int batchCount, int maxThreadCount, Separator separator) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException
static XSLGenerator   | generateXMLFiles(CSVFilePath csvFilePath, XSDFilePath xsdFilePath, DestinationPath destinationPath, int batchCount, int maxThreadCount, int timeout, Separator separator) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException



### Support for "if" element node

attribute | Description
----------|-------------
test      | handles the javascript condition logic (must always return boolean)

```xsd
         <if test="${SUP_ORG_REF != 'N/A'}">
            <bsvc:Supervisory_Organization_Reference bsvc:Descriptor="${JOB_POSTING_ID}">
            <!--Zero or more repetitions:-->
            <bsvc:ID bsvc:type="?">${SUP_ORG_REF}</bsvc:ID>
            </bsvc:Supervisory_Organization_Reference>
        </if>
```