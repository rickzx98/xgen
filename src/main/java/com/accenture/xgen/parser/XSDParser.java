package com.accenture.xgen.parser;

import com.accenture.xgen.model.XSDData;
import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.apache.ws.commons.schema.XmlSchemaSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XSDParser {
    private XSDData root;

    public XSDParser(String filepath) throws FileNotFoundException, XmlSchemaSerializer.XmlSchemaSerializerException {
        InputStream is = new FileInputStream(filepath);
        XmlSchemaCollection schemaCol = new XmlSchemaCollection();
        XmlSchema schema = schemaCol.read(new StreamSource(is), null);
        Document[] docs = schema.getAllSchemas();
        root = null;
        for (Document doc : docs) {
            root = new XSDData("document", doc.getTextContent(), doc.getAttributes());
            parseNodeBody(doc.getChildNodes(), root);
        }
    }

    private void parseNodeBody(NodeList nodeList, XSDData xsdData) {
        int length = nodeList.getLength();
        for (int i = 0; i < length; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                node.normalize();
                XSDData newData = new XSDData(node.getNodeName(), node.getTextContent(), node.getAttributes());
                parseNodeBody(node.getChildNodes(), newData);
                xsdData.addChild(newData);
            }
        }
    }

    public XSDData getRoot() {
        return root;
    }
}
