package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.exceptions.WrongNumberParametersException;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.RequestErrorException;
import ua.com.juja.sqlcmd.view.View;

public class UpdateData implements Command {
    public static final String UPDATE_DATA_SAMPLE = "update|tableName|columnNameWhere|valueWhere|columnNameSet|valueSet";

    private final View view;
    private final DatabaseManager manager;

    public UpdateData(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("update|");
    }

    @Override
    public void process(String command) throws WrongNumberParametersException {
        String[] data = command.split("\\|");
        if (data.length != 6) {
            throw new WrongNumberParametersException("Error entering command, must be like " +
                    UPDATE_DATA_SAMPLE + ", but you enter:" + command);
        }

        String tableName = data[1];
        String columnWhere = data[2];
        String valuesWhere = data[3];
        String columnSet = data[4];
        String valuesSet = data[5];

        DataSet dataSet = new DataSet();
        dataSet.put(columnSet, valuesSet);

        DataSet dataWhere = new DataSet();
        dataWhere.put(columnWhere, valuesWhere);

        try {
            manager.update(tableName, dataWhere, dataSet);
            view.write(String.format("Data from %s was updated", tableName));
        } catch (RequestErrorException e) {
            view.writeError(e);
        }
    }
}
