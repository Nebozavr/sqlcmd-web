package ua.com.juja.sqlcmd.model;

import java.util.Arrays;

public class DataSet {

    class Data {
        private String name;
        private Object value;

        public Data(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    public Data[] data = new Data[100];
    public int index = 0;

    public void put(String name, Object value) {
        data[index++] = new Data(name, value);
    }

    public Object get(String name) {
        for (int i = 0; i < index; index++) {
            if (data[i].getName().equals(name)) {
                return data[i].getValue();
            }
        }
        return null;
    }

    public Object[] getValues() {
        Object[] result = new Object[index];
        for (int i = 0; i < index; i++) {
            result[i] = data[i].getValue();
        }
        return result;
    }

    public String[] getNames() {
        String[] result = new String[index];
        for (int i = 0; i < index; i++) {
            result[i] = data[i].getName();
        }
        return result;
    }

    @Override
    public String toString() {
        return "DataSet{\n" +
                "names: " + Arrays.toString(getNames()) + "\n" +
                "values: " + Arrays.toString(getValues()) + "\n" +
                "}";
    }
}
