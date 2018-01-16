package ua.com.juja.sqlcmd.controller.web.actions;

import ua.com.juja.sqlcmd.controller.web.AbstractAction;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.exceptions.PgSQLDatabaseManagerException;
import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ControlAction extends AbstractAction {
    public ControlAction(Service service) {
        super(service);
    }

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("table");
        try {
            req.setAttribute("tableNames", service.find(getDB_manager(req, resp), tableName));
            goTo("control.jsp", req, resp);
        } catch (PgSQLDatabaseManagerException e) {
            exceptionPage(e, req, resp);
        }
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            DatabaseManager manager = getDB_manager(req, resp);
            String action = req.getParameter("action");
            String tableName = req.getParameter("table");

            if (action.equals("Clear")) {
                service.clear(manager, tableName);
                redirectTo("control?table=" + tableName, resp);
            } else if (action.equals("Drop")) {
                service.drop(manager, tableName);
                redirectTo("list", resp);
            } else if (action.equals("Insert")) {

                List<String> result = new LinkedList<>();
                List<String> columnsName = service.find(manager, tableName).get(0);
                for (String value : columnsName) {
                    result.add(value);
                    result.add(req.getParameter(value));
                }
                service.insert(manager, tableName, result);
                redirectTo("control?table=" + tableName, resp);
            } else if (action.equals("Update")) {

                List<String> result = new LinkedList<>();
                result.add(req.getParameter("columnWhere"));
                result.add(req.getParameter("valueWhere"));
                result.add(req.getParameter("columnSet"));
                result.add(req.getParameter("valueSet"));

                service.update(manager, tableName, result);
                redirectTo("control?table=" + tableName, resp);
            } else if (action.equals("Delete")) {
                String columnName = req.getParameter("columnDelete");
                String value = req.getParameter("valueDelete");
                service.delete(manager, tableName, columnName, value);
                redirectTo("control?table=" + tableName, resp);
            }
        } catch (PgSQLDatabaseManagerException e) {
            exceptionPage(e, req, resp);
        }
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/control");
    }
}
