package com.accenture.xgen;

import com.accenture.xgen.generator.XSLGenerator;
import com.accenture.xgen.model.CSVFilePath;
import com.accenture.xgen.model.DestinationPath;
import com.accenture.xgen.model.Separator;
import com.accenture.xgen.model.XSDFilePath;
import org.apache.ws.commons.schema.XmlSchemaSerializer;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.lang.Long;

public class XGen {
  
    private static Map<String, String> placeHolderDefaultValueMapHolder = null;
    private static final XGen xGen = new XGen();
  
    public static XSLGenerator generateXMLFiles(CSVFilePath csvFilePath, XSDFilePath xsdFilePath,
                                                DestinationPath destinationPath, Separator separator, String version) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        return new XSLGenerator(csvFilePath, xsdFilePath, destinationPath)
                .separator(separator.toString()).setPlaceHolderValueMap(setPlaceHolderMap(version)).generate();
    }

    public static XSLGenerator generateXMLFiles(
            CSVFilePath csvFilePath,
            XSDFilePath xsdFilePath,
            DestinationPath destinationPath, int batchCount,
            Separator separator, String version) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        return new XSLGenerator(csvFilePath, xsdFilePath, destinationPath, batchCount)
                .separator(separator.toString()).setPlaceHolderValueMap(setPlaceHolderMap(version)).generate();
    }

    public static XSLGenerator generateXMLFiles(
            CSVFilePath csvFilePath,
            XSDFilePath xsdFilePath,
            DestinationPath destinationPath,
            int batchCount,
            int maxThreadCount,
            Separator separator, String version) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        return new XSLGenerator(csvFilePath, xsdFilePath, destinationPath, batchCount, maxThreadCount)
                .separator(separator.toString()).setPlaceHolderValueMap(setPlaceHolderMap(version)).generate();
    }

    public static XSLGenerator generateXMLFiles(
            CSVFilePath csvFilePath,
            XSDFilePath xsdFilePath,
            DestinationPath destinationPath, int batchCount,
            int maxThreadCount, int timeout,
            Separator separator, String version) throws IOException, XmlSchemaSerializer.XmlSchemaSerializerException {
        return new XSLGenerator(csvFilePath, xsdFilePath, destinationPath, batchCount, maxThreadCount, timeout)
                .separator(separator.toString()).setPlaceHolderValueMap(setPlaceHolderMap(version)).generate();
    }
  
    private static final Map<String, String> setPlaceHolderMap(String version) {
      Map<String, String> placeHolderDefaultsMap = new HashMap<String, String>();
        placeHolderDefaultsMap.put("version", version);
        //placeHolderDefaultsMap.put("ETHNICITY","123");
      placeHolderDefaultValueMapHolder = placeHolderDefaultsMap;
      return placeHolderDefaultsMap;
    }
  
    public static String getVersion() {
      return placeHolderDefaultValueMapHolder.get("version");
    }

}
