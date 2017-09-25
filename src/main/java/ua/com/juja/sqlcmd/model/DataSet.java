package ua.com.juja.sqlcmd.model;

import java.util.*;

public class DataSet {

    private Map<String, Object> data = new LinkedHashMap<>();

    public void put(String name, Object value) {
        data.put(name, value);
    }

    public List<Object> getValues() {
        return new ArrayList<>(data.values());
    }

    public Set<String> getNames() {
        return data.keySet();
    }

    @Override
    public String toString() {
        return "DataSet{\n" +
                "names: " + (getNames().toString()) + "\n" +
                "values: " + (getValues().toString()) + "\n" +
                "}";
    }
}
