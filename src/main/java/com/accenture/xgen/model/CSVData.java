package com.accenture.xgen.model;

import java.util.HashMap;
import java.util.Map;

public class CSVData implements DocumentData {
    public String[] fields;
    public String[] data;

    private CSVData(String[] fields, String[] data) {
        this.fields = fields;
        this.data = data;
    }

    public String[] getFields() {
        return fields;
    }

    public String[] getData() {
        return data;
    }

    public String getValue(String field) {
        int index;
        for (index = -1; index <= fields.length; index++) {
            if (index > -1 && fields[index] == field)
                break;
        }
        if (index == -1) {
            return null;
        }
        return data[index];
    }

    public String construct(Structure structure, StructureData structureData) throws Structure.StructureException {
        for (String field : fields) {
            String value = getValue(field);
            structure.put(field, value);
        }
        return structure.build(structureData);
    }

    public static class Builder {
        private Map<String, String> mappings;

        private Builder() {
            mappings = new HashMap<String, String>();
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder setValue(String field, String value) {
            mappings.put(field, value);
            return this;
        }

        public CSVData build() {
            String fields[] = mappings.keySet().toArray(new String[mappings.keySet().size()]);
            String data[] = mappings.values().toArray(new String[mappings.values().size()]);
            return new CSVData(fields, data);
        }
    }

}
