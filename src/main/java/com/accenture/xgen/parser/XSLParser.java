package com.accenture.xgen.parser;

import com.accenture.xgen.model.StructureData;
import com.accenture.xgen.model.xsl.XSLAttributeData;
import com.accenture.xgen.model.xsl.XSLElementData;
import com.accenture.xgen.model.xsl.syntax.XSLIFData;
import com.accenture.xgen.model.xsl.syntax.XSLTestAttributeData;
import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XSLParser {
    private List<StructureData> parsedData;
    private StructureData root;

    public XSLParser(String filepath) {
        InputStream is = null;
        try {
            is = new FileInputStream(filepath);
        } catch (FileNotFoundException e) {
            throw new XSLParserException(e.getMessage());
        }
        XmlSchemaCollection schemaCol = new XmlSchemaCollection();
        XmlSchema schema = schemaCol.read(new StreamSource(is), null);
        Document[] docs = schema.getAllSchemas();
        parsedData = new ArrayList<StructureData>();
        for (Document doc : docs) {
            root = new XSLElementData(doc.getNodeName(), doc.getNodeValue(), doc.getPrefix(), doc.getNamespaceURI(), null);
            parseNodeBody(doc.getChildNodes(), (XSLElementData) root);
        }
    }

    private void parseNodeBody(NodeList nodeList, XSLElementData parent) {
        int length = nodeList.getLength();
        for (int i = 0; i < length; i++) {
            Node node = nodeList.item(i);
            node.normalize();
            StructureData structureData = null;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equals(XSLIFData.KEYWORD)) {
                    structureData = new XSLIFData(node.getNodeName(), node.getNodeValue(), node.getPrefix(), node.getNamespaceURI(), parent);
                } else {
                    structureData = new XSLElementData(node.getNodeName(), node.getNodeValue(),
                            node.getPrefix(), node.getNamespaceURI(), parent);
                }
                addAttributes(node, structureData);
            }
            if (structureData != null) {
                XSLElementData xslElementData = (XSLElementData) structureData;
                parent.addChild(xslElementData);
                parseNodeBody(node.getChildNodes(), xslElementData);
            }
        }
    }

    private void parseNodeBody(NodeList nodeList, List<StructureData> parsedData, StructureData parent) {
        int length = nodeList.getLength();
        for (int i = 0; i < length; i++) {
            Node node = nodeList.item(i);
            node.normalize();
            StructureData structureData = null;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equals(XSLIFData.KEYWORD)) {
                    structureData = new XSLIFData(node.getNodeName(), node.getNodeValue(), node.getPrefix(), node.getNamespaceURI(), parent);
                } else {
                    structureData = new XSLElementData(node.getNodeName(), node.getNodeValue(),
                            node.getPrefix(), node.getNamespaceURI(), parent);
                }
                addAttributes(node, structureData);
            }
            if (structureData != null) {
                parsedData.add(structureData);
                parseNodeBody(node.getChildNodes(), parsedData, structureData);
            }
        }
    }

    private void addAttributes(Node node, StructureData structureData) {
        int attributeSize = node.getAttributes().getLength();
        for (int i = 0; i < attributeSize; i++) {
            Node attribute = node.getAttributes().item(i);
            if (!attribute.getNodeValue().equals("unqualified")) {
                if (XSLTestAttributeData.KEYWORD.equals(attribute.getNodeName())) {
                    ((XSLElementData) structureData).addAttribute(new XSLTestAttributeData(attribute.getNodeName(), attribute.getNodeValue(), attribute.getPrefix(), attribute.getNamespaceURI(), structureData));
                } else {
                    ((XSLElementData) structureData).addAttribute(new XSLAttributeData(attribute.getNodeName(), attribute.getNodeValue(), attribute.getPrefix(), attribute.getNamespaceURI(), structureData));
                }
            }
        }
    }

    public List<StructureData> getParsedData() {
        return parsedData;
    }

    public StructureData getRoot() {
        return root;
    }

    public static class XSLParserException extends RuntimeException {
        XSLParserException(String msg) {
            super(msg);
        }
    }
}
