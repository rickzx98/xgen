package com.accenture.xgen.model;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;

public class CSVData implements DocumentData {
    public String[] fields;
    public String[] data;
    private long recordNumber;

    private CSVData(String[] fields, String[] data, long recordNumber) {
        this.fields = fields;
        this.data = data;
        this.recordNumber = recordNumber;
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

    public String construct(Structure structure, StructureData structureData, Map<String, String> placeHolderDefaultsValueMap) throws Structure.StructureException {
        for (String field : fields) {
            String value = getValue(field);
            if (placeHolderDefaultsValueMap != null && !placeHolderDefaultsValueMap.isEmpty()) {
                if (placeHolderDefaultsValueMap.containsKey(field) && (value == null || value.isEmpty())) {
                    value = placeHolderDefaultsValueMap.get(field);
                }
            }
            structure.put(field, value);
        }
        return structure.build(structureData);
    }

    public long recordNumber() {
        return recordNumber;
    }

    public static class Builder {
        private Map<String, String> mappings;
        private long recordNumber;

        private Builder(long recordNumber) {
            this.recordNumber = recordNumber;
            mappings = new HashMap<String, String>();
        }

        public static Builder create(long recordNumber) {
            return new Builder(recordNumber);
        }

        public Builder setValue(String field, String value) {
            value = StringEscapeUtils.unescapeJava(value);
            mappings.put(field, value);
            return this;
        }

        public CSVData build() {
            String fields[] = mappings.keySet().toArray(new String[mappings.keySet().size()]);
            String data[] = mappings.values().toArray(new String[mappings.values().size()]);
            return new CSVData(fields, data, recordNumber);
        }
    }

}
