package ua.com.juja.sqlcmd.view;

public interface View {

    void write(String message);

    void writeError(Exception e);

    String read();

}
