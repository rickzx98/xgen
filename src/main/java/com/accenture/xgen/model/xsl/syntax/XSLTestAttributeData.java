package com.accenture.xgen.model.xsl.syntax;

import com.accenture.xgen.model.StructureData;
import com.accenture.xgen.model.xsl.XSLAttributeData;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class XSLTestAttributeData extends XSLAttributeData {
    public static final String KEYWORD = "test";
    private ScriptEngine scriptEngine;
    private static final String SYNTAX_REG = "\\$\\{.*}";

    public XSLTestAttributeData(String field, String value, String prefix, String namespaceUri, StructureData structureData) {
        super(field, value, prefix, namespaceUri, structureData);
        ScriptEngineManager manager = new ScriptEngineManager();
        scriptEngine = manager.getEngineByName("JavaScript");
    }

    public static class TestAttributeNotFoundException extends RuntimeException {
        public TestAttributeNotFoundException(String element) {
            super(String.format("Missing test attribute in %s element tag", element));
        }
    }

    public static class TestSyntaxErrorException extends RuntimeException {
        public TestSyntaxErrorException(String element) {
            super(String.format("Syntax error for test attribute in %s element tag", element));
        }
    }

    public boolean evaluate() {
        boolean valid = Boolean.FALSE;
        if (value.matches(SYNTAX_REG)) {
            for (String key : constructedValue.keySet()) {
                scriptEngine.put(key, constructedValue.get(key));
            }
        } else {
            throw new TestSyntaxErrorException(field);
        }
        try {
            valid = (Boolean)scriptEngine.eval(value.replace("$", "")
                    .replace("{", "").replace("}", ""));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return valid;
    }
}
