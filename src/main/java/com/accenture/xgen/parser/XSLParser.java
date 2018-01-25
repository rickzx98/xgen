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
import org.xml.sax.InputSource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XSLParser {
    private List<StructureData> parsedData;
    private StructureData root;
    private static final String REG_EXP = "\\s(.*)(\'.*\'>)";

    public XSLParser(String filepath) {
        InputStream is = null;
        try {
            is = new FileInputStream(filepath);
        } catch (FileNotFoundException e) {
            throw new XSLParserException(e.getMessage());
        }
        boolean foundKeywords = Boolean.FALSE;
        StringBuilder schemaBuilder = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fileReader);
            String sCurrentLine = null;
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.contains("<#if")) {
                    Pattern pattern = Pattern.compile(REG_EXP);
                    Matcher matcher = pattern.matcher(sCurrentLine);
                    String expression = null;
                    while (matcher.find()) {
                        expression = matcher.group(0)
                                .replace("<#if", "")
                                .replace(" ", "")
                                .replace(">", "");
                        if (!expression.contains("!") && expression.contains("=")) {
                            expression = expression.replace("=", "===");
                        }
                    }
                    String newExp = String.format(" test=\"\\$\\{%s\\}\">", expression);
                    sCurrentLine = sCurrentLine
                            .replace("#if", "if")
                            .replaceAll(REG_EXP, newExp);
                    sCurrentLine = "<if" + sCurrentLine;
                    foundKeywords = Boolean.TRUE;
                } else if (sCurrentLine.contains("</#if>")) {
                    sCurrentLine = sCurrentLine.replace("#if", "if");
                }
                schemaBuilder.append(sCurrentLine);
            }
        } catch (FileNotFoundException e) {
            throw new XSLParserException(e.getMessage());
        } catch (IOException e) {
            throw new XSLParserException(e.getMessage());
        }

        XmlSchemaCollection schemaCol = new XmlSchemaCollection();
        XmlSchema schema = null;
        if (foundKeywords) {
            schema = schemaCol.read(new StringReader(schemaBuilder.toString()), null);
        } else {
            schema = schemaCol.read(new InputSource(is), null);
        }

        Document[] docs = schema.getAllSchemas();
        parsedData = new ArrayList<StructureData>();
        for (Document doc : docs) {
            root = new XSLElementData(doc.getNodeName(), doc.getTextContent(), doc.getPrefix(), doc.getNamespaceURI(), null);
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
                    structureData = new XSLIFData(node.getNodeName(), node.getTextContent(), node.getPrefix(), node.getNamespaceURI(), parent);
                } else {
                    structureData = new XSLElementData(node.getNodeName(), node.getTextContent(),
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
